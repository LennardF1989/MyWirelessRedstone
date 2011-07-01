package com.lennardf1989.bukkit;

import java.io.BufferedReader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.SQLitePlatform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.avaje.ebeaninternal.server.lib.sql.TransactionIsolation;

public abstract class MyJavaPlugin extends JavaPlugin {
    private boolean usingSQLite;
    private Level loggerLevel;
    
    //TODO: Config file replacement/override/mkdirs
    //TODO: Test if table is dropped if it contains entries, otherwise add drop/create to config
    public void onEnable() {        
        //Check if the database is enabled
        if (getConfiguration().getNode("database") == null)
            return;
        
        //Create a data folder for this plugin if it does not exist yet
        getDataFolder().mkdirs();
        
        //Logging needs to be set back to the original level, no matter what happens
        try {            
            //Disable all logging
            disableDatabaseLogging();
            
            //Setup the database based on the configuration file
            setupDatabase();
            
            //Create all tables
            installDatabase();
        }
        catch(Exception ex) {
            throw new RuntimeException("An exception has occured while preparing the database", ex);
        }
        finally {
            //Enable all logging
            enableDatabaseLogging();
        }
    }
    
    private void setupDatabase() {       
        //Setup the data source
        DataSourceConfig ds = new DataSourceConfig();
        ds.setDriver(getConfiguration().getString("database.driver"));
        ds.setUrl(getConfiguration().getString("database.url"));
        ds.setUsername(getConfiguration().getString("database.username"));
        ds.setPassword(getConfiguration().getString("database.password"));
        ds.setIsolationLevel(TransactionIsolation.getLevel(getConfiguration().getString("database.isolation")));

        //Setup the server configuration
        ServerConfig db = new ServerConfig();
        db.setDefaultServer(false);
        db.setRegister(false);
        db.setClasses(getDatabaseClasses());
        db.setName(getDescription().getName());
        ds.setUrl(replaceDatabaseString(ds.getUrl()));
        
        //Check if we are using the SQLite JDBC supplied with Bukkit
        if (ds.getDriver().equalsIgnoreCase("org.sqlite.JDBC")) {
            //Remember the database is a SQLite-database
            usingSQLite = true;
            
            //Modify the platform, as SQLite has no AUTO_INCREMENT field
            db.setDatabasePlatform(new SQLitePlatform());
            db.getDatabasePlatform().getDbDdlSyntax().setIdentity("");
        }

        //Finally the data source
        db.setDataSourceConfig(ds);
        
        //Setup the database itself
        ClassLoader previous = Thread.currentThread().getContextClassLoader();

        try {
            //Something Bukkit
            Thread.currentThread().setContextClassLoader(getClassLoader());
            
            //Set the private "ebean" variable in JavaPlugin to public
            Field f = JavaPlugin.class.getDeclaredField("ebean");
            Field.setAccessible(new Field[] { f }, true);
            
            //Create a new instance of the EbeanServer 
            f.set(this, EbeanServerFactory.create(db));
        }
        catch(PersistenceException ex) {
            throw new RuntimeException("Failed to create a new instance of the EbeanServer", ex);
        }
        catch(Exception ex) {
            throw new RuntimeException("Failed to modify the database in JavaPlugin using reflection", ex);
        }
        finally {
            //Something Bukkit
            Thread.currentThread().setContextClassLoader(previous);
        }
    }
    
    private void installDatabase() {
        //Create a DDL generator
        SpiEbeanServer serv = (SpiEbeanServer) getDatabase();
        DdlGenerator gen = serv.getDdlGenerator();
        
        //Generate a DropDDL-script
        gen.runScript(true, gen.generateDropDdl());
        
        //Generate a CreateDDL-script
        if(usingSQLite) {
            //If SQLite is being used, the CreateDLL-script has to be validated and potentially fixed to be valid
            gen.runScript(false, validateCreateDDLSqlite(gen.generateCreateDdl()));
        }
        else {
            gen.runScript(false, gen.generateCreateDdl());
        }
    }
    
    private String replaceDatabaseString(String input) {
        input = input.replaceAll("\\{DIR\\}", getDataFolder().getPath().replaceAll("\\\\", "/") + "/");
        input = input.replaceAll("\\{NAME\\}", getDescription().getName().replaceAll("[^\\w_-]", ""));
        
        return input;
    }
    
    private String validateCreateDDLSqlite(String oldScript) {
        try {
            //Create a BufferedReader out of the potentially invalid script
            BufferedReader scriptReader = new BufferedReader(new StringReader(oldScript));
            
            //Create an array to store all the lines we encounter
            List<String> scriptLines = new ArrayList<String>();
            
            //Create some additional variables for keeping track of tables
            HashMap<String, Integer> foundTables = new HashMap<String, Integer>();
            String currentTable = null;
            int tableOffset = 0;
            
            //Loop through all lines
            String currentLine;
            while ((currentLine = scriptReader.readLine()) != null) {
                //Trim the current line as we don't need trailing spaces
                currentLine = currentLine.trim();
                
                //Add the current line to the rest of the lines
                scriptLines.add(currentLine.trim());
                              
                //Check if the current line is of any use
                if(currentLine.startsWith("create table")) {
                    //Found a table so get its name and remember the line we encountered it on
                    currentTable = currentLine.split(" ", 4)[2];
                    foundTables.put(currentLine.split(" ", 3)[2], scriptLines.size() - 1);
                }
                else if(currentLine.startsWith(";") && currentTable != null && !currentTable.equals("")) {
                    //Found the end of a table definition, so update the entry
                    foundTables.put(currentTable, scriptLines.size() - 1);
                    
                    //Reset the table-tracker
                    currentTable = null;
                }
                else if(currentLine.startsWith("alter table")) {
                    //Found a potentially unsupported action
                    String[] alterTableLine = currentLine.split(" ", 4);
                    
                    if(alterTableLine[3].startsWith("add constraint")) {
                        //Found an unsupported action: ALTER TABLE using ADD CONSTRAINT
                        String[] addConstraintLine = alterTableLine[3].split(" ", 4);
                        
                        //Check if we can somehow fix this line
                        if(addConstraintLine[3].startsWith("foreign key")) {
                            //Calculate the index of last line of the current table
                            int tableLastLine = foundTables.get(alterTableLine[2]) + tableOffset;
                            
                            //Change ";" to ");"
                            scriptLines.set(tableLastLine, ");");
                            
                            //Change "...))" to "...),"
                            String otherLine = scriptLines.get(tableLastLine - 1);
                            otherLine = otherLine.substring(0, otherLine.length() - 1);
                            scriptLines.set(tableLastLine - 1, otherLine + ",");
                            
                            //Add the constraint as a new line - Remove the ";" on the end
                            scriptLines.add(tableLastLine, addConstraintLine[3].substring(0, addConstraintLine[3].length() - 1));
                            
                            //Remove this line and raise the table offset, since we added a line
                            scriptLines.remove(scriptLines.size() - 1);
                            tableOffset++;
                        }
                        else {
                            //Exception: This line cannot be fixed but is known the be unsupported by SQLite
                            throw new RuntimeException("Unsupported action encountered: ALTER TABLE using ADD CONSTRAINT with " + addConstraintLine[3]);
                        }
                    }
                }
            }
            
            //Turn all the lines back into a single string
            String newScript = "";
            for(String newLine : scriptLines) {
                newScript += newLine + "\n";
            }
            
            //Return the fixed script
            return newScript;
        } 
        catch (Exception ex) {
            //Exception: Failed to fix the DDL or something just went plain wrong
            throw new RuntimeException("Failed to validate the CreateDDL-script for SQLite", ex);
        }
    }
    
    private void disableDatabaseLogging() {
        //If logging is allowed, nothing has to be changed
        if(getConfiguration().getBoolean("database.logging", false))
            return;
        
        //Retrieve the level of the root logger
        loggerLevel = Logger.getLogger("").getLevel();
        
        //Set the level the root logger to OFF
        Logger.getLogger("").setLevel(Level.OFF);
    }
    
    private void enableDatabaseLogging() {
        //If logging is allowed, nothing has to be changed
        if(getConfiguration().getBoolean("database.logging", false))
            return;
        
        //Set the level of the root logger back to the original value
        Logger.getLogger("").setLevel(loggerLevel);
    }
}

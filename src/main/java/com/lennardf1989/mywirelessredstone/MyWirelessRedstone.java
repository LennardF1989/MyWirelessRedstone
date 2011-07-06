package com.lennardf1989.mywirelessredstone;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.avaje.ebean.EbeanServer;
import com.lennardf1989.bukkitex.MyDatabase;
import com.lennardf1989.mywirelessredstone.listeners.MWRBlockListener;
import com.lennardf1989.mywirelessredstone.listeners.MWREntityListener;

public class MyWirelessRedstone extends JavaPlugin {
    private static MyWirelessRedstone plugin;
    private MyDatabase database;
    
    public void onEnable() {
        try {
            //Store reference to this class
            plugin = this;
            
            //Load the configuration
            loadConfiguration();
            
            //Initialize the database
            initializeDatabase();
            
            //Register all required listeners
            new MWRBlockListener().registerEvents();
            new MWREntityListener().registerEvents();

            //Plugin succesfully enabled
            System.out.print(String.format("[%s v%s] has been succesfully enabled!", getDescription().getName(), getDescription().getVersion()));
        }
        catch(Exception ex) {
            //Plugin failed to enable
            System.out.print(String.format("[%s v%s] could not be enabled!", getDescription().getName(), getDescription().getVersion()));

            //Print the stack trace of the actual cause
            Throwable t = ex;
            while(t != null) {
                if(t.getCause() == null) {
                    System.out.println(String.format("[%s v%s] exception:", getDescription().getName(), getDescription().getVersion()));
                    t.printStackTrace();
                }

                t = t.getCause();
            }
        }
    }

    public void onDisable() {
        System.out.println(String.format("%s version %s is disabled!", getDescription().getName(), getDescription().getVersion()));
    }

    public static MyWirelessRedstone getInstance() {
        return plugin;
    }

    private void loadConfiguration() {
        Configuration config = getConfiguration();
        
        config.setProperty("database.driver", config.getString("database.driver", "org.sqlite.JDBC"));
        config.setProperty("database.url", config.getString("database.url", "jdbc:sqlite:{DIR}{NAME}.db"));
        config.setProperty("database.username", config.getString("database.username", "root"));
        config.setProperty("database.password", config.getString("database.password", ""));
        config.setProperty("database.isolation", config.getString("database.isolation", "SERIALIZABLE"));
        config.setProperty("database.logging", config.getBoolean("database.logging", false));
        config.setProperty("database.rebuild", config.getBoolean("database.rebuild", true));
        
        config.save();
    }
    
    private void initializeDatabase() {
        Configuration config = getConfiguration();
        
        database = new MWRDatabase(this);
        database.initializeDatabase(
                config.getString("database.driver"),
                config.getString("database.url"),
                config.getString("database.username"),
                config.getString("database.password"),
                config.getString("database.isolation"),
                config.getBoolean("database.logging", false),
                config.getBoolean("database.rebuild", true)
                );
        
        config.setProperty("database.rebuild", false);
        config.save();
    }
    
    @Override
    public EbeanServer getDatabase() {
        return database.getDatabase();
    }
    
    /*
     * Get all channels from database
     * New sign -> persist and add to local array
     */
    
    /*database = new MyDatabase(this) {
        protected java.util.List<Class<?>> getDatabaseClasses() {
            List<Class<?>> list = new ArrayList<Class<?>>();
            list.add(MWRSign.class);
            list.add(MWRTransmitter.class);
            list.add(MWRReceiver.class);
            list.add(MWRChannel.class);
    
            return list;
        };
    };
    
    database.initializeDatabase(
            config.getString("database.driver"),
            config.getString("database.url"),
            config.getString("database.username"),
            config.getString("database.password"),
            config.getString("database.isolation"),
            config.getBoolean("database.logging", false),
            config.getBoolean("database.rebuild", true)
        );*/
    
    /*private void initializeDatabaseHardcoded() {
        Configuration config = getConfiguration();
        
        mwrDatabase = new MWRDatabase(this, getClassLoader());
        mwrDatabase.initializeDatabase(
                "org.sqlite.JDBC",
                "jdbc:sqlite:{DIR}{NAME}.db",
                "root",
                "",
                "SERIALIZABLE",
                config.getBoolean("database.logging", false),
                config.getBoolean("database.rebuild", true)
                );
        
        config.setProperty("database.rebuild", false);
        config.save();
    }*/
}

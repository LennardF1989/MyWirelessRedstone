package com.lennardf1989.mywirelessredstone;

import java.util.ArrayList;
import java.util.List;

import com.lennardf1989.bukkit.MyJavaPlugin;
import com.lennardf1989.mywirelessredstone.listeners.MWRBlockListener;
import com.lennardf1989.mywirelessredstone.listeners.MWREntityListener;
import com.lennardf1989.mywirelessredstone.models.MWRChannel;
import com.lennardf1989.mywirelessredstone.models.MWRReceiver;
import com.lennardf1989.mywirelessredstone.models.MWRSign;
import com.lennardf1989.mywirelessredstone.models.MWRTransmitter;

public class MyWirelessRedstone extends MyJavaPlugin {
    private static MyWirelessRedstone plugin;
    
	public void onEnable() {
	    try {
    	    //Call the super method
    	    super.onEnable();
    	    
    		//Store reference to this class
    		plugin = this;
    		
    		//Register all required listeners
    		//TODO: Create manager for all events/database activities
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

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(MWRSign.class);
        list.add(MWRTransmitter.class);
        list.add(MWRReceiver.class);
        list.add(MWRChannel.class);
        
        return list;
    }
}

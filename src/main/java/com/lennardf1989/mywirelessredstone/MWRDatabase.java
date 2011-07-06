package com.lennardf1989.mywirelessredstone;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import com.lennardf1989.bukkitex.MyDatabase;
import com.lennardf1989.mywirelessredstone.models.MWRChannel;
import com.lennardf1989.mywirelessredstone.models.MWRReceiver;
import com.lennardf1989.mywirelessredstone.models.MWRSign;
import com.lennardf1989.mywirelessredstone.models.MWRTransmitter;

public class MWRDatabase extends MyDatabase {
    private List<MWRChannel> backupChannels = new ArrayList<MWRChannel>();
  
    public MWRDatabase(JavaPlugin javaPlugin) {
        super(javaPlugin);
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
    
    @Override
    public void beforeDropDatabase() {
        List<MWRChannel> oldChannels = MWRChannel.getAllChannels();
        
        for(MWRChannel channel : oldChannels) {
            MWRChannel newChannel = new MWRChannel(channel.getName());
            
            for(MWRTransmitter transmitter : channel.getTransmitters()) {
                try {
                    newChannel.getTransmitters().add(new MWRTransmitter(newChannel, transmitter.getLocation()));
                }
                catch(Exception ex) {
                    System.out.println(String.format("World %s doesn't exist, dropping record.", transmitter.getWorld()));
                }
            }
            
            for(MWRReceiver receiver : channel.getReceivers()) {
                try {
                    newChannel.getReceivers().add(new MWRReceiver(newChannel, receiver.getLocation()));
                }
                catch(Exception ex) {
                    System.out.println(String.format("World %s doesn't exist, dropping record.", receiver.getWorld()));
                }
            }
            
            backupChannels.add(newChannel);
        }
    }
    
    @Override
    public void afterCreateDatabase() {
        getDatabase().save(backupChannels);
    }
}

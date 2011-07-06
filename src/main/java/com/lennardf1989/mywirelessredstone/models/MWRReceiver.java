package com.lennardf1989.mywirelessredstone.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.bukkit.Location;

@Entity
@Table(name="mwr_receiver")
public class MWRReceiver extends MWRSign {
    @ManyToOne
    private MWRChannel channel;
    
    /**
     * Constructor intended for the Persistence Layer, not for public use.
     */
    @Deprecated
    public MWRReceiver() {}

    public MWRReceiver(MWRChannel channel, Location location) {
        super(location);
        
        this.channel = channel;
    }

    public MWRChannel getChannel() {
        return channel;
    }

    public void setChannel(MWRChannel channel) {
        this.channel = channel;
    }
}

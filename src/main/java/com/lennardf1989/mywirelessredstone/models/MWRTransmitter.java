package com.lennardf1989.mywirelessredstone.models;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="mwr_transmitter")
public class MWRTransmitter {
    @Id
    private int id;
    
    @ManyToOne
    private MWRChannel channel;
    
    @Embedded
    private MWRSign sign;
    
    public MWRTransmitter() {}
    
    public MWRTransmitter(MWRChannel channel, MWRSign sign) {
        this.channel = channel;
        this.sign = sign;
    }
    
    /**
     * Method intended for the Persistence Layer, not for public use.
     */
    @Deprecated
    public int getId() {
        return id;
    }

    /**
     * Method intended for the Persistence Layer, not for public use.
     */
    @Deprecated
    public void setId(int id) {
        this.id = id;
    }
    
    public MWRChannel getChannel() {
        return channel;
    }
    
    public void setChannel(MWRChannel channel) {
        this.channel = channel;
    }
    
    public MWRSign getSign() {
        return sign;
    }
    
    public void setSign(MWRSign sign) {
        this.sign = sign;
    }
}

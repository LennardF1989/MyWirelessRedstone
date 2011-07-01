package com.lennardf1989.mywirelessredstone.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PostRemove;
import javax.persistence.Table;

import com.lennardf1989.mywirelessredstone.MyWirelessRedstone;

@Entity
@Table(name="mwr_channel")
public class MWRChannel {
    @Id
    private int id;
    
    @Column(unique=true)
    private String name;
    
    @OneToMany(mappedBy="channel")
    private List<MWRTransmitter> transmitters;
    
    @OneToMany(mappedBy="channel")
    private List<MWRReceiver> receivers;
    
    /**
     * Constructor intended for the Persistence Layer, not for public use.
     */
    @Deprecated
    public MWRChannel() {
        this.transmitters = new ArrayList<MWRTransmitter>();
        this.receivers = new ArrayList<MWRReceiver>();
    }
    
    public MWRChannel(String name) {
        super();
        
        this.name = name;
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

    public String getName() {
        return name;
    }

    /**
     * Method intended for the Persistence Layer, not for public use.
     */
    @Deprecated 
    public void setName(String name) {
        this.name = name;
    }

    public List<MWRTransmitter> getTransmitters() {
        return transmitters;
    }
    
    /**
     * Method intended for the Persistence Layer, not for public use.
     */
    @Deprecated 
    public void setTransmitters(List<MWRTransmitter> transmitters) {
        this.transmitters = transmitters;
    }
    
    public List<MWRReceiver> getReceivers() {
        return receivers;
    }
    
    /**
     * Method intended for the Persistence Layer, not for public use.
     */
    @Deprecated 
    public void setReceivers(List<MWRReceiver> receivers) {
        this.receivers = receivers;
    }
    
    @PostRemove
    @SuppressWarnings("unused")
    private void removeOrphans() {
        System.out.println("Removing all orphans");
        
        List<MWRTransmitter> signs = MyWirelessRedstone.getInstance().getDatabase().find(MWRTransmitter.class).where().eq("channel", this).findList();
        MyWirelessRedstone.getInstance().getDatabase().delete(signs);
    }

    @Override
    public String toString() {
        return String.format("MWRChannel<id=%d,name=%s,transmitters=%d,receivers=%d>", id, name, transmitters.size(), receivers.size());
    }
}

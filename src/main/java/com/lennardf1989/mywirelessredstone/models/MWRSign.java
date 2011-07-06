package com.lennardf1989.mywirelessredstone.models;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.bukkit.Bukkit;
import org.bukkit.Location;

@MappedSuperclass
public abstract class MWRSign {
    @Id
    private int id;

    private int x;
    private int y;
    private int z;
    private String world;

    /**
     * Constructor intended for the Persistence Layer, not for public use.
     */
    @Deprecated
    public MWRSign() {}
    
    public MWRSign(Location location) {
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.world = location.getWorld().getName();
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

    public int getX() {
        return x;
    }

    /**
     * Method intended for the Persistence Layer, not for public use.
     */
    @Deprecated
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    /**
     * Method intended for the Persistence Layer, not for public use.
     */
    @Deprecated
    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    /**
     * Method intended for the Persistence Layer, not for public use.
     */
    @Deprecated
    public void setZ(int z) {
        this.z = z;
    }

    public String getWorld() {
        return world;
    }

    /**
     * Method intended for the Persistence Layer, not for public use.
     */
    @Deprecated
    public void setWorld(String world) {
        this.world = world;
    }
    
    public Location getLocation() {
        return new Location(Bukkit.getServer().getWorld(world), x, y, z);
    }

    @Override
    public String toString() {
        return String.format("MWRSign<id=%d,location=%d,%d,%d>", 0, x, y, z);
    }
}

package com.lennardf1989.mywirelessredstone.models;

import javax.persistence.Embeddable;

import org.bukkit.Location;

@Embeddable
public class MWRSign {
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

    @Override
    public String toString() {
        return String.format("MWRSign<id=%d,location=%d,%d,%d>", 0, x, y, z);
    }
}

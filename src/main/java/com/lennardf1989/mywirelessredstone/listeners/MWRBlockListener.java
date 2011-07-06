package com.lennardf1989.mywirelessredstone.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.PluginManager;

import com.lennardf1989.mywirelessredstone.MyWirelessRedstone;

public class MWRBlockListener extends BlockListener {
    public void registerEvents() {
        MyWirelessRedstone plugin = MyWirelessRedstone.getInstance();
        PluginManager pm = plugin.getServer().getPluginManager();

        pm.registerEvent(Type.BLOCK_PLACE, this, Priority.Normal, plugin);
        pm.registerEvent(Type.SIGN_CHANGE, this, Priority.Normal, plugin);
        pm.registerEvent(Type.REDSTONE_CHANGE, this, Priority.Normal, plugin);
        pm.registerEvent(Type.BLOCK_BREAK, this, Priority.Normal, plugin);
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        Material type = event.getBlock().getType();
        Bukkit.getServer().broadcastMessage(type.name());

        if(type == Material.TORCH)
        {
            //TODO: Torch position added
        }
    }

    @Override
    public void onSignChange(SignChangeEvent event) {
        //TODO: Sign position added

        /*
             If sign is transmitter then add transmitter
             Else if sign is receiver then add receiver

             Additional: If !channel exists then create channel

             Also, store orientation:
                Torches and Redstone Torches
                    0x1: Pointing south
                    0x2: Pointing north
                    0x3; Pointing west
                    0x4: Pointing east
                    0x5: Standing on the floor 

                Sign Posts
                    0x0: West
                    0x1: West-Northwest
                    0x2: Northwest
                    0x3: North-Northwest
                    0x4: North
                    0x5: North-Northeast
                    0x6: Northeast
                    0x7: East-Northeast
                    0x8: East
                    0x9: East-Southeast
                    0xA: Southeast
                    0xB: South-Southeast
                    0xC: South
                    0xD: South-Southwest
                    0xE: Southwest
                    0xF: West-Southwest 

                Wall Signs
                    0x2: Facing east
                    0x3: Facing west
                    0x4: Facing north
                    0x5: Facing south 
         */
    }

    @Override
    public void onBlockRedstoneChange(BlockRedstoneEvent event) {
        // Sign <-> Redstone Torch On || Torch <-> Redstone Torch Off
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        //TODO: (Redstone) Torch/Sign position removed
    }
}

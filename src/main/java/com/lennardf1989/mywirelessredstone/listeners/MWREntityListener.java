package com.lennardf1989.mywirelessredstone.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEventBlock;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.PreItemSpawnEvent;
import org.bukkit.plugin.PluginManager;

import com.lennardf1989.mywirelessredstone.MyWirelessRedstone;

public class MWREntityListener extends EntityListener {
    public void registerEvents() {
        MyWirelessRedstone plugin = MyWirelessRedstone.getInstance();
        PluginManager pm = plugin.getServer().getPluginManager();
        
        pm.registerEvent(Type.PRE_ITEM_SPAWN, this, Priority.Normal, plugin);
        pm.registerEvent(Type.ENTITY_EXPLODE, this, Priority.Normal, plugin);
    }
    
    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        for(EntityExplodeEventBlock explosionBlock : event.getBlocks())
        {
            Block block = explosionBlock.getBlock();
            Material type = block.getType();

            if( type == Material.REDSTONE_TORCH_ON || 
                type == Material.REDSTONE_TORCH_OFF || 
                type == Material.SIGN_POST ||
                type == Material.WALL_SIGN ||
                type == Material.TORCH)
            {
                //TODO: (Redstone) Torch/Sign position removed
            }
        }
    }
    
    @Override
    public void onPreItemSpawn(PreItemSpawnEvent event) {
        Material type = event.getItem().getType();
        Bukkit.getServer().broadcastMessage(type.name());

        if( type == Material.REDSTONE_TORCH_ON || 
            type == Material.REDSTONE_TORCH_OFF || 
            type == Material.SIGN ||
            type == Material.TORCH) 
        {
            //TODO: (Redstone) Torch/Sign position removed
        }
    }
}

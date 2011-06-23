package com.lennardf1989.mywirelessredstone;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.PluginManager;

public class MWRBlockListener extends BlockListener {
	private MyWirelessRedstone plugin;
	
	public MWRBlockListener(MyWirelessRedstone plugin) {
		this.plugin = plugin;
	}
	
	public void registerEvents() {
		PluginManager pm = plugin.getServer().getPluginManager();
		
		pm.registerEvent(Type.SIGN_CHANGE, this, Priority.Highest, plugin);
	}
	
	@Override
	public void onSignChange(SignChangeEvent event) {
		//Do nothing
	}
}

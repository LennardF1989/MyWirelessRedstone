package com.lennardf1989.mywirelessredstone;

import org.bukkit.plugin.java.JavaPlugin;

public class MyWirelessRedstone extends JavaPlugin {
	public void onEnable() {
		System.out.println(String.format("%s version %s is enabled!", getDescription().getName(), getDescription().getVersion()));
		
		new MWRBlockListener(this).registerEvents();
	}

	public void onDisable() {
		System.out.println(String.format("%s version %s is disabled!", getDescription().getName(), getDescription().getVersion()));
	}
}

package org.henrya.animatedmotd;

import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import org.henrya.pingapi.PingAPI;

public class AnimatedMOTD extends JavaPlugin {
	private YamlConfiguration config;
	
	public void onEnable() {
		this.getDataFolder().mkdirs();
		File configFile = new File(this.getDataFolder() + File.separator +  "config.yml");
		if(!configFile.exists()) {
			this.saveResource("config.yml", false);
		}
		this.config = YamlConfiguration.loadConfiguration(configFile);
		PingAPI.registerListener(new MyListener(this));
	}
	
	public YamlConfiguration getConfig() {
		return this.config;
	}
}
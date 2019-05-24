package org.henrya.pingtext;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import org.henrya.pingapi.PingAPI;

public class PingText extends JavaPlugin {
	private YamlConfiguration config;
	
	public void onEnable() {
		this.getDataFolder().mkdirs();
		try {
			File configFile = new File(this.getDataFolder() + File.separator +  "config.yml");
			this.config = YamlConfiguration.loadConfiguration(configFile);
			if(!configFile.exists()) {
				config.set("Enabled", true);
				config.set("Message", "&b&lPlayers&6: &f%ONLINE%&6/&f%MAX%");
				this.config.save(configFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		PingAPI.registerListener(new MyListener(this));
	}
	
	public YamlConfiguration getConfig() {
		return this.config;
	}
}
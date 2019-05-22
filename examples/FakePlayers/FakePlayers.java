package anderson.henry.fakeplayers;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import anderson.henry.pingapi.PingAPI;

public class FakePlayers extends JavaPlugin {
	private YamlConfiguration config;
	
	public void onEnable() {
		this.getDataFolder().mkdirs();
		try {
			File configFile = new File(this.getDataFolder() + File.separator +  "config.yml");
			this.config = YamlConfiguration.loadConfiguration(configFile);
			if(!configFile.exists()) {
				config.set("Enabled", true);
				config.set("Add Real Players", true);
				config.set("Online Players", 1000000);
				config.set("Max Players", 1234567);
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
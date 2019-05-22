package anderson.henry.fakeplayers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import anderson.henry.pingapi.PingEvent;
import anderson.henry.pingapi.PingListener;
import anderson.henry.pingapi.PingReply;

public class MyListener implements PingListener {
	private YamlConfiguration config;
	
	public MyListener(FakePlayers plugin) {
		this.config = plugin.getConfig();
	}

	@Override
	public void onPing(PingEvent event) {
		PingReply reply = event.getReply();
		if(config.getBoolean("Enabled")) {
			int onlineCount = (config.getBoolean("Add Real Players")) ? config.getInt("Online Players") + Bukkit.getOnlinePlayers().size() : config.getInt("Online Players");
			reply.setOnlinePlayers(onlineCount);
			reply.setMaxPlayers(config.getInt("Max Players"));
		}
	}
}

package org.henrya.pingtext;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.henrya.pingapi.PingEvent;
import org.henrya.pingapi.PingListener;
import org.henrya.pingapi.PingReply;

public class MyListener implements PingListener {
	private YamlConfiguration config;
	
	public MyListener(PingText plugin) {
		this.config = plugin.getConfig();
	}

	@Override
	public void onPing(PingEvent event) {
		PingReply reply = event.getReply();
		if(config.getBoolean("Enabled")) {
			String message = config.getString("Message");
			message = message.replaceAll("%ONLINE%", String.valueOf(Bukkit.getOnlinePlayers().size()));
			message = message.replaceAll("%MAX%", String.valueOf(Bukkit.getMaxPlayers()));
			reply.setProtocolVersion(-1);
			reply.setProtocolName(ChatColor.translateAlternateColorCodes('&', message));
		}
	}
}

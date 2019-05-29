package org.henrya.animatedmotd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.henrya.pingapi.PingEvent;
import org.henrya.pingapi.PingListener;
import org.henrya.pingapi.PingReply;

public class MyListener implements PingListener {
	private Plugin plugin;
	private YamlConfiguration config;
	
	public MyListener(AnimatedMOTD plugin) {
		this.plugin = plugin;
		this.config = plugin.getConfig();
	}

	@Override
	public void onPing(PingEvent event) {
		PingReply reply = event.getReply();
		reply.setProtocolVersion(47);
		if(config.getBoolean("Enabled")) {
			event.cancelPong(true);
			event.setCancelled(true);
			String defaultMOTD = config.getString("Default MOTD");
			reply.setMOTD(ChatColor.translateAlternateColorCodes('&', defaultMOTD));
			event.createNewPacket(reply).send();
			int ticks = config.getInt("Animation.Ticks");
			List<String> animation = config.getStringList("Animation.MOTDs");
			event.createNewPacket(reply).send();
			for(int i = 0; i < animation.size(); i++) {
				new MotdRunnable(event, animation.get(i)).runTaskLater(plugin, i * ticks);
	        }
			new PongRunnable(event).runTaskLater(plugin, (animation.size() * ticks));
		}
	}
}

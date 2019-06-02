package org.henrya.animatedmotd;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.CachedServerIcon;
import org.henrya.pingapi.PingEvent;
import org.henrya.pingapi.PingListener;
import org.henrya.pingapi.PingReply;

public class MyListener implements PingListener {
	private Plugin plugin;
	private YamlConfiguration config;
	private String defaultMOTD;
	private String defaultPlayerCount;
	private List<String> motds;
	private List<String> playerCounts;
	private List<String> icons;
	private int ticks;
	
	public MyListener(AnimatedMOTD plugin) {
		this.plugin = plugin;
		this.config = plugin.getConfig();
		this.defaultMOTD = this.replaceVars(config.getString("Default MOTD"));
		this.defaultPlayerCount = this.replaceVars(config.getString("Default PlayerCount"));
		this.motds = config.getStringList("MOTD.Animation");
		this.playerCounts = config.getStringList("PlayerCount.Animation");
		this.icons = config.getStringList("Icon.Animation");
		this.ticks = config.getInt("Ticks");
	}

	@Override
	public void onPing(PingEvent event) {
		try {
		PingReply reply = event.getReply();
		if(config.getBoolean("Enabled")) {
			event.cancelPong(true);
			event.setCancelled(true);
			if(config.getBoolean("MOTD.Enabled")) {
				reply.setMOTD(ChatColor.translateAlternateColorCodes('&', this.defaultMOTD));
			}
			if(config.getBoolean("PlayerCount.Enabled")) {
				reply.setProtocolVersion(-1);
				reply.setProtocolName(ChatColor.translateAlternateColorCodes('&', this.defaultPlayerCount));
			}
			event.createNewPacket(reply).send();
			
			String lastMotd = (motds.isEmpty()) ? this.replaceVars(motds.get(motds.size() - 1)) : defaultMOTD;
			String lastPlayerCount = (!playerCounts.isEmpty()) ? this.replaceVars(playerCounts.get(playerCounts.size() - 1)) : defaultPlayerCount;
			CachedServerIcon lastIcon = (!icons.isEmpty()) ? Bukkit.loadServerIcon(new File(this.plugin.getDataFolder() + icons.get(icons.size() - 1))) : Bukkit.getServerIcon();
			
			int largest = Collections.max(Arrays.asList(motds.size(), playerCounts.size(), icons.size()));
			event.createNewPacket(reply).send();
			for(int i = 0; i < largest; i++) {
				String motd = null;
				String playerCount = null;
				CachedServerIcon icon = Bukkit.getServerIcon();
				if(config.getBoolean("MOTD.Enabled")) {
					motd = (motds.size() > i) ? this.replaceVars(motds.get(i)) : lastMotd;
				}
				if(config.getBoolean("PlayerCount.Enabled")) {
					playerCount = (playerCounts.size() > i) ? this.replaceVars(playerCounts.get(i)) : lastPlayerCount;
				}
				if(config.getBoolean("Icon.Enabled")) {
					icon = (icons.size() > i) ? Bukkit.loadServerIcon(new File(this.plugin.getDataFolder() + icons.get(i))) : lastIcon;
				}
				new AnimationRunnable(event, motd, playerCount, icon).runTaskLater(plugin, i * ticks);
	        }
			new PongRunnable(event).runTaskLater(plugin, largest * ticks);
		}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private String replaceVars(String message) {
		return message.replaceAll("%ONLINE%", String.valueOf(Bukkit.getOnlinePlayers().size())).replaceAll("%MAX%", String.valueOf(Bukkit.getMaxPlayers()));
	}
}

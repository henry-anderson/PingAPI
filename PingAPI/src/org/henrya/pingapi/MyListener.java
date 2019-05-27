package org.henrya.pingapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class MyListener implements PingListener {
	private Plugin plugin;
	
	public MyListener(Plugin plugin) {
		this.plugin = plugin;
	}
	
    public void onPing(PingEvent event) {
        event.setCancelled(true);
        event.cancelPong(true);
        BukkitScheduler scheduler = Bukkit.getScheduler();
        for(int i = 0; i < 10; i++) {
            scheduler.runTaskLater(plugin, new MyRunnable(event, "MOTD " + (i + 1)), i * 10);
        }
    }
}

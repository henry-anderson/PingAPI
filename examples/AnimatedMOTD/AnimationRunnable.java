package org.henrya.animatedmotd;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.CachedServerIcon;
import org.henrya.pingapi.PingEvent;
import org.henrya.pingapi.ServerInfoPacket;

public class AnimationRunnable extends BukkitRunnable {
    private PingEvent event;
    private String motd;
    private String playerCount;
    private CachedServerIcon icon;
    
    public AnimationRunnable(PingEvent event, String motd, String playerCount, CachedServerIcon icon) {
        this.event = event;
        this.motd = motd;
        this.playerCount = playerCount;
        this.icon = icon;
    }
    
    @Override
    public void run() {
    	if(this.motd != null) {
    		this.event.getReply().setMOTD(ChatColor.translateAlternateColorCodes('&', this.motd));
    	}
    	if(this.playerCount != null) {
    		this.event.getReply().setProtocolVersion(-1);
        	this.event.getReply().setProtocolName(ChatColor.translateAlternateColorCodes('&', this.playerCount));
    	}
    	if(this.icon != null) {
    		this.event.getReply().setIcon(this.icon);
    	}
        ServerInfoPacket packet = event.createNewPacket(event.getReply());
        packet.send();
    }
}
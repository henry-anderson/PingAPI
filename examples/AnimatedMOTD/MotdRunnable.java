package org.henrya.animatedmotd;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.henrya.pingapi.PingEvent;
import org.henrya.pingapi.ServerInfoPacket;

public class MotdRunnable extends BukkitRunnable {
    private PingEvent event;
    private String motd;
    
    public MotdRunnable(PingEvent event, String motd) {
        this.event = event;
        this.motd = motd;
    }
    
    @Override
    public void run() {
        this.event.getReply().setMOTD(ChatColor.translateAlternateColorCodes('&', this.motd));
        ServerInfoPacket packet = event.createNewPacket(event.getReply());
        packet.send();
    }
}
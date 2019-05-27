package org.henrya.pingapi;

import org.bukkit.scheduler.BukkitRunnable;

public class MyRunnable extends BukkitRunnable {
    private PingEvent event;
    private String motd;
    
    public MyRunnable(PingEvent event, String motd) {
        this.event = event;
        this.motd = motd;
    }
    public void run() {
        this.event.getReply().setMOTD(this.motd);
	ServerInfoPacket packet = event.createNewPacket(event.getReply());
        packet.send();
    }
}
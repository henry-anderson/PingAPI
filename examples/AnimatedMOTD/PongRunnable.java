package org.henrya.animatedmotd;

import org.bukkit.scheduler.BukkitRunnable;
import org.henrya.pingapi.PingEvent;

public class PongRunnable extends BukkitRunnable {
    private PingEvent event;
    
    public PongRunnable(PingEvent event) {
        this.event = event;
    }
    
    @Override
    public void run() {
        this.event.sendPong();
    }
}
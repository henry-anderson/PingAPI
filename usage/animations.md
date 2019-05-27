## Animations

<img src="https://i.imgur.com/LAQCN0K.gif"></img>

Unfortunately this is no longer possible, but servers running 1.8 or earlier can still make use of this feature.

PingAPI makes it easy to send multiple PacketStatusOutServerInfo packets. When the 'onPing' method is finished it constructs a new PacketStatusOutServerInfo packet based on the properties in the PingReply object. If the PingEvent is cancelled (Use PingEvent#setCancelled(boolean)) it won't send the packet. Here is an example of cancelling the original packet, but creating and sending a different one which is redundant, but a good example.

```java
public class MyListener implements PingListener {
    public void onPing(PingEvent event) {
        ServerInfoPacket packet = event.createNewPacket(event.getReply());
        packet.send();
        event.setCancelled(true);
    }
}
```

The ServerInfoPacket is basically a simple PacketStatusOutServerInfo wrapper which creates and sends a new packet based on the information stored in the PingReply object.
It's worth noting that the 'onPing' method is not invoked on the main thread. That said you can easily create an animated MOTD, but after a few seconds the client will close the connection and you will no longer be able to send PacketStatusOutServerInfo packets.

Here is an example of an animated MOTD. First we create a BukkitRunnable subclass and pass our PingEvent instance and a String representing the MOTD to set.

```java
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
```

Then you need to create a PingListener. In order to send multiple ServerInfoPackets the pong packet must not be sent to the client. This is accomplished by invoking PingEvent#setCancelled(true). From there we schedule multiple delayed tasks with the subclass we created above.

```java
public class MyListener implements PingListener {
    public void onPing(PingEvent event) {
        event.setCancelled(true);
        event.cancelPong(true);
        Plugin plugin = MyPlugin.getPlugin();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        Plugin plugin = PingAPI.getPlugin();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        for(int i = 0; i < 10; i++) {
            scheduler.runTaskLater(plugin, new MyRunnable(event, "MOTD " + (i + 1)), i * 10);
        }
    }
}
```

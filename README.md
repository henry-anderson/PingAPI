# PingAPI

<img src="https://img.shields.io/badge/minecraft-1.14.1-important.svg"> <img src="https://img.shields.io/badge/spigot-1.7.8%20--%201.14.1-critical.svg"> <img src="https://img.shields.io/badge/release-v1.1.0-blue.svg"> <a href="https://github.com/henry-anderson/PingAPI/blob/master/LICENSE"><img src="https://img.shields.io/badge/license-MIT-blue.svg"></a> ![Spiget Downloads](https://img.shields.io/spiget/downloads/3829.svg?color=0042ad)

PingAPI gives Bukkit developers more control over how they reply to ping requests. You can download it <a href="http://www.spigotmc.org/resources/pingapi.3829/">here</a>.  

It has been a few years since I have worked on this project, but I decided to update it for newer versions of CraftBukkit and keep backwards compatibility. I will try to keep it maintained and up to date from now on. The most important change to note is that creating animations is no longer possible for servers running newer versions of CraftBukkit.

<img src="http://i.imgur.com/vIWvOUv.gif"></img>
## Features
- Modify the online player count and the max players
- Change the player sample (list of players shown when hovering over the player count)
- Fake the protocol name and version to display text instead of the player count
- Hide the player count
- Create server list animations (1.8 or before)

## Javadocs
<a href="http://henry-anderson.github.io/PingAPI/">The Javadocs can be found here</a>

## Usage
### Creating a basic listener

To use PingAPI to its full potential you have to create a Listener. This is done similar to registering a Bukkit listener. Before we actually register it we need to create it. You can either create a new class that implements 'PingListener,' or just an anonymous inner class. Here is a simple example of a PingListener.

```java
public class MyListener implements PingListener {
    public void onPing(PingEvent event) {
        event.getReply().setMOTD("This is an MOTD");
    }
}
```

Now that we have created the PingListener we need to actually register it. You can do so by invoking the static method 'PingAPI.registerListener(PingListener).' Here is an example using an anonymous inner class.

```java
public class MyPlugin extends JavaPlugin {
    public void onEnable() {
        PingAPI.registerListener(new PingListener() {
            public void onPing(PingEvent event) {
                event.getReply().setMOTD("This is an MOTD");
            }
        }
    }
}
```

### Changing the player count

<img src="http://i.imgur.com/ZsavWWd.png"></img>

Bukkit does not support changing the online player count with the ServerListPingEvent for obvious reasons, but PingAPI does. You can easily do this by invoking PingReply#setOnlinePlayers(int). Here is an example.

```java
public class MyListener implements PingListener {
    public void onPing(PingEvent event) {
        event.getReply().setOnlinePlayers(5000);
    }
}
```

### Changing the player count to text

<img src="http://i.imgur.com/JwaX1Im.png"></img>

The client sends a ping packet to the server and it replies with all your information including the server's protocol version and compares it to that of the client. If the protocol version's do not match it displays a red message in replacement of the player count such as "Spigot 1.8" We can use this and send a fake protocol version such as -1 and change the default protocol name message to a new one. Here is an example.

```java
public class MyListener implements PingListener {
    public void onPing(PingEvent event) {
        PingReply reply = event.getReply();
        reply.setProtocolVersion(-1);
        reply.setProtocolName(ChatColor.GREEN + "This is a message...");
    }
}
```

### Changing the player sample (hover message)

<img src="http://i.imgur.com/m7TmDgs.png"></img>

As you probably know, when you hover over the player count it displays a list of a few random player's names. This can easily be changed to your own message with PingAPI. Here is an example.

```java
public class MyListener implements PingListener {
    public void onPing(PingEvent event) {
        List<String> sample = new ArrayList<String>();
        sample.add(ChatColor.BLUE + "GenericCraft");
        sample.add("-------------------------");
        sample.add(ChatColor.GOLD + "New game thing available");
        sample.add(ChatColor.AQUA + "Join for free stuff now!");
        event.getReply().setPlayerSample(sample);
    }
}
```

### Animations

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
It's worth noting that the 'onPing' method is not invoked on the main thread. That said you can easily create an animated motd, but after a few seconds the client will close the connection and you will no longer be able to send PacketStatusOutServerInfo packets.

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

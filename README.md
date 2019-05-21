<img src="http://i.imgur.com/vIWvOUv.gif"></img>

PingAPI gives you more control over how you reply to ping requests. You can download it <a href="http://www.spigotmc.org/resources/pingapi.3829/">here</a>

# Features
- Modify the online player count and the max players
- Change the player sample (list of players shown when hovering over the player count)
- Fake the protocol name and version to display text instead of the player count
- Hide the player count
- Create server list animations (1.8 or before)

# Tutorials
<h3>Creating a basic listener</h3>

To use PingAPI to its full potential you have to create a Listener. This is done similar to registering a Bukkit listener. Before we actually register it we need to create it. You can either create a new class that implements 'PingListener,' or just an anonymous inner class. Here is a simple example of a PingListener.

<pre>
public class MyListener implements PingListener {
    public void onPing(PingEvent event) {
        event.getReply().setMOTD("This is an MOTD");
    }
}
</pre>

Now that we have created the PingListener we need to actually register it. You can do so by invoking the static method 'PingAPI.registerListener(PingListener).' Here is an example using an anonymous inner class.

<pre>
public class MyPlugin extends JavaPlugin {
    public void onEnable() {
        PingAPI.registerListener(new PingListener() {
            public void onPing(PingEvent event) {
                event.getReply().setMOTD("This is an MOTD");
            }
        }
    }
}
</pre>

<h3>Changing the player count</h3>

<img src="http://i.imgur.com/ZsavWWd.png"></img>

Bukkit does not support changing the online player count with the ServerListPingEvent for obvious reasons, but PingAPI does. You can easily do this by invoking PingReply#setOnlinePlayers(int). Here is an example.

<pre>
public class MyListener implements PingListener {
    public void onPing(PingEvent event) {
        event.getReply().setOnlinePlayers(5000);
    }
}
</pre>

<h3>Changing the player count to text</h3>

<img src="http://i.imgur.com/JwaX1Im.png"></img>

The client sends a ping packet to the server and it replies with all your information including the server's protocol version and compares it to that of the client. If the protocol version's do not match it displays a red message in replacement of the player count such as "Spigot 1.8" We can use this and send a fake protocol version such as -1 and change the default protocol name message to a new one. Here is an example.

<pre>
public class MyListener implements PingListener {
    public void onPing(PingEvent event) {
        PingReply reply = event.getReply();
        reply.setProtocolVersion(-1);
        reply.setProtocolName(ChatColor.GREEN + "This is a message...");
    }
}
</pre>

<h3>Changing the player sample (hover message)</h3>

<img src="http://i.imgur.com/m7TmDgs.png"></img>

As you probably know, when you hover over the player count it displays a list of a few random player's names. This can easily be changed to your own message with PingAPI. Here is an example.

<pre>
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
</pre>

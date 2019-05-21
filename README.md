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

To use PingAPI to its full potential you have to create a Listener. This is done similar to registering a Bukkit listener. Before we actually register it we need to create it. You can either create a new class that implements 'PingListener,' or just an anonymous inner class. Here is a simple example of a PingListener

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
Bukkit does not support changing the online player count with the ServerListPingEvent for obvious reasons, but PingAPI does. You can easily do this by invoking PingReply#setOnlinePlayers(int). Here is an example:

<pre>
public class MyListener implements PingListener {
    public void onPing(PingEvent event) {
        event.getReply().setOnlinePlayers(5000);
    }
}
</pre>

This is the output.

<img src="http://i.imgur.com/ZsavWWd.png"></img>

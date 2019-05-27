# Creating a basic listener

To use PingAPI to its full potential you have to create a Listener. This is done similar to creating a Bukkit listener. You can either create a new class that implements PingListener, or just an anonymous inner class. Here is a simple example of a PingListener implementation.

```java
public class MyListener implements PingListener {
    public void onPing(PingEvent event) {
        event.getReply().setMOTD("This is an MOTD");
    }
}
```

Now that we have created the listener we need to actually register it. You can do so by invoking the static method "PingAPI.registerListener(PingListener)." Here is an example using an anonymous inner class.

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

Your listener's method will be invoked right before a PacketStatusOutServerInfo packet is sent back to the client. This happens when the server is pinged.

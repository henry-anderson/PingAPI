# Changing the player count

<img src="http://i.imgur.com/ZsavWWd.png"></img>

Bukkit does not support changing the online player count with the ServerListPingEvent, but PingAPI does. You can easily do this by invoking PingReply#setOnlinePlayers(int). Here is an example.

```java
public class MyListener implements PingListener {
    public void onPing(PingEvent event) {
        event.getReply().setOnlinePlayers(5000);
    }
}
```
As displayed in the image above this will change the player count that shows up on the client's server list to 5000.

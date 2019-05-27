# Changing the player count to text

<img src="http://i.imgur.com/JwaX1Im.png"></img>

The client sends a ping packet to the server and it replies with all your information including the server's protocol version and compares it to that of the client. If the protocol version's do not match it displays a red message in replacement of the player count such as "Spigot 1.8" We can use this and send a fake protocol version such as -1 and change the default protocol name to a new one. Here is an example.

```java
public class MyListener implements PingListener {
    public void onPing(PingEvent event) {
        PingReply reply = event.getReply();
        reply.setProtocolVersion(-1);
        reply.setProtocolName(ChatColor.GREEN + "This is a message...");
    }
}
```

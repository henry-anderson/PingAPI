## Changing the player count to text

[<img src="https://i.imgur.com/UE7RSvS.png">]()

The client sends a ping packet to the server and it replies with all of the server's information including the server's protocol version and compares it to that of the client. If the protocol version's do not match it displays a red message in replacement of the player count such as "Spigot 1.8."

We can use this logic to display a message instead. If we send a fake protocol version such as -1 to the client it will think that the server and client are running different versions of Minecraft and display the protocol message instead of the player count. From there we can change the default protocol name to a new one. Here is an example.

```java
public class MyListener implements PingListener {
    public void onPing(PingEvent event) {
        PingReply reply = event.getReply();
        reply.setProtocolVersion(-1);
        reply.setProtocolName(ChatColor.GREEN + "This is a message...");
    }
}
```

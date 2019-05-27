# Changing the player sample

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

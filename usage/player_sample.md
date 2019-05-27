# Changing the player sample

<img src="http://i.imgur.com/m7TmDgs.png"></img>

When you hover over the player count it displays a list of a few random player's names. This list contains a maximum of 10 names. Unlike Bukkit, PingAPI lets you modify this list and allows you to change it to any text instead of just player names.

Here is an example.

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

It is also worth noting that there is a method in the PingReply class that will prevent this list from being sent all together.

```java
public class MyListener implements PingListener {
    public void onPing(PingEvent event) {
        event.getReply().hidePlayerSample();
    }
}
```


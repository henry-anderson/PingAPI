# PingAPI
If you aren't interested in using PingAPI this document will go over how I created it. The API itself is simple and easy to use, but what happens behind the scenes is more complicated.

## NMS
You can tell by looking above that there are a number of different packages with version numbers. I had to dig into NMS itself to listen for and modify outgoing packets. NMS classes are version dependent because the package changes for each major update. [This](https://bukkit.org/threads/safeguarding-against-unchecked-and-potentially-damaging-plugins.116749/) thread explains why that was decided upon.

## Abstraction
I made use of abstraction so that I could handle the changes from each version of CraftBukkit accordingly. NMS classes are obfuscated so there are small changes that occur from version to version such as the method "a()" being changed to "c()." That is the reason I included a package for every major CraftBukkit update. I used reflection to instantiate the appropriate classes for the version of CraftBukkit that is being run and downcasted to the abstract class I created for the API.

## Packet Sniffing
In order to listen for outgoing packets I created a class called PingInjector. This class gets the list of NetworkManager instances and injects a subclass of ChannelDuplexHandler I created into each NetworkManager's pipeline. In my ChannelDuplexHandler subclass I overrode the write() method which sends the packet to the client. This way I was able to check whether the packet being sent was a PacketStatusOutServerInfo packet and modify the data appropriately

```java
public class DuplexHandler extends ChannelDuplexHandler {
	
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if(msg instanceof PacketStatusOutServerInfo) {
            //do something	
        }
        super.write(ctx, msg, promise);
    }
}
```
```java
public class PingInjector implements Listener {
    private MinecraftServer server;
    private List<?> networkManagers;
	
    @EventHandler
    public void serverListPing(ServerListPingEvent event) {
        this.injectOpenConnections();
    }
  
    public PingInjector() {
        try {
            CraftServer craftserver = (CraftServer) Bukkit.getServer();
            Field console = craftserver.getClass().getDeclaredField("console");
            console.setAccessible(true);
            this.server = (MinecraftServer) console.get(craftserver);
            ServerConnection conn = this.server.am();
            networkManagers = Collections.synchronizedList((List<?>) this.getNetworkManagerList(conn));
        } catch(IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
	
    public void injectOpenConnections() {
        try {
            Field field = ReflectUtils.getFirstFieldByType(NetworkManager.class, Channel.class);
            field.setAccessible(true);
            for(Object manager : networkManagers) {
                Channel channel = (Channel) field.get(manager);
                if(channel.pipeline().context("ping_handler") == null && (channel.pipeline().context("packet_handler") != null)) {
                channel.pipeline().addBefore("packet_handler", "ping_handler", new DuplexHandler());
            }
        }
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }
    }
	
    public Object getNetworkManagerList(ServerConnection conn) {
        try {
            for(Method method : conn.getClass().getDeclaredMethods()) {
                method.setAccessible(true);
                if(method.getReturnType() == List.class) {
                    Object object = method.invoke(null, conn);
                    return object;
                }
            }
        } catch(IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
```
The PingInjector class references a static method in a ReflectUtils class I made. Since NMS is obfuscated, field names change from time to time so what this method does is iterate through each field and returns the first field of a certain class type. I used this method to grab the list of NetworkManager instances stored in the CraftServer class.

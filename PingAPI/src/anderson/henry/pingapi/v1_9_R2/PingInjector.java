package anderson.henry.pingapi.v1_9_R2;

import io.netty.channel.Channel;
import net.minecraft.server.v1_9_R2.MinecraftServer;
import net.minecraft.server.v1_9_R2.NetworkManager;
import net.minecraft.server.v1_9_R2.ServerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R2.CraftServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;

import anderson.henry.pingapi.reflect.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class PingInjector implements Listener {
	private MinecraftServer server;
	private List<?> networkManagers;
	
	public PingInjector() {
		try {
			CraftServer craftserver = (CraftServer) Bukkit.getServer();
			Field console = craftserver.getClass().getDeclaredField("console");
			console.setAccessible(true);
			this.server = (MinecraftServer) console.get(craftserver);
			ServerConnection conn = this.server.am();
			networkManagers = Collections.synchronizedList((List<?>) this.getNetworkManagerList(conn));
		} catch(Exception e) {
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
		} catch(Exception e) {
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
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@EventHandler
	public void serverListPing(ServerListPingEvent event) {
		this.injectOpenConnections();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		this.injectOpenConnections();
	}
}

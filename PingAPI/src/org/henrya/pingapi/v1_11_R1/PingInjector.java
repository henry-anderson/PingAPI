package org.henrya.pingapi.v1_11_R1;

import io.netty.channel.Channel;
import net.minecraft.server.v1_11_R1.MinecraftServer;
import net.minecraft.server.v1_11_R1.NetworkManager;
import net.minecraft.server.v1_11_R1.ServerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_11_R1.CraftServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.henrya.pingapi.reflect.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A class that injects out packet listener into open NetworkManagers
 * @author Henry Anderson
 */
public class PingInjector implements Listener {
	private MinecraftServer server;
	private List<?> networkManagers;
	
	/**
	 * Constructs a new PingInjector and gets the list of open NetworkManager instances
	 */
	public PingInjector() {
		try {
			CraftServer craftserver = (CraftServer) Bukkit.getServer();
			Field console = craftserver.getClass().getDeclaredField("console");
			console.setAccessible(true);
			this.server = (MinecraftServer) console.get(craftserver);
			ServerConnection conn = this.server.an();
			networkManagers = Collections.synchronizedList((List<?>) this.getNetworkManagerList(conn));
		} catch(IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Iterates through every open NetworkManager and adds my ChannelDuplexHandler subclass into the pipeline
	 * This allows you to listen for outgoing packets and modify them before they are sent
	 * 
	 * The List of NetworkManager instances is converted to an array to avoid ConcurrentModificationExceptions
	 * NullPointerExceptions, IllegalArgumentExceptions, and NoSuchElementException only occur if there is a massive amount of ping requests being sent to the server.
	 * NullPointerExceptions are thrown when the pipeline has yet to be created. 
	 * Since ping responses are handled on separate threads IllegalArgumentExceptions are thrown when this method is invoked at the same time on two different threads
	 * This means the null check will be passed and this method will attempt to create a duplicate handler which throws this exception
	 * NoSuchElementExceptions have a similar cause. They are caused when the "packet_handler" has yet to be added.
	 * The best solution I could find is simply ignoring these exceptions
	 */
	public void injectOpenConnections() {
		try {
			Field field = ReflectUtils.getFirstFieldByType(NetworkManager.class, Channel.class);
			field.setAccessible(true);
			for(Object manager : networkManagers.toArray()) {
				Channel channel = (Channel) field.get(manager);
				if(channel.pipeline().context("pingapi_handler") == null && (channel.pipeline().context("packet_handler") != null)) {
					channel.pipeline().addBefore("packet_handler", "pingapi_handler", new DuplexHandler());
				}
			}
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		} catch(NullPointerException | IllegalArgumentException | NoSuchElementException ignored) {}
	}
	
	/**
	 * Returns the list of open NetworkManager instances
	 * @param conn The ServerConnection instance
	 * @return A List of NetworkManager instances downcasted to an Object
	 */
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
	
	/**
	 * Injects a DuplexHandler into each NetworkManager's pipeline when the server receives a ping packet
	 * @param event The event
	 */
	@EventHandler
	public void serverListPing(ServerListPingEvent event) {
		this.injectOpenConnections();
	}
}

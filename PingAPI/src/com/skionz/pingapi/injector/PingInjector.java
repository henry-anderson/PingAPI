package com.skionz.pingapi.injector;

import io.netty.channel.Channel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.server.v1_8_R1.MinecraftServer;
import net.minecraft.server.v1_8_R1.NetworkManager;
import net.minecraft.server.v1_8_R1.ServerConnection;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.skionz.pingapi.reflect.ReflectUtils;

public class PingInjector {
	private JavaPlugin plugin;
	private MinecraftServer server;
	private List<?> networkManagers;
	private List<NetworkManager> injectedNetworkManagers;
	
	public PingInjector(JavaPlugin plugin) {
		try {
			this.plugin = plugin;
			CraftServer craftserver = (CraftServer) Bukkit.getServer();
			Field console = craftserver.getClass().getDeclaredField("console");
			console.setAccessible(true);
			this.server = (MinecraftServer) console.get(craftserver);
			ServerConnection conn = this.server.getServerConnection();
			networkManagers = Collections.synchronizedList((List<?>) this.getNetworkManagerList(conn));
			injectedNetworkManagers = Collections.synchronizedList(new ArrayList<NetworkManager>());
			Bukkit.getPluginManager().registerEvents(new Listener() {
				@EventHandler
				public void onPing(ServerListPingEvent event) {
					injectOpenConnections();
				}
			}, this.plugin);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void uninjectOpenConnections() {
		try {
			Field field = NetworkManager.class.getDeclaredField("i");
			field.setAccessible(true);
			for(NetworkManager manager : injectedNetworkManagers) {
				Channel channel = (Channel) field.get(manager);
				if(channel.pipeline().context("ping_handler") != null) {
					channel.pipeline().remove("ping_handler");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void injectOpenConnections() {
		try {
			Field field = NetworkManager.class.getDeclaredField("i");
			field.setAccessible(true);
			for(Object manager : networkManagers) {
				if(!injectedNetworkManagers.contains(manager)) {
					injectedNetworkManagers.add((NetworkManager) manager);
					Channel channel = (Channel) ReflectUtils.getFirstFieldByType(NetworkManager.class, Channel.class).get(manager);
					if(channel.pipeline().context("packet_handler") != null) {
						channel.pipeline().addBefore("packet_handler", "ping_handler", new DuplexHandler());
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private Object getNetworkManagerList(ServerConnection conn) {
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
	
	public List<NetworkManager> getInjectedNetworkManagers() {
		return this.injectedNetworkManagers;
	}
}

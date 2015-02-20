package com.skionz.pingapi;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;

public class PingAPI extends JavaPlugin {
	private static List<PingListener> listeners;

	public void onEnable() {
		try {
			PingAPI.listeners = new ArrayList<PingListener>();
			String name = Bukkit.getServer().getClass().getPackage().getName();
	        String version = name.substring(name.lastIndexOf('.') + 1);
	        Class<?> injector = Class.forName("com.skionz.pingapi." + version + ".PingInjector");
	        Bukkit.getPluginManager().registerEvents((Listener) injector.newInstance(), this);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void registerListener(PingListener listener) {
		listeners.add(listener);
	}
	
	public static List<PingListener> getListeners() {
		return listeners;
	}
}
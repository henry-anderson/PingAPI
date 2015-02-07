package com.skionz.pingapi;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;

import com.skionz.pingapi.injector.PingInjector;

public class PingAPI extends JavaPlugin {
	private static List<PingListener> listeners;
	private PingInjector injector;
	
	public void onDisable() {
		this.injector.uninjectOpenConnections();
	}
	
	public void onEnable() {
		PingAPI.listeners = new ArrayList<PingListener>();
		this.injector = new PingInjector(this);
	}
	
	public static void registerListener(PingListener listener) {
		listeners.add(listener);
	}
	
	public static List<PingListener> getListeners() {
		return listeners;
	}
}
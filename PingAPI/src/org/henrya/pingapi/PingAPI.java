package org.henrya.pingapi;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
        	Class<?> injector = Class.forName("org.henrya.pingapi." + version + ".PingInjector");
        	Bukkit.getPluginManager().registerEvents((Listener) injector.newInstance(), this);
        	this.getLogger().log(Level.INFO, "Successfully hooked into " + Bukkit.getServer().getName() + " " + version);
		} catch(ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
		    	this.getLogger().log(Level.SEVERE, "Non compatible server version!", e);
		    	Bukkit.getPluginManager().disablePlugin(this);
		}
		PingAPI.registerListener(new MyListener(this));
	}
	
	/**
	 * Registers a new PingListener instance
	 * @param listener The PingListener implementation
	 */
	public static void registerListener(PingListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Returns a List of all PingListener's that have been registered
	 * @return Return's a List of PingListener
	 */
	public static List<PingListener> getListeners() {
		return listeners;
	}
}
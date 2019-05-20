package anderson.henry.pingapi;

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
        	        Class<?> injector = Class.forName("anderson.henry.pingapi." + version + ".PingInjector");
        	        Bukkit.getPluginManager().registerEvents((Listener) injector.newInstance(), this);
        	        this.getLogger().log(Level.INFO, "Successfully hooked into " + Bukkit.getServer().getName() + " " + version);
		} catch(Exception e) {
		    	this.getLogger().log(Level.SEVERE, "No compatible server version!", e);
		    	Bukkit.getPluginManager().disablePlugin(this);
		}
	}
	
	public static void registerListener(PingListener listener) {
		listeners.add(listener);
	}
	
	public static List<PingListener> getListeners() {
		return listeners;
	}
}
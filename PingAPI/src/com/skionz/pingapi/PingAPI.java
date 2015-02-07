package com.skionz.pingapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;
import com.skionz.pingapi.injector.PingInjector;

public class PingAPI extends JavaPlugin implements PingListener {
	private static List<PingListener> listeners;
	private PingInjector injector;
	private List<String> motds = new ArrayList<String>();
	private GameProfile[] hover = new GameProfile[1];
	
	@Override
	public void onDisable() {
		this.injector.uninjectAll();
	}
	
	@Override
	public void onEnable() {
		PingAPI.listeners = new ArrayList<PingListener>();
		this.injector = new PingInjector(this);
		PingAPI.registerListener(this);
		this.motds.add(ChatColor.AQUA + "" + ChatColor.BOLD + "P");
		this.motds.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Pl");
		this.motds.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Pla");
		this.motds.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Play");
		this.motds.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Playe");
		this.motds.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Player");
		this.motds.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Players");
		this.motds.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Players:");
		this.motds.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Players: ");
		this.motds.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Players: " + ChatColor.WHITE + "5");
		this.motds.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Players: " + ChatColor.WHITE + "50");
		this.motds.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Players: " + ChatColor.WHITE + "50" + ChatColor.GOLD + "/");
		this.motds.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Players: " + ChatColor.WHITE + "50" + ChatColor.GOLD + "/" + ChatColor.WHITE + "5");
		this.motds.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Players: " + ChatColor.WHITE + "50" + ChatColor.GOLD + "/" + ChatColor.WHITE + "50");
		this.hover[0] = new GameProfile(UUID.randomUUID(), ChatColor.GOLD + "StuffCraft1");
	}
	
	public static void registerListener(PingListener listener) {
		listeners.add(listener);
	}
	
	public static List<PingListener> getListeners() {
		return listeners;
	}

	@Override
	public void onPing(PingReply ping) {
		ping.setOnlinePlayers(53);
		ping.setMaxPlayers(100);
		
	}
}
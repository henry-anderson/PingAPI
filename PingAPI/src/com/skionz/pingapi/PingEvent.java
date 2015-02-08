package com.skionz.pingapi;

import java.lang.reflect.Constructor;

import org.bukkit.Bukkit;

public class PingEvent {
	private PingReply reply;
	private boolean cancel;
	
	public PingEvent(PingReply reply) {
		this.reply = reply;
	}
	
	public PingReply getReply() {
		return this.reply;
	}
	
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
	
	public boolean isCancelled() {
		return this.cancel;
	}
	
	public ServerInfoPacket createNewPacket() {
		try {
			String name = Bukkit.getServer().getClass().getPackage().getName();
	        String version = name.substring(name.lastIndexOf('.') + 1);
	        Class<?> packet = Class.forName("com.skionz.pingapi." + version + ".ServerInfoPacketHandler");
	        Constructor<?> constructor = packet.getDeclaredConstructor(this.reply.getClass());
			return (ServerInfoPacket) constructor.newInstance(this.reply);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
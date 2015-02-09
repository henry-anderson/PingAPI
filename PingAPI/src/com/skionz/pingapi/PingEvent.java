package com.skionz.pingapi;

import java.lang.reflect.Constructor;

import org.bukkit.Bukkit;

public class PingEvent {
	private PingReply reply;
	private boolean cancelEvent;
	private boolean cancelPong;
	
	public PingEvent(PingReply reply) {
		this.reply = reply;
	}
	
	public PingReply getReply() {
		return this.reply;
	}
	
	public void setCancelled(boolean cancel) {
		this.cancelEvent = cancel;
	}
	
	public void cancelPong(boolean cancel) {
		this.cancelPong = cancel;
	}
	
	public boolean isCancelled() {
		return this.cancelEvent;
	}
	
	public boolean isPongCancelled() {
		return this.cancelPong;
	}
	
	public ServerInfoPacket createNewPacket(PingReply reply) {
		try {
			String name = Bukkit.getServer().getClass().getPackage().getName();
	        String version = name.substring(name.lastIndexOf('.') + 1);
	        Class<?> packet = Class.forName("com.skionz.pingapi." + version + ".ServerInfoPacketHandler");
	        Constructor<?> constructor = packet.getDeclaredConstructor(reply.getClass());
			return (ServerInfoPacket) constructor.newInstance(reply);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
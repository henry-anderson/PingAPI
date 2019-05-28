package org.henrya.pingapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;

/**
 * An event that is called when the server receives a ping
 * @author Henry Anderson
 */
public class PingEvent {
	private PingReply reply;
	private boolean cancelEvent;
	private boolean cancelPong;
	
	/**
	 * Created a new PingEvent instance
	 * @param reply The reply
	 */
	public PingEvent(PingReply reply) {
		this.reply = reply;
	}
	
	/**
	 * Returns the PingReply instance
	 * @return The reply
	 */
	public PingReply getReply() {
		return this.reply;
	}
	
	/**
	 * Cancels the event
	 * @param cancel A boolean
	 */
	public void setCancelled(boolean cancel) {
		this.cancelEvent = cancel;
	}
	
	/**
	 * Prevents the PacketStatusOutPong packet from being sent
	 * The PacketStatusOutPong packet closes the connection and must be cancelled to create animations
	 * @param cancel A boolean
	 */
	public void cancelPong(boolean cancel) {
		this.cancelPong = cancel;
	}
	
	/**
	 * Returns whether the event has been cancelled
	 * @return A boolean
	 */
	public boolean isCancelled() {
		return this.cancelEvent;
	}
	
	/**
	 * Returns whether the PacketStatusOutPong packet has been cancelled
	 * @return A boolean
	 */
	public boolean isPongCancelled() {
		return this.cancelPong;
	}
	
	/**
	 * Creates a new ServerInfoPacket from a PingReply instance
	 * @param reply The reply
	 * @return A ServerInfoPacket instance
	 */
	public ServerInfoPacket createNewPacket(PingReply reply) {
		try {
			String name = Bukkit.getServer().getClass().getPackage().getName();
	        String version = name.substring(name.lastIndexOf('.') + 1);
	        Class<?> packet = Class.forName("org.henrya.pingapi." + version + ".ServerInfoPacketHandler");
	        Constructor<?> constructor = packet.getDeclaredConstructor(reply.getClass());
			return (ServerInfoPacket) constructor.newInstance(reply);
		} catch(ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Sends a pong packet to the client which closes the connection
	 * The pong packet changes the icon to the right of the player count from red to green
	 */
	public void sendPong() {
		try {
			String name = Bukkit.getServer().getClass().getPackage().getName();
	        String version = name.substring(name.lastIndexOf('.') + 1);
	        Class<?> packet = Class.forName("org.henrya.pingapi." + version + ".PongPacketHandler");
	        Constructor<?> constructor = packet.getDeclaredConstructor(this.getClass());
			PongPacket pong = (PongPacket) constructor.newInstance(this);
			pong.send();
		} catch(ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
package org.henrya.pingapi;

/**
 * An interface for creating PacketStatusOutServerInfo from PingReply instances 
 * @author Henry Anderson
 */
public abstract class ServerInfoPacket {
	private PingReply reply;
	
	public ServerInfoPacket(PingReply reply) {
		this.reply = reply;
	}
	
	/**
	 * Sends the packet to the server 
	 */
	public abstract void send();
	
	/**
	 * Returns the PingReply instance
	 * @return The reply
	 */
	public PingReply getReply() {
		return this.reply;
	}
	
	/**
	 * Sets the PingReply instance
	 * @param reply The reply
	 */
	public void setReply(PingReply reply) {
		this.reply = reply;
	}
}

package org.henrya.pingapi;

/**
 * An abstract class for creating PacketStatusOutPong packets from PingEvent instances 
 * @author Henry Anderson
 */
public abstract class PongPacket {
	private PingEvent event;
	
	public PongPacket(PingEvent event) {
		this.event = event;
	}
	
	/**
	 * Sends the packet to the server 
	 */
	public abstract void send();
	
	/**
	 * Returns the PingEvent instance
	 * @return The reply
	 */
	public PingEvent getEvent() {
		return this.event;
	}
	
	/**
	 * Sets the PingEvent instance
	 * @param reply The reply
	 */
	public void setEvent(PingEvent event) {
		this.event = event;
	}
}

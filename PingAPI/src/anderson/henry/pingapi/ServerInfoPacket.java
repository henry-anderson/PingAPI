package anderson.henry.pingapi;

/**
 * An interface for creating PacketStatusOutServerInfo from PingReply instances 
 * @author Henry Anderson
 */
public interface ServerInfoPacket {
	
	/**
	 * Sends the packet to the server 
	 */
	public void send();
	
	/**
	 * Returns the PingReply instance
	 * @return The reply
	 */
	public PingReply getPingReply();
	
	/**
	 * Sets the PingReply instance
	 * @param reply The reply
	 */
	public void setPingReply(PingReply reply);
}

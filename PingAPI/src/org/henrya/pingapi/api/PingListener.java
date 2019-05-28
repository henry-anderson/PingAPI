package org.henrya.pingapi.api;

/**
 * An interface for creating PingAPI listeners
 * @author Henry Anderson
 */
public interface PingListener {
	
	/**
	 * A method that is invoked when a client pings the server
	 * @param event The PingEvent
	 */
	public void onPing(PingEvent event);
}
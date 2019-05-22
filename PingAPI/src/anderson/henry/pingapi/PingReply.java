package anderson.henry.pingapi;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.util.CachedServerIcon;

/**
 * A class that defines the data that will be sent to the client in a PacketStatusOutServerInfo packet
 * @author Henry Anderson
 */
public class PingReply {
	private String motd;
	private int onlinePlayers;
	private int maxPlayers;
	private int protocolVersion;
	private String protocolName;
	private List<String> playerSample;
	private boolean hidePlayerSample = false;
	private CachedServerIcon icon = Bukkit.getServerIcon();
	
	/**
	 * Creates a class to store data that will be sent to the client in a PacketStatusOutServerInfo packet
	 * @param motd The MOTD
	 * @param onlinePlayers The amount of online players
	 * @param maxPlayers The maximum amount of players
	 * @param protocolVersion The protocol version
	 * @param protocolName The name of the protocol
	 * @param playerSample A list of player names
	 */
	public PingReply(String motd, int onlinePlayers, int maxPlayers, int protocolVersion, String protocolName, List<String> playerSample) {
		this.motd = motd;
		this.onlinePlayers = onlinePlayers;
		this.maxPlayers = maxPlayers;
		this.protocolVersion = protocolVersion;
		this.protocolName = protocolName;
		this.playerSample = playerSample;
	}
	
	/**
	 * Returns the amount of online players to be sent to the client
	 * @return An integer representing the online players
	 */
	public int getOnlinePlayers() {
		return this.onlinePlayers;
	}
	
	/**
	 * Returns the maximum amount of players to be sent to the client
	 * @return An integer representing the max players
	 */
	public int getMaxPlayers() {
		return this.maxPlayers;
	}
	
	/**
	 * Returns the MOTD to be sent to the client
	 * @return A String representing the MOTD
	 */
	public String getMOTD() {
		return this.motd;
	}
	
	/**
	 * Returns the protocol version to be sent to the client
	 * @return An integer representing the protocol version
	 */
	public int getProtocolVersion() {
		return this.protocolVersion;
	}
	
	/**
	 * Returns the protocol name to be sent to the client
	 * @return A String representing the protocol's name
	 */
	public String getProtocolName() {
		return this.protocolName;
	}
	
	/**
	 * Returns a list of player names to be sent to the client
	 * @return A list of player names
	 */
	public List<String> getPlayerSample() {
		return this.playerSample;
	}
	
	/**
	 * Returns whether the player sample is to be hidden
	 * @return A boolean
	 */
	public boolean isPlayerSampleHidden() {
		return this.hidePlayerSample;
	}
	
	/**
	 * Returns the server's icon that is to be sent to the client
	 * @return The server icon
	 */
	public CachedServerIcon getIcon() {
		return this.icon;
	}
	
	/**
	 * Sets the amount of online players to be sent to the client
	 * @param onlinePlayers Amount of online players
	 */
	public void setOnlinePlayers(int onlinePlayers) {
		this.onlinePlayers = onlinePlayers;
	}
	
	/**
	 * Sets the maximum amount of players to be sent to the client
	 * @param maxPlayers Amount of maximum players
	 */
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	
	/**
	 * Sets the MOTD to be sent to the client
	 * @param motd The MOTD
	 */
	public void setMOTD(String motd) {
		this.motd = motd;
	}
	
	/**
	 * Sets the protocol version to be sent to the client
	 * @param protocolVersion The protocol version
	 */
	public void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}
	
	/**
	 * Sets the protocol name to be sent to the client
	 * @param protocolName The protocol's name
	 */
	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}
	
	/**
	 * Sets the list of player names that will be sent to the client
	 * Note that if the player sample is hidden this list will not be sent
	 * @param playerSample New list of player names
	 */
	public void setPlayerSample(List<String> playerSample) {
		this.playerSample = playerSample;
	}
	
	/**
	 * Sets whether the player sample should be hidden
	 * @param hidePlayerSample A boolean
	 */
	public void hidePlayerSample(boolean hidePlayerSample) {
		this.hidePlayerSample = hidePlayerSample;
	}
	
	/**
	 * Sets the icon to be sent to the client
	 * @param icon The icon
	 */
	public void setIcon(CachedServerIcon icon) {
		this.icon = icon;
	}
}
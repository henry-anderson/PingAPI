package com.skionz.pingapi;

import io.netty.channel.ChannelHandlerContext;

import com.mojang.authlib.GameProfile;

public class PingReply {
	protected ChannelHandlerContext ctx;
	private String motd;
	private int onlinePlayers;
	private int maxPlayers;
	private int protocolVersion;
	private String protocolName;
	private GameProfile[] playerSample;
	private boolean cancelled = false;
	
	public PingReply(ChannelHandlerContext ctx, String motd, int onlinePlayers, int maxPlayers, int protocolVersion, String protocolName, GameProfile[] playerSample) {
		this.ctx = ctx;
		this.motd = motd;
		this.onlinePlayers = onlinePlayers;
		this.maxPlayers = maxPlayers;
		this.protocolVersion = protocolVersion;
		this.protocolName = protocolName;
		this.playerSample = playerSample;
	}
	
	public int getOnlinePlayers() {
		return this.onlinePlayers;
	}
	
	public int getMaxPlayers() {
		return this.maxPlayers;
	}
	
	public String getMOTD() {
		return this.motd;
	}
	
	public int getProtocolVersion() {
		return this.protocolVersion;
	}
	
	public String getProtocolName() {
		return this.protocolName;
	}
	
	public GameProfile[] getPlayerSample() {
		return this.playerSample;
	}
	
	public boolean isCancelled() {
		return this.cancelled;
	}
	
	
	public void setOnlinePlayers(int onlinePlayers) {
		this.onlinePlayers = onlinePlayers;
	}
	
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	
	public void setMOTD(String motd) {
		this.motd = motd;
	}
	
	public void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}
	
	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}
	
	public void setPlayerSample(GameProfile[] playerSample) {
		this.playerSample = playerSample;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
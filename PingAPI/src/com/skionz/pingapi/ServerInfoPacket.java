package com.skionz.pingapi;

import net.minecraft.server.v1_8_R1.ChatComponentText;
import net.minecraft.server.v1_8_R1.PacketStatusOutServerInfo;
import net.minecraft.server.v1_8_R1.ServerPing;
import net.minecraft.server.v1_8_R1.ServerPingPlayerSample;
import net.minecraft.server.v1_8_R1.ServerPingServerData;

public class ServerInfoPacket {
	private PingReply reply;
	
	public ServerInfoPacket(PingReply reply) {
		this.reply = reply;
	}
	
	public void send() {
		this.reply.ctx.writeAndFlush(this.constructorPacket(reply));
	}
	
	private PacketStatusOutServerInfo constructorPacket(PingReply reply) {
		ServerPingPlayerSample playerSample = new ServerPingPlayerSample(reply.getMaxPlayers(), reply.getOnlinePlayers());
        playerSample.a(reply.getPlayerSample());
        ServerPing ping = new ServerPing();
        ping.setMOTD(new ChatComponentText(reply.getMOTD()));
        ping.setPlayerSample(playerSample);
        ping.setServerInfo(new ServerPingServerData(reply.getProtocolName(), reply.getProtocolVersion()));
        return new PacketStatusOutServerInfo(ping);
	}
	
	public PingReply getPingReply() {
		return this.reply;
	}
	
	public void setPingReply(PingReply reply) {
		this.reply = reply;
	}
}

package com.skionz.pingapi.v1_7_R4;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.skionz.pingapi.PingAPI;
import com.skionz.pingapi.PingEvent;
import com.skionz.pingapi.PingListener;
import com.skionz.pingapi.PingReply;
import com.skionz.pingapi.reflect.ReflectUtils;

import net.minecraft.server.v1_7_R4.ChatComponentText;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.PacketStatusOutServerInfo;
import net.minecraft.server.v1_7_R4.ServerPing;
import net.minecraft.server.v1_7_R4.ServerPingPlayerSample;
import net.minecraft.server.v1_7_R4.ServerPingServerData;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.io.netty.channel.ChannelDuplexHandler;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.channel.ChannelPromise;

public class DuplexHandler extends ChannelDuplexHandler {
	private Field serverPingField = ReflectUtils.getFirstFieldByType(PacketStatusOutServerInfo.class, ServerPing.class);
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if(msg instanceof PacketStatusOutServerInfo) {
			PacketStatusOutServerInfo packet = (PacketStatusOutServerInfo) msg;
			PingReply reply = this.constructReply(packet, ctx);
			PingEvent event = new PingEvent(reply);
			for(PingListener listener : PingAPI.getListeners()) {
				listener.onPing(event);
			}
			if(!event.isCancelled()) {
				super.write(ctx, this.constructorPacket(reply), promise);
			}
			return;
		}
		super.write(ctx, msg, promise);
	}

	private PingReply constructReply(PacketStatusOutServerInfo packet, ChannelHandlerContext ctx) {
		try {
			ServerPing ping = (ServerPing) serverPingField.get(packet);
			String motd = ChatSerializer.a(ping.a());
			int max = ping.b().a();
			int online = ping.b().b();
			int protocolVersion = ping.c().b();
			String protocolName = ping.c().a();
			GameProfile[] profiles = ping.b().c();
			List<String> list = new ArrayList<String>();
			for(int i = 0; i < profiles.length; i++) {
				list.add(profiles[i].getName());
			}
			PingReply reply = new PingReply(ctx, motd, online, max, protocolVersion, protocolName, list);
			return reply;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private PacketStatusOutServerInfo constructorPacket(PingReply reply) {
		GameProfile[] sample = new GameProfile[reply.getPlayerSample().size()];
		List<String> list = reply.getPlayerSample();
		for(int i = 0; i < list.size(); i++) {
			sample[i] = new GameProfile(UUID.randomUUID(), list.get(i));
		}
		ServerPingPlayerSample playerSample = new ServerPingPlayerSample(reply.getMaxPlayers(), reply.getOnlinePlayers());
        playerSample.a(sample);
        ServerPing ping = new ServerPing();
        ping.setMOTD(new ChatComponentText(reply.getMOTD()));
        ping.setPlayerSample(playerSample);
        ping.setServerInfo(new ServerPingServerData(reply.getProtocolName(), reply.getProtocolVersion()));
        return new PacketStatusOutServerInfo(ping);
	}
}

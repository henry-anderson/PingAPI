package com.skionz.pingapi.injector;

import java.lang.reflect.Field;

import com.mojang.authlib.GameProfile;
import com.skionz.pingapi.PingAPI;
import com.skionz.pingapi.PingEvent;
import com.skionz.pingapi.PingListener;
import com.skionz.pingapi.PingReply;
import com.skionz.pingapi.reflect.ReflectUtils;

import net.minecraft.server.v1_8_R1.ChatComponentText;
import net.minecraft.server.v1_8_R1.PacketStatusOutServerInfo;
import net.minecraft.server.v1_8_R1.ServerPing;
import net.minecraft.server.v1_8_R1.ServerPingPlayerSample;
import net.minecraft.server.v1_8_R1.ServerPingServerData;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

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
			String motd = ping.a().getText();
			int max = ping.b().a();
			int online = ping.b().b();
			int protocolVersion = ping.c().b();
			String protocolName = ping.c().a();
			GameProfile[] profiles = ping.b().c();
			PingReply reply = new PingReply(ctx, motd, online, max, protocolVersion, protocolName, profiles);
			return reply;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private PacketStatusOutServerInfo constructorPacket(PingReply reply) {
		ServerPingPlayerSample playerSample = new ServerPingPlayerSample(reply.getMaxPlayers(), reply.getOnlinePlayers());
        playerSample.a(reply.getPlayerSample());
        ServerPing ping = new ServerPing();
        ping.setMOTD(new ChatComponentText(reply.getMOTD()));
        if(reply.arePlayersHidden()) {
        	ping.setPlayerSample(null);
        }
        else {
        	ping.setPlayerSample(playerSample);
        }
        ping.setServerInfo(new ServerPingServerData(reply.getProtocolName(), reply.getProtocolVersion()));
        PacketStatusOutServerInfo packet = new PacketStatusOutServerInfo(ping);
        return packet;
	}
}

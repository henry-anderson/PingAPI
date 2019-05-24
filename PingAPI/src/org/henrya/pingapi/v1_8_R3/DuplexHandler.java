package org.henrya.pingapi.v1_8_R3;

import com.mojang.authlib.GameProfile;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketStatusOutPong;
import net.minecraft.server.v1_8_R3.PacketStatusOutServerInfo;
import net.minecraft.server.v1_8_R3.ServerPing;
import net.minecraft.server.v1_8_R3.ServerPing.ServerData;
import net.minecraft.server.v1_8_R3.ServerPing.ServerPingPlayerSample;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftIconCache;
import org.henrya.pingapi.PingAPI;
import org.henrya.pingapi.PingEvent;
import org.henrya.pingapi.PingListener;
import org.henrya.pingapi.PingReply;
import org.henrya.pingapi.reflect.ReflectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DuplexHandler extends ChannelDuplexHandler {
	private static final Field serverPingField = ReflectUtils.getFirstFieldByType(PacketStatusOutServerInfo.class, ServerPing.class);
	private PingEvent event;
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if(msg instanceof PacketStatusOutServerInfo) {
			PacketStatusOutServerInfo packet = (PacketStatusOutServerInfo) msg;
			PingReply reply = this.constructReply(packet, ctx);
			PingEvent event = new PingEvent(reply);
			for(PingListener listener : PingAPI.getListeners()) {
				listener.onPing(event);
			}
			this.event = event;
			if(!event.isCancelled()) {
				super.write(ctx, this.constructPacket(reply), promise);
			}
			return;
		}
		if(msg instanceof PacketStatusOutPong) {
			if(this.event != null && this.event.isPongCancelled()) {
				return;
			}
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
			PingReply reply = new PingReply(motd, online, max, protocolVersion, protocolName, list);
			return reply;
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private PacketStatusOutServerInfo constructPacket(PingReply reply) {
		GameProfile[] sample = new GameProfile[0];
		if(!reply.isPlayerSampleHidden()) {
			sample = new GameProfile[reply.getPlayerSample().size()];
			List<String> list = reply.getPlayerSample();
			for (int i = 0; i < list.size(); i++) {
				sample[i] = new GameProfile(UUID.randomUUID(), list.get(i));
			}
		}
		ServerPingPlayerSample playerSample = new ServerPingPlayerSample(reply.getMaxPlayers(), reply.getOnlinePlayers());
		playerSample.a(sample);
		ServerPing ping = new ServerPing();
		ping.setMOTD(new ChatComponentText(reply.getMOTD()));
		ping.setPlayerSample(playerSample);
		ping.setServerInfo(new ServerData(reply.getProtocolName(), reply.getProtocolVersion()));
		ping.setFavicon(((CraftIconCache) reply.getIcon()).value);
		return new PacketStatusOutServerInfo(ping);
	}
}

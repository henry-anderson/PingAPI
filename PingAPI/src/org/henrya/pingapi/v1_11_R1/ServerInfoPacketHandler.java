package org.henrya.pingapi.v1_11_R1;

import com.mojang.authlib.GameProfile;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.server.v1_11_R1.ChatComponentText;
import net.minecraft.server.v1_11_R1.PacketStatusOutServerInfo;
import net.minecraft.server.v1_11_R1.ServerPing;
import net.minecraft.server.v1_11_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_11_R1.ServerPing.ServerData;
import net.minecraft.server.v1_11_R1.ServerPing.ServerPingPlayerSample;
import org.bukkit.craftbukkit.v1_11_R1.util.CraftIconCache;
import org.henrya.pingapi.PingReply;
import org.henrya.pingapi.ServerInfoPacket;
import org.henrya.pingapi.reflect.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerInfoPacketHandler extends ServerInfoPacket {
	private static final Field SERVER_PING_FIELD = ReflectUtils.getFirstFieldByType(PacketStatusOutServerInfo.class, ServerPing.class);

	/**
	 * Constructs a new ServerInfoPacketHandler from a PingReply instance
	 * @param reply The PingReply instance
	 */
	public ServerInfoPacketHandler(PingReply reply) {
		super(reply);
	}
	
	/**
	 * Sends the PacketStatusOutServerInfo to the client
	 */
	@Override
	public void send() {
		try {
			Field field = this.getReply().getClass().getDeclaredField("ctx");
			field.setAccessible(true);
			Object ctx = field.get(this.getReply());
			Method writeAndFlush = ctx.getClass().getMethod("writeAndFlush", Object.class);
			writeAndFlush.setAccessible(true);
			writeAndFlush.invoke(ctx, ServerInfoPacketHandler.constructPacket(this.getReply()));
		} catch(NoSuchFieldException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new PacketStatusOutServerInfo packet from the data found in a PingReply instance
	 * @param reply The PingReply instance
	 * @return A PacketStatusOutServerInfo packet
	 */
	public static PacketStatusOutServerInfo constructPacket(PingReply reply) {
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
		ping.setServerInfo(new ServerData(reply.getProtocolName(), reply.getProtocolVersion()));
		ping.setFavicon(((CraftIconCache) reply.getIcon()).value);
		return new PacketStatusOutServerInfo(ping);
	}
	
	/**
	 * Creates a new PingReply instance from the data found in a PacketStatusOutServerInfo packet
	 * @param packet The PacketStatusOutServerInfo instance
	 * @param ctx The ChannelHandlerContext instance
	 * @return A PingReply instance
	 */
	public static PingReply constructReply(PacketStatusOutServerInfo packet, ChannelHandlerContext ctx) {
		try {
			ServerPing ping = (ServerPing) SERVER_PING_FIELD.get(packet);
			String motd = ChatSerializer.a(ping.a());
			int max = ping.b().a();
			int online = ping.b().b();
			int protocolVersion = ping.getServerData().getProtocolVersion();
			String protocolName = ping.getServerData().a();
			GameProfile[] profiles = ping.b().c();
			List<String> list = new ArrayList<String>();
			for(int i = 0; i < profiles.length; i++) {
				list.add(profiles[i].getName());
			}
			PingReply reply = new PingReply(ctx, motd, online, max, protocolVersion, protocolName, list);
			return reply;
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
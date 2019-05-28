package org.henrya.pingapi.v1_7_R2;

import org.henrya.pingapi.PingAPI;
import org.henrya.pingapi.PingEvent;
import org.henrya.pingapi.PingListener;
import org.henrya.pingapi.PingReply;

import net.minecraft.server.v1_7_R2.PacketStatusOutPong;
import net.minecraft.server.v1_7_R2.PacketStatusOutServerInfo;
import net.minecraft.util.io.netty.channel.ChannelDuplexHandler;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.channel.ChannelPromise;

public class DuplexHandler extends ChannelDuplexHandler {
	private PingEvent event;
	
	/**
	 * The write() method sends packets to the client
	 * It needs to be overrode in order to listen for outgoing packets
	 */
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if(msg instanceof PacketStatusOutServerInfo) {
			PacketStatusOutServerInfo packet = (PacketStatusOutServerInfo) msg;
			PingReply reply = ServerInfoPacketHandler.constructReply(packet, ctx);
			this.event = new PingEvent(reply);
			for(PingListener listener : PingAPI.getListeners()) {
				listener.onPing(event);
			}
			if(!this.event.isCancelled()) {
				super.write(ctx, ServerInfoPacketHandler.constructPacket(reply), promise);
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
}

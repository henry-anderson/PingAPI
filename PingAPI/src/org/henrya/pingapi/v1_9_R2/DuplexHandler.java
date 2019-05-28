package org.henrya.pingapi.v1_9_R2;

import org.henrya.pingapi.PingAPI;
import org.henrya.pingapi.PingEvent;
import org.henrya.pingapi.PingListener;
import org.henrya.pingapi.PingReply;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_9_R2.PacketStatusOutPong;
import net.minecraft.server.v1_9_R2.PacketStatusOutServerInfo;

/**
 * A class for listening to outgoing packets
 * @author Henry Anderson
 */
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
		else if(msg instanceof PacketStatusOutPong) {
			if(this.event != null && this.event.isPongCancelled()) {
				return;
			}
		}
		super.write(ctx, msg, promise);
	}
	
	/**
	 * In versions 1.8 and higher the connection is closed after two packets have been sent
	 * Overriding this method is required to create animations for versions 1.8 through 1.8.3
	 */
	@Override
    public void close(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
        if(this.event == null || !this.event.isPongCancelled()) {
            super.close(ctx, future);
        }
    }
}

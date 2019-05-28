package org.henrya.pingapi.v1_10_R1;

import net.minecraft.server.v1_10_R1.PacketStatusOutPong;
import org.henrya.pingapi.PingEvent;
import org.henrya.pingapi.PingReply;
import org.henrya.pingapi.PongPacket;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A class that handles the sending of PacketStatusOutPong packets
 * @author Henry Anderson
 */
public class PongPacketHandler extends PongPacket {
	
	/**
	 * Constructs a new ServerInfoPacketHandler from a PingEvent instance
	 * @param reply The PingEvent instance
	 */
	public PongPacketHandler(PingEvent reply) {
		super(reply);
	}
	
	/**
	 * Sends the PacketStatusOutPong packet to the client
	 */
	@Override
	public void send() {
		try {
			PingReply reply = this.getEvent().getReply();
			PacketStatusOutPong packet = new PacketStatusOutPong();
			Field field = this.getEvent().getReply().getClass().getDeclaredField("ctx");
			field.setAccessible(true);
			Object ctx = field.get(reply);
			Method writeAndFlush = ctx.getClass().getMethod("writeAndFlush", Object.class);
			writeAndFlush.setAccessible(true);
			writeAndFlush.invoke(ctx, packet);
		} catch(NoSuchFieldException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
}
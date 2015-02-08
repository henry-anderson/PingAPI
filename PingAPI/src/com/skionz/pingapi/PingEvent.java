package com.skionz.pingapi;

public class PingEvent {
	private PingReply reply;
	private boolean cancel;
	
	public PingEvent(PingReply reply) {
		this.reply = reply;
	}
	
	public PingReply getReply() {
		return this.reply;
	}
	
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
	
	public boolean isCancelled() {
		return this.cancel;
	}
	
	public ServerInfoPacket createNewPacket() {
		return new ServerInfoPacket(this.reply);
	}
}
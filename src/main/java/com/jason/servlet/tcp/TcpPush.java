package com.jason.servlet.tcp;

import com.jason.session.Push;
import com.jason.session.ServerProtocol;
import com.jason.session.Session;
import com.jason.util.WrapperUtil;

import io.netty.channel.Channel;
/**
 * TCP推送管道
 * @author kira
 *
 */
public class TcpPush implements Push {
	
	private Channel channel;
	
	public TcpPush(Channel channel) {
		this.channel = channel;
	}

	@Override
	public void push(Session session, String command, byte[] body) {
		if (isPushable()) {
			channel.write(WrapperUtil.wrapper(command, 0, body));
		}
	}

	@Override
	public boolean isPushable() {
		return channel != null && channel.isWritable();
	}

	@Override
	public void clear() {
		if (channel != null) {
			channel.close();
		}
	}

	@Override
	public void discard() {
//		channel.close();
	}

	@Override
	public void heartbeat() {
		String command = "heartBeat";
		channel.write(WrapperUtil.wrapper(command, 0, command.getBytes()));
	}

	@Override
	public ServerProtocol getPushProtocol() {
		return ServerProtocol.TCP;
	}

}

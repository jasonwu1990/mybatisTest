package com.jason.framework.netty.handler.tcp;

import com.jason.framework.netty.decode.MessageDecoder;
import com.jason.framework.netty.invocate.InvocationFactory;
import com.jason.framework.netty.servlet.tcp.TcpServerHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class TcpServletChannelHandler extends ChannelInitializer<SocketChannel>{
	private InvocationFactory ac;
	
	public void init(InvocationFactory ac) {
		this.ac = ac;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// TODO Auto-generated method stub
//		ch.pipeline().addLast("tcpOutHandler", new TcpOutBoundHandler());
//		ch.pipeline().addLast("tcpEncoder", new MessageEncoder());
		ch.pipeline().addLast("tcpDecoder", new MessageDecoder());
		ch.pipeline().addLast("tcpHandler", new TcpServerHandler(ac));  
		
	}
}

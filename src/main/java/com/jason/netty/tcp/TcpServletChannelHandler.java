package com.jason.netty.tcp;

import com.jason.invocate.InvocationFactory;
import com.jason.netty.MessageDecoder;
import com.jason.servlet.tcp.TcpServerHandler;

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

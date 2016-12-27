package com.jason.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class TcpChannnelHandlerInit extends ChannelInitializer<SocketChannel>{

	
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast("tcpDecoder", new MessageDecoder());
		ch.pipeline().addLast("tcphHandler", new TcpChannnelHandlerInit());
	}

}

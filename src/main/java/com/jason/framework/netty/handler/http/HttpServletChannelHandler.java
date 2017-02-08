package com.jason.framework.netty.handler.http;

import com.jason.framework.netty.invocate.InvocationFactory;
import com.jason.framework.netty.servlet.http.HttpServerHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class HttpServletChannelHandler extends ChannelInitializer<SocketChannel>{
	private InvocationFactory ac;
	
	public void init(InvocationFactory ac) {
		this.ac = ac;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码  
		ch.pipeline().addLast("httpEncoder", new HttpResponseEncoder());  
        // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
		ch.pipeline().addLast("httpDecoder", new HttpRequestDecoder());
		ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536)); 
		ch.pipeline().addLast("httpHandler", new HttpServerHandler(ac));  
	}
}

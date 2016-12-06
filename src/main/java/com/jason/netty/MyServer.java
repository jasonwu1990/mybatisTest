package com.jason.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MyServer {
	private final static Logger logger = LoggerFactory.getLogger(MyServer.class);

	private final int port;

	public MyServer(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		ServerBootstrap server = new ServerBootstrap();
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			server.group(new NioEventLoopGroup(), new NioEventLoopGroup())
					.channel(NioServerSocketChannel.class)
					.localAddress(port)
					.childHandler(new DispatcherServletChannelInitializer());

			logger.info("Netty server has started on port : " + port);

			server.bind().sync().channel().closeFuture().sync();
		}
		finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		new MyServer(port).run();
	}
}

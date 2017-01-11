package com.jason.netty;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;

import com.jason.common.config.ServletConfig;
import com.jason.invocate.InvocationFactory;
import com.jason.netty.tcp.TcpServletChannelHandler;
import com.jason.session.SessionManager;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MyServer {
	private final static Logger logger = LoggerFactory.getLogger("com.jason.dayreport");
	private AbstractApplicationContext ac;
	private ServletConfig sc;

	public MyServer(AbstractApplicationContext ac, ServletConfig sc) {
		this.ac = ac;
		this.sc = sc;
	}

	public void run() throws Exception {
		// 服务器工作组
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)  
		EventLoopGroup workerGroup = new NioEventLoopGroup();  
		
		EventLoopGroup httpBossGroup = new NioEventLoopGroup(); // (1)  
		EventLoopGroup httpWorkerGroup = new NioEventLoopGroup();  
		try {
			InvocationFactory servlet  = InvocationFactory.getInstance();
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			TcpServletChannelHandler tcpChannel = new TcpServletChannelHandler();
			tcpChannel.init(servlet);
			
			b.childHandler(tcpChannel)
			.option(ChannelOption.SO_BACKLOG, 128)          // (5)  监听socket设置
			.childOption(ChannelOption.SO_KEEPALIVE, true) // (6)  客户端socket设置
			.childOption(ChannelOption.TCP_NODELAY, true)
			.childOption(ChannelOption.SO_REUSEADDR, true);
			
			ChannelFuture f1 = b.bind(new InetSocketAddress(sc.getIp() , sc.getTcpPort())).sync(); // (7)  绑定端口

			logger.info("Netty server has started on port : " + sc.getTcpPort());

			SessionManager.getInstance().startSessionCheckThread();
			
			f1.channel().closeFuture().sync();
			System.out.println("close");
		}
		finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			httpBossGroup.shutdownGracefully();
			httpWorkerGroup.shutdownGracefully();
		}
	}

//	public static void main(String[] args) throws Exception {
//		int port;
//		if (args.length > 0) {
//			port = Integer.parseInt(args[0]);
//		} else {
//			port = 9000;
//		}
//		new MyServer(port).run();
//	}
}

package com.jason.netty;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

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

	public void start() throws Exception {
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

	public static void main(String[] args) throws Exception {
		GenericApplicationContext  newAc = new GenericApplicationContext();
		ApplicationContext ac = null;
		try {
			// 优先尝试类路径加载
			ac = new ClassPathXmlApplicationContext("classpath*:/spring-mvc.xml");
		}catch (BeansException be) {
			// 如果类路径加载失败，兼容使用文件路径的方式
//			ac = new FileSystemXmlApplicationContext(beanFiles);
		}
		newAc.setParent(ac);

		AnnotationConfigUtils.registerAnnotationConfigProcessors(newAc);
		newAc.refresh();

		newAc.registerShutdownHook();
		newAc.start();
		
//		String scanPath = Configuration.getProperty(Configuration.PACKAGE_PATH);
		String scanPath = "com.jason";
		InvocationFactory.getInstance().init(newAc, scanPath);
		
		ServletConfig sc = new ServletConfig();
		sc.setHttpPort(8001);
		sc.setTcpPort(8000);
		sc.setIp("127.0.0.1");
		sc.setPackagePath("com.jason");
//		sc.setHttpPort(Integer.valueOf(Configuration.getProperty(Configuration.HTTP_PORT)));
//		sc.setTcpPort(Integer.valueOf(Configuration.getProperty(Configuration.TCP_PORT)));
//		sc.setIp(Configuration.getProperty(Configuration.IP));
//		sc.setPackagePath(Configuration.getProperty(Configuration.PACKAGE_PATH));
		
		MyServer server = new MyServer(newAc, sc);
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}  
			
	}
}

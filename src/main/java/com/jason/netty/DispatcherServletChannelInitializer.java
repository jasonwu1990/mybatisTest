package com.jason.netty;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class DispatcherServletChannelInitializer extends ChannelInitializer<SocketChannel>{
	private final DispatcherServlet dispatcherServlet;
	
	public DispatcherServletChannelInitializer() throws ServletException, IOException {

		MockServletContext servletContext = new MockServletContext();
		MockServletConfig servletConfig = new MockServletConfig(servletContext);
		AnnotationConfigWebApplicationContext wac = new AnnotationConfigWebApplicationContext();
		wac.setServletContext(servletContext);
		wac.setServletConfig(servletConfig);
		wac.register(AppConfig.class);
		wac.refresh();
		
		dispatcherServlet = new DispatcherServlet(wac);
		dispatcherServlet.init(servletConfig);

		//set spring config in xml
//		this.dispatcherServlet = new DispatcherServlet();
//		this.dispatcherServlet.setContextConfigLocation("classpath*:/spring-mvc.xml");
//		this.dispatcherServlet.init(servletConfig);
	}

	@Override
	public void initChannel(SocketChannel channel) throws Exception {
		// Create a default pipeline implementation.
		ChannelPipeline pipeline = channel.pipeline();

		// Uncomment the following line if you want HTTPS
		//SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
		//engine.setUseClientMode(false);
		//pipeline.addLast("ssl", new SslHandler(engine));


		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
		pipeline.addLast("handler", new ServletNettyHandler(this.dispatcherServlet));
	}

}

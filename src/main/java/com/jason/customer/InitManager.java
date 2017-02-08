package com.jason.customer;

import org.springframework.context.support.AbstractApplicationContext;

import com.jason.framework.common.Configuration;
import com.jason.framework.common.config.ServletConfig;
import com.jason.framework.netty.server.MyServer;


public class InitManager{

	private AbstractApplicationContext ac;
	
	public void init() throws Exception {
		Configuration.init();
//		InterceptorInitManager.init();
		InvocationInitManager.init(ac);
		startServer();
	}


	private void startServer() {
		ServletConfig sc = new ServletConfig();
		sc.setHttpPort(Integer.valueOf(Configuration.getProperty(Configuration.HTTP_PORT)));
		sc.setTcpPort(Integer.valueOf(Configuration.getProperty(Configuration.TCP_PORT)));
		sc.setIp(Configuration.getProperty(Configuration.IP));
		sc.setPackagePath(Configuration.getProperty(Configuration.PACKAGE_PATH));
		
		MyServer server = new MyServer(ac, sc);
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void setContext(AbstractApplicationContext context) {
		this.ac = context;
	}
	
}

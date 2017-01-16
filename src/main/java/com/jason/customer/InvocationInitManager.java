package com.jason.customer;

import org.springframework.context.support.AbstractApplicationContext;
import com.jason.common.Configuration;
import com.jason.invocate.InvocationFactory;


public class InvocationInitManager {

	public static void init(AbstractApplicationContext ac) throws Exception {
		String scanPath = Configuration.getProperty(Configuration.PACKAGE_PATH);
		InvocationFactory.getInstance().init(ac, scanPath);
	}
	
}

package com.jason.log4jDemo;

import org.apache.log4j.Logger;

public class Log4jTest {
	
	private static final Logger logger = Logger.getLogger(Log4jTest.class);
	private static final Logger logger2 = Logger.getLogger("com.jason.dayreport");
	
	public static void main(String[] args) {
		logger.debug("@debug");	
		logger.info("@info");
		logger.warn("@warn");
		logger.error("@error");
		logger2.debug("@debug2");
		logger2.info("@info2");
		logger2.warn("@warn2");
		logger2.error("@error2");
	}

}

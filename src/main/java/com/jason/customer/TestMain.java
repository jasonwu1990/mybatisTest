package com.jason.customer;

public class TestMain {

	public static void main(String[] args) throws Exception {
		InitManager initializer = new InitManager();
		CustomApplication application = new CustomApplication(initializer, new String[] {
				"classpath*:/spring-mvc.xml","classpath*:/spring-mybatis.xml" });
		application.start();
		
	}
}

package com.jason.mybatisTest;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * Hello world!
 *
 */
public class App 
{
	private static SqlSessionFactoryBuilder builder;
	
	private static SqlSessionFactory factory;
	
    public static void main(String[] args) throws IOException
    {
		String resource = "mybatis-config.xml";
		Reader reader = Resources.getResourceAsReader(resource);
		builder = new SqlSessionFactoryBuilder();
		factory = builder.build(reader);
    }
}

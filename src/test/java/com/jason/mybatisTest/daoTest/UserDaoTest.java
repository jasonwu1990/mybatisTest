package com.jason.mybatisTest.daoTest;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;

import com.jason.user.dao.UserDao;
import com.jason.user.dto.User;

public class UserDaoTest {

//	  @Test
//	  public void findUserById() {
//	      SqlSession sqlSession = getSessionFactory().openSession(true);  
//	      UserDao userMapper = sqlSession.getMapper(UserDao.class);  
//	      User user = userMapper.findUserById(8);
//	      Assert.assertNotNull("没找到数据", user);
//	  }
//	  
	  @Test
	  public void insertUser() {
		  // openSession参数表示事务是否自动提交，mybatis管理事务默认是不自动提交的, 
		  // 或者手动调用session的commit
		  SqlSession sqlSession = getSessionFactory().openSession(true);
		  UserDao userMapper = sqlSession.getMapper(UserDao.class);
		  User user = new User();
		  user.setAge(154);
		  user.setDeleteFlag(0);
		  user.setName("牛逼呀");
		  user.setPassword("1990");
		  userMapper.insert(user);
		  Assert.assertTrue(true);
	  }
//	  
//	  @Test
//	  public void updateUser() {
//		  SqlSession sqlSession = getSessionFactory().openSession(true);
//		  UserDao userMapper = sqlSession.getMapper(UserDao.class);
//	      User user = userMapper.findUserById(2);
//	      user.setAge(100);
//	      userMapper.updateUser(user);
//	      Assert.assertTrue(true);
//	  }
//	  
//	  @Test
//	  public void deleteUser() {
//		  SqlSession sqlSession = getSessionFactory().openSession(true);
//		  UserDao userMapper = sqlSession.getMapper(UserDao.class);
//	      User user = userMapper.findUserById(9);
//	      userMapper.deleteUser(user);
//	      Assert.assertTrue(true);
//	  }
//
//	  @Test
//	  public void findUser() {
//	      SqlSession sqlSession = getSessionFactory().openSession(true);  
//	      UserDao userMapper = sqlSession.getMapper(UserDao.class);  
//	      List<User> users = userMapper.findAllUsers();
//	      Assert.assertNotNull("没找到数据", users);
//	  }
	
	
	  // Mybatis 通过SqlSessionFactory获取SqlSession, 然后才能通过SqlSession与数据库进行交互
	  private static SqlSessionFactory getSessionFactory() {
	      SqlSessionFactory sessionFactory = null;  
	      String resource = "mybatis-config.xml";  
	      try {  
	          sessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(resource));
	      } catch (IOException e) {
	          e.printStackTrace();  
	      }  
	      return sessionFactory;  
	  }
	
}

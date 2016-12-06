package com.jason.user.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jason.user.dao.UserDao;
import com.jason.user.dto.User;

@Component
public class UserService implements IUserService {

	@Autowired
	private SqlSessionFactory sessionFactory;
	
	@Override
	public User getUserById(int userId) {
	      SqlSession sqlSession = sessionFactory.openSession(true);  
	      UserDao userDao = sqlSession.getMapper(UserDao.class);  
	      User user = userDao.findUserById(userId);
	      return user;
	}

}

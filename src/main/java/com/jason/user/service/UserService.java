package com.jason.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jason.user.dao.UserDao;
import com.jason.user.dto.User;

@Component
public class UserService implements IUserService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public User getUserById(int userId) {
	      User user = userDao.findUserById(userId);
	      return user;
	}

}

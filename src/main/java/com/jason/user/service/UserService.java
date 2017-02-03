package com.jason.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jason.servlet.Result;
import com.jason.user.dao.UserDao;
import com.jason.user.dto.User;
import com.jason.util.ResultUtil;

@Component
public class UserService implements IUserService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public User getUserById(int userId) {
	      User user = userDao.findUserById(userId);
	      return user;
	}
	
	@Override
	public Result login(String username, String password) {
		User user = userDao.findUserByUsername(username);
		if (user == null) {
			return ResultUtil.buildResultFail("No such user!");
		}
//		if (user.getPassword().equals(MD5Util.getMD5Code(password, MD5Util.ENCODE_SIMPLE))) {
			return ResultUtil.buildResultSucc(user);
//		}
//		return ResultUtil.buildResultFail("Password error!");
	}

	@Override
	public Result register(String username, String password, int age) {
		
		return null;
	}

}

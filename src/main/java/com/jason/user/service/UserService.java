package com.jason.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jason.framework.common.json.JsonBuilder;
import com.jason.framework.common.json.JsonDocument;
import com.jason.mvc.view.ResultState;
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
	
	@Override
	public byte[] login(String username, String password) {
		User user = userDao.findUserByUsername(username);
		if (user == null) {
			return JsonBuilder.getFailJson("No such user!");
		}
//		if (user.getPassword().equals(MD5Util.getMD5Code(password, MD5Util.ENCODE_SIMPLE))) {
		return JsonBuilder.getJson(ResultState.SUCCESS, "");
//		}
//		return ResultUtil.buildResultFail("Password error!");
	}

	@Override
	public byte[] register(String username, String password, int age) {
		User user = userDao.findUserByUsername(username);
		if(user != null) {
			userDao.deleteUser(user);
		}
		user = new User();
		user.setName(username);
		user.setPassword(password);
		user.setAge(age);
		userDao.insert(user);
		
		JsonDocument doc = new JsonDocument();
		doc.startObject();
		doc.createElement("username", user.getName());
		doc.endObject();
		
		return JsonBuilder.getJson(ResultState.SUCCESS, doc.toByte());
		
	}

}

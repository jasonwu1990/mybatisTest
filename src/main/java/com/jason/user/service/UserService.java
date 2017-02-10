package com.jason.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jason.framework.common.json.JsonBuilder;
import com.jason.framework.common.json.JsonDocument;
import com.jason.mvc.view.ResultState;
import com.jason.user.dao.UserDao;
import com.jason.user.dto.User;
import com.jason.util.stl.Tuple;

@Component
public class UserService implements IUserService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public byte[] getUserById(int userId) {
	      User user = userDao.findUserById(userId);
	      if(user == null) {
	    	  return JsonBuilder.getFailJson("No such user!"); 
	      }
	      JsonDocument doc = new JsonDocument();
	      doc.startObject();
	      doc.createElement("username", user.getName());
	      doc.endObject();
	      return JsonBuilder.getJson(ResultState.SUCCESS, doc.toByte());
	}
	
	@Override
	public Tuple<User, byte[]> login(String username, String password) {
		Tuple<User, byte[]> tuple = new Tuple<User, byte[]> ();
		User user = userDao.findUserByUsername(username);
		if (user == null) {
			tuple.right = JsonBuilder.getFailJson("No such user!");
			return tuple;
		}
		tuple.left = user;
		return tuple;
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

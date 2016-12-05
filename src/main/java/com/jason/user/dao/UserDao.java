package com.jason.user.dao;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.jason.user.dto.User;

//@Alias("UserDao")
public interface UserDao {

	  public void insert(User user);

	  public User findUserById (int userId);

	  public void updateUser(User user);
	  
	  public void deleteUser(User user);
	  
	  public List<User> findAllUsers();
	
}

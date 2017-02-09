package com.jason.user.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jason.mybatis.annotation.MyBatisRepository;
import com.jason.user.dto.User;

@Component
@MyBatisRepository
public interface UserDao {

	  public void insert(User user);

	  public User findUserById (int userId);

	  public User findUserByUsername (String name);
	  
	  public void updateUser(User user);
	  
	  public void deleteUser(User user);
	  
	  public List<User> findAllUsers();
	
}

package com.jason.user.service;

import com.jason.user.dto.User;

public interface IUserService {

	User getUserById(int userId);

	byte[] login(String username, String password);

	byte[] register(String username, String password, int age);

}

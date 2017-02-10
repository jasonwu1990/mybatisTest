package com.jason.user.service;

import com.jason.user.dto.User;
import com.jason.util.stl.Tuple;

public interface IUserService {

	byte[] getUserById(int userId);

	Tuple<User, byte[]> login(String username, String password);

	byte[] register(String username, String password, int age);

}

package com.jason.user.service;

import com.jason.servlet.Result;
import com.jason.user.dto.User;

public interface IUserService {

	User getUserById(int userId);

	Result login(String username, String password);

}

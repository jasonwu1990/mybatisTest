package com.jason.user.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.jason.annotation.Action;
import com.jason.annotation.Command;
import com.jason.annotation.RequestParam;
import com.jason.common.dto.UserDto;
import com.jason.servlet.Result;
import com.jason.user.dto.User;
import com.jason.user.service.IUserService;
import com.jason.util.ResultUtil;

@Action
public class UserAction {
	
	@Autowired
	private IUserService userService;
	
	@Command(value="user@login")
	public Result login(@RequestParam("username")String username, 
			@RequestParam("password")String password) {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return ResultUtil.buildResultFail("Username and password can't be empty");
		}
		Result result = userService.login(username, password);
		if (result.getCode() != ResultUtil.SUCCESS) {
			return result;
		}
		User user = (User)result.getData();
		UserDto dto = UserDto.getNewUserDto(String.valueOf(user.getId()), user.getId(), "poem");
		return ResultUtil.buildResultSucc(dto);
	}

	@Command(value="user@regist")
	public Result regist(@RequestParam("username") String username, 
			@RequestParam(value="password") String password,
			@RequestParam("mobile") String mobile,
			@RequestParam("email") String email,
			@RequestParam("adult") int adult,
			@RequestParam("age") int age,
			@RequestParam("sex") int sex,
			@RequestParam("career") int career) {
		return ResultUtil.buildResultSucc();
	}

	@Command(value="user@read")
	public Result read(@RequestParam("id") int id) {
		User user = userService.getUserById(id);
		return ResultUtil.buildResultSucc(user);
	}

}

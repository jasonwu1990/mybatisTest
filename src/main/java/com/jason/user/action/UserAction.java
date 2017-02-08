package com.jason.user.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jason.annotation.Action;
import com.jason.annotation.Command;
import com.jason.annotation.RequestParam;
import com.jason.framework.json.JsonBuilder;
import com.jason.servlet.Result;
import com.jason.user.dto.User;
import com.jason.user.service.IUserService;
import com.jason.util.ResultUtil;

@Component
@Action
public class UserAction {
	
	@Autowired
	private IUserService userService;
	

	@Command(value="userRegist")
	public byte[] regist(@RequestParam("username") String username, 
			@RequestParam(value="password") String password,
			@RequestParam("age") int age) {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return JsonBuilder.getFailJson("Username and password can't be empty");
		}
		byte[] result = userService.register(username, password, age);
		return result;
	}
	
	@Command(value="userLogin")
	public byte[] login(@RequestParam("username")String username, 
			@RequestParam("password")String password) {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return JsonBuilder.getFailJson("Username and password can't be empty");
		}
		byte[] result = userService.login(username, password);
//		if (result.getCode() != ResultUtil.SUCCESS) {
			return result;
//		}
//		User user = (User)result.getData();
//		UserDto dto = UserDto.getNewUserDto(String.valueOf(user.getId()), user.getId(), "poem");
//		return ResultUtil.buildResultSucc(dto);
	}

	@Command(value="userRead")
	public Result read(@RequestParam("id") int id) {
		User user = userService.getUserById(id);
		return ResultUtil.buildResultSucc(user);
	}

}

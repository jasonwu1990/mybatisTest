package com.jason.user.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jason.framework.common.Constants;
import com.jason.framework.common.dto.UserDto;
import com.jason.framework.common.json.JsonBuilder;
import com.jason.framework.common.json.JsonDocument;
import com.jason.framework.netty.annotation.Action;
import com.jason.framework.netty.annotation.Command;
import com.jason.framework.netty.annotation.RequestParam;
import com.jason.framework.netty.servlet.Request;
import com.jason.framework.session.Session;
import com.jason.mvc.view.ByteResult;
import com.jason.mvc.view.ResultState;
import com.jason.user.dto.User;
import com.jason.user.service.IUserService;
import com.jason.util.stl.Tuple;

@Component
@Action
public class UserAction {
	
	@Autowired
	private IUserService userService;
	

	@Command(value="userRegist")
	public ByteResult regist(@RequestParam("username") String username, 
			@RequestParam(value="password") String password,
			@RequestParam("age") int age) {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return new ByteResult(JsonBuilder.getFailJson("Username and password can't be empty"));
		}
		byte[] result = userService.register(username, password, age);
		return new ByteResult(result);
	}
	
	@Command(value="userLogin")
	public ByteResult login(@RequestParam("username")String username, 
			@RequestParam("password")String password,
			Request request) {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return new ByteResult(JsonBuilder.getFailJson("Username and password can't be empty"));
		}
		Tuple<User, byte[]> tuple = userService.login(username, password);
		if(tuple.left == null) {
			return new ByteResult(tuple.right);
		}
		User user = tuple.left;
		UserDto dto = UserDto.getNewUserDto(String.valueOf(user.getId()), user.getId(), "poem");
		Session session = request.getNewSession();
		request.setSessionId(session.getId());
		request.getSession().setAttribute(Constants.USER, dto);
		
		JsonDocument doc = new JsonDocument();
		doc.startObject();
		doc.createElement("sessionId", session.getId());
		doc.endObject();
		return new ByteResult(JsonBuilder.getJson(ResultState.SUCCESS, doc.toByte()));
	}

	@Command(value="userRead")
	public ByteResult read(@RequestParam("id") int id) {
		return new ByteResult(userService.getUserById(id));
	}

}

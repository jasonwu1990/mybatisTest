package com.jason.system.action;

import org.springframework.stereotype.Component;

import com.jason.annotation.Action;
import com.jason.annotation.Command;
import com.jason.framework.json.JsonBuilder;
import com.jason.mvc.view.ResultState;

@Component
@Action
public class HeartBeatAction {

	@Command(value="heartbeat")
	public byte[] heartbeat() {
		return JsonBuilder.getJson(ResultState.SUCCESS,"");
	}
	
}

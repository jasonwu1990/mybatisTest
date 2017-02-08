package com.jason.system.action;

import org.springframework.stereotype.Component;

import com.jason.framework.common.json.JsonBuilder;
import com.jason.framework.netty.annotation.Action;
import com.jason.framework.netty.annotation.Command;
import com.jason.mvc.view.ResultState;

@Component
@Action
public class HeartBeatAction {

	@Command(value="heartbeat")
	public byte[] heartbeat() {
		return JsonBuilder.getJson(ResultState.SUCCESS,"");
	}
	
}

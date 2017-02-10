package com.jason.framework.netty.adaptor;

import com.jason.framework.netty.servlet.Request;
import com.jason.framework.netty.servlet.Response;

public class ResponseAdaptor extends Adaptor {

	protected Class<?> type;
	
	public ResponseAdaptor(Class<?> type) {
		this.type = type;
	}
	
	@Override
	public Object get(Request request, Response response) {
		return response;
	}
	
	@Override
	public String getType() {
		return type.getName();
	}
}

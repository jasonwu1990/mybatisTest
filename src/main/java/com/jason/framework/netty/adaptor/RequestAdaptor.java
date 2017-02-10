package com.jason.framework.netty.adaptor;

import com.jason.framework.netty.servlet.Request;
import com.jason.framework.netty.servlet.Response;

public class RequestAdaptor extends Adaptor {

	protected Class<?> type;
	
	public RequestAdaptor(Class<?> type) {
		this.type = type;
	}
	
	@Override
	public Object get(Request request, Response response) {
		return request;
	}
	
	@Override
	public String getType() {
		return type.getName();
	}
}

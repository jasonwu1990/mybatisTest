package com.jason.framework.netty.adaptor;

import com.jason.framework.netty.servlet.Request;
import com.jason.framework.netty.servlet.Response;

public abstract class Adaptor {

	public Object get(Request request, Response response) {
		return null;
	}

	public String getType() {
		return null;
	}
	
}

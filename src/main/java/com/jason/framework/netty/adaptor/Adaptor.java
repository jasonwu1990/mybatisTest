package com.jason.framework.netty.adaptor;

import com.jason.framework.netty.servlet.Request;

public abstract class Adaptor {

	public Object get(Request request) {
		return null;
	}

	public String getType() {
		return null;
	}
	
}

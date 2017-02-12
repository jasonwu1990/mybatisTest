package com.jason.framework.netty.interceptor;

import java.util.Iterator;

import com.jason.framework.netty.invocate.ActionInvocation;
import com.jason.framework.netty.servlet.Request;
import com.jason.framework.netty.servlet.Response;

public interface Interceptor {
	public Object interceptor(Iterator<Interceptor> iterator, ActionInvocation paramActionInvocation,
			Request paramRequest, Response paramResponse);
	
}

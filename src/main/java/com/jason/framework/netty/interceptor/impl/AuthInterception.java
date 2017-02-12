package com.jason.framework.netty.interceptor.impl;

import java.util.Iterator;

import com.jason.framework.common.Configuration;
import com.jason.framework.common.json.JsonDocument;
import com.jason.framework.netty.interceptor.Interceptor;
import com.jason.framework.netty.invocate.ActionInvocation;
import com.jason.framework.netty.servlet.Request;
import com.jason.framework.netty.servlet.Response;
import com.jason.framework.netty.servlet.http.HttpRequest;
import com.jason.mvc.view.ByteResult;
import com.jason.mvc.view.ResultState;
import com.jason.util.WebUtils;

public class AuthInterception implements Interceptor{

	private static final ByteResult IP_FORBIDDEN;
	
	static {
		JsonDocument doc = new JsonDocument();
		doc.startObject();
		doc.createElement("state", ResultState.FAIL.getValue());
		doc.startObject("data");
		doc.createElement("state", "IP_FORBIDDEN");
		doc.endObject();
		doc.endObject();
		IP_FORBIDDEN = new ByteResult(doc.toByte());
	}
	
	@Override
	public Object interceptor(Iterator<Interceptor> iterator, ActionInvocation invocation,
			Request request, Response response) {
		try {
			//check IP
			if(request.getCommand().equals("userLogin")) {
				String ip = request.getIp();
				if(request instanceof HttpRequest) {
					ip = WebUtils.getIp(((HttpRequest)request).getHttpRequest(), request.getChannel());
				}
				String passips = Configuration.getProperty("server.passip");
				if(passips != null && passips.indexOf(ip) < 0) {
					return IP_FORBIDDEN;
				}
			}
			
			Object result = invocation.invoke(iterator, request, response);
			return result;
		}catch(Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

}

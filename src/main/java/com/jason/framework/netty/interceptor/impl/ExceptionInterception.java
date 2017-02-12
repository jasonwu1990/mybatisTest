package com.jason.framework.netty.interceptor.impl;

import java.util.Iterator;

import com.jason.framework.common.json.JsonDocument;
import com.jason.framework.netty.interceptor.Interceptor;
import com.jason.framework.netty.invocate.ActionInvocation;
import com.jason.framework.netty.servlet.Request;
import com.jason.framework.netty.servlet.Response;
import com.jason.mvc.view.ByteResult;
import com.jason.mvc.view.ResultState;

public class ExceptionInterception implements Interceptor{

	private static final ByteResult ERROR_RESULT;
	
	static {
		JsonDocument doc = new JsonDocument();
		doc.startObject();
		doc.createElement("state", ResultState.EXCEPTION.getValue());
		doc.startObject("data");
		doc.createElement("state", "ERROR");
		doc.endObject();
		doc.endObject();
		ERROR_RESULT = new ByteResult(doc.toByte());
	}
	
	
	@Override
	public Object interceptor(Iterator<Interceptor> iterator, ActionInvocation invocation,
			Request request, Response response) {
		try {
			Object result = invocation.invoke(iterator, request, response);
			return result;
		}catch(Throwable t) {
			t.printStackTrace();
		}
		return ERROR_RESULT;
	}

}

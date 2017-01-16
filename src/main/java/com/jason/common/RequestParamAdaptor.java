package com.jason.common;

import com.jason.adaptor.Adaptor;
import com.jason.servlet.Request;
import com.jason.util.Utils;

public class RequestParamAdaptor extends Adaptor {

	protected String name;
	
	protected Class<?> type;
	
	public RequestParamAdaptor(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}
	
	@Override
	public Object get(Request request) {
		Object obj = request.getParamterValues(name);
		try {
			return Utils.cast(obj, type);
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String getType() {
		return type.getName();
	}
}

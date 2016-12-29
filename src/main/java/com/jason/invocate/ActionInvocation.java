package com.jason.invocate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ActionInvocation {
	protected String path;
	
	protected Method method;
	
	protected String methodName;
	
	protected Adaptor[] paramsAdaptors;
	
	private static final String CLASS_KEY = "class";
	
	public ActionInvocation(String path, Method method) {
		this.path = path;
		this.method = method;
		this.methodName = method.getName();
	}
	
	public void init() {
		Annotation[][] annoArr = method.getParameterAnnotations();
		Class<?>[] parameterType = method.getParameterTypes();
		
		
	}
	
}

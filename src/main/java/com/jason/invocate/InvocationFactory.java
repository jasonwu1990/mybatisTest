package com.jason.invocate;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jason.annotation.Action;
import com.jason.annotation.Command;
import com.jason.common.Constants;
import com.jason.common.dto.UserDto;
import com.jason.log4jDemo.Log4jTest;
import com.jason.servlet.Request;
import com.jason.servlet.Response;
import com.jason.servlet.Result;
import com.jason.util.ResultUtil;
import com.jason.util.ScanUtils;
import com.jason.util.Utils;

public class InvocationFactory {
	private static final Logger logger = Logger.getLogger(Log4jTest.class);
	
	private static final InvocationFactory instance = new InvocationFactory();
	
	private InvocationFactory() {}
	
	public static InvocationFactory getInstance() {
		return instance;
	}
	
	private AbstractApplicationContext ac;
	
	protected static Map<String, ActionInvocation> handlerMap = new HashMap<String, ActionInvocation>();
	
	public void init(AbstractApplicationContext ac, String packagePath) throws Exception {
		
		this.ac = ac;

		String scanPath = packagePath;
		if (StringUtils.isEmpty(scanPath)) {
			logger.error("actionPackage path is null！！！！！");
			throw new Exception();
		}
		
		try {
			initHandleAction(scanPath);
		}catch (Exception e) {
			throw e;
		}
	}

	private void initHandleAction(String scanPath) throws Exception {
		Set<Class<?>> set = ScanUtils.getClasses(scanPath);
		for (Class<?> clazz : set) {
			initHandleAction(clazz);
		}
	}

	private void initHandleAction(Class<?> clazz) throws Exception {
		if (Modifier.isAbstract(clazz.getModifiers()) || Modifier.isInterface(clazz.getModifiers())) {
			return;
		}
//		if (!Modifier.isInterface(clazz.getModifiers())) {
//			return;
//		}
		
		Action action = Utils.getAnnotation(clazz, Action.class);
		if (action == null) {
			return;
		}
		
		// 表示该方法只能同步访问
		boolean isSyn = false;
//		Syn syn = clazz.getAnnotation(Syn.class);
//		if (syn != null) {
//			isSyn = syn.value();
//		}
		
		createActionInvocation(clazz, isSyn);
	}
	
	private void createActionInvocation(Class<?> clazz, boolean isSyn) throws Exception {
		Object obj = ac.getBean(clazz);
		if(obj == null) {
			 obj = clazz.newInstance();
		}
		
		ActionInvocation ai;
 		String path = clazz.getName();
		Method[] methods = clazz.getMethods();
		
		for (Method method : methods) {
			if (Modifier.isStatic(method.getModifiers()) || Modifier.isFinal(method.getModifiers())) {
				continue;
			}
			
			Command cmd = method.getAnnotation(Command.class);
			if (cmd == null) {
				continue;
			}
			
			// build
			ai = new ActionInvocation(path, method, isSyn, obj); 
			ai.init();
			
			handlerMap.put(cmd.value(), ai);
		}
		
	}
	
	public void service(Request request, Response response) {
		ActionInvocation invocation = handlerMap.get(request.getCommand().trim());
		try {
			if (null == invocation) {
				throw new RuntimeException("No such method，command is ："+request.getCommand());
			}
//			Result result = invocation.invoke(request, response);
			byte[] result = invocation.invoke(request, response);
			if (result != null) {
//				byte[] resultArr = JSON.toJSONBytes(result);
//				ActionInvocation.render(resultArr, request, response);
				ActionInvocation.render(result, request, response);
			}
//			afterDeal(request, response, result);
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}
	
	private void afterDeal(Request request, Response response, byte[] result) {
		Result r = JSONObject.parseObject(result, Result.class);
		if (request.getCommand().equalsIgnoreCase("user@login") && r.getCode() == ResultUtil.SUCCESS) {
			request.getNewSession().setAttribute(Constants.USER, ((JSONObject)r.getData()).toJavaObject(UserDto.class));
		}
	}

	public static void main(String[] args) {
		System.out.println(ActionInvocation.class.getName());
		System.out.println(ActionInvocation.class.getPackage().getName());
	}
	
}

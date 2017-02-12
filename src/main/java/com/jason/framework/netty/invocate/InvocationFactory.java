package com.jason.framework.netty.invocate;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.util.StringUtils;

import com.jason.framework.netty.annotation.Action;
import com.jason.framework.netty.annotation.Command;
import com.jason.framework.netty.interceptor.Interceptor;
import com.jason.framework.netty.interceptor.impl.AuthInterception;
import com.jason.framework.netty.interceptor.impl.ExceptionInterception;
import com.jason.framework.netty.servlet.Request;
import com.jason.framework.netty.servlet.Response;
import com.jason.log4jDemo.Log4jTest;
import com.jason.mvc.view.ByteResult;
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
	
	protected List<Interceptor> interceptorList;
	
	public void init(AbstractApplicationContext ac, String packagePath) throws Exception {
		this.ac = ac;

		String scanPath = packagePath;
		if (StringUtils.isEmpty(scanPath)) {
			logger.error("actionPackage path is null！！！！！");
			throw new Exception();
		}
		
		try {
			initInterceptorList();
			initHandleAction(scanPath);
		}catch (Exception e) {
			throw e;
		}
	}

	private void initInterceptorList() {
		Interceptor intercete = new ExceptionInterception();
		interceptorList = new ArrayList<Interceptor>();
		interceptorList.add(intercete);
		Interceptor intercete2 = new AuthInterception();
		interceptorList.add(intercete2);
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
			Object result = invocation.invoke(interceptorList == null ? null : interceptorList.iterator(), request, response);
			if (result instanceof ByteResult) {
				ActionInvocation.render(((ByteResult)result).getResult(), request, response);
			}
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	public static void main(String[] args) {
		System.out.println(ActionInvocation.class.getName());
		System.out.println(ActionInvocation.class.getPackage().getName());
	}
	
}

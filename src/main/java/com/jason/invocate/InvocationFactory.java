package com.jason.invocate;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.util.StringUtils;

import com.jason.annotation.Action;
import com.jason.annotation.Command;
import com.jason.util.AnnotationUtils;
import com.jason.util.ScanUtils;

public class InvocationFactory {

	private static final InvocationFactory instance = new InvocationFactory();
	
	private InvocationFactory() {}
	
	public static InvocationFactory getInstance() {
		return instance;
	}
	
	private AbstractApplicationContext ac;
	
	// 指令名称与具体指令实现
	private Map<String, ActionInvocation> handlerMap = new HashMap<String, ActionInvocation>();
	
	/**
	 * 扫描path，获取action注解
	 * @param ac
	 * @param packagePath
	 */
	public void init(AbstractApplicationContext ac, String packagePath) throws Exception{
		this.ac = ac;
		
		String scanPath = packagePath;
		if(StringUtils.isEmpty(scanPath)) {
			throw new Exception();
		}
		
		try{
			initHandlerAction(packagePath);
		}catch(Exception e) {
			throw e;
		}
	}
	
	
	private void initHandlerAction(String packagePath) {
		Set<Class<?>> set = ScanUtils.getClasses(packagePath);
		// TODO: plugin加载
		for(Class<?> clazz: set) {
			initHandlerAction(clazz);
		}
	}

	private void initHandlerAction(Class<?> clazz) {
		if(!Modifier.isInterface(clazz.getModifiers())) {
			return;
		}
		
		Action action = AnnotationUtils.getAnnotation(clazz, Action.class);
		if(action == null) {
			return;
		}
		createActionInvocation(clazz);
	}

	private void createActionInvocation(Class<?> clazz) {
		ActionInvocation ai;
		String path = clazz.getName();
		Method[] methods = clazz.getMethods();
		for(Method method : methods) {
			int methodModifiers = method.getModifiers();
			if(Modifier.isStatic(methodModifiers) || Modifier.isFinal(methodModifiers)) {
				continue;
			}
			Command cmd = method.getAnnotation(Command.class);
			if(cmd == null) {
				continue;
			}
			if(handlerMap.containsKey(cmd.value())) {
				System.out.println("有同名接口啊");
			}
			ai = new ActionInvocation(path, method);
			handlerMap.put(cmd.value(), ai);
		}
		
	}
	
}

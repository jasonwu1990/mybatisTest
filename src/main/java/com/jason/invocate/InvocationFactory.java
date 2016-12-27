package com.jason.invocate;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.util.StringUtils;

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
		if(Modifier.isInterface(clazz.getModifiers())) {
			
		}
		
	}
	
}

package com.jason.framework.netty.invocate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Iterator;

import com.jason.framework.netty.adaptor.Adaptor;
import com.jason.framework.netty.adaptor.RequestAdaptor;
import com.jason.framework.netty.adaptor.RequestParamAdaptor;
import com.jason.framework.netty.adaptor.ResponseAdaptor;
import com.jason.framework.netty.annotation.RequestParam;
import com.jason.framework.netty.interceptor.Interceptor;
import com.jason.framework.netty.servlet.Request;
import com.jason.framework.netty.servlet.Response;
import com.jason.framework.session.ServerProtocol;
import com.jason.framework.session.Session;
import com.jason.mvc.view.ByteResult;
import com.jason.util.WrapperUtil;

public class ActionInvocation {

	protected String path;
	
	protected Method method;
	
	protected String methodName;
	
	protected Adaptor[] paramsAdaptors;
	
	/** 是否同步 */
	protected boolean syn;
	
	protected boolean needValidate;
	
	protected Object obj;
	
	private static final String CLASS_KEY = "class";
	
	public ActionInvocation(String path, Method method, boolean syn, Object obj) {
		this.path = path;
		this.method = method;
		this.methodName = method.getName();
		this.syn = syn;
		this.obj = obj;
	}
	

	public void init() throws Exception {
		initParam();
	}
	
	public Object invoke(Iterator<Interceptor> iterator, Request request, Response response) throws Exception {
		Session session = request.getSession(false);
		if (syn && session != null) {
			synchronized(session) {
				return _invoke(iterator, request, response);
			}
		}else {
			return _invoke(iterator, request, response);
		}
	}

	protected Object _invoke(Iterator<Interceptor> interceptor, Request request, Response response) throws Exception {
		Object[] params = adapt(request, response);
		// 拦截器添加
		if(interceptor == null || !interceptor.hasNext()) {
			ByteResult result = (ByteResult) method.invoke(obj, params);
			return result;
		}
		Object object = interceptor.next().interceptor(interceptor, this, request, response);
		return object;
	}

	protected void initParam() {
		Annotation[][] annoArr = method.getParameterAnnotations();
		Class<?>[] parameterTypes = method.getParameterTypes();
		paramsAdaptors = new Adaptor[parameterTypes.length];
		for (int count = 0; count < parameterTypes.length; count++) {
			Annotation[] anns = annoArr[count];
			RequestParam requestParam = null;
			
			for (Annotation ann : anns) {
				if (ann instanceof RequestParam) {
					requestParam = (RequestParam) ann;
					break;
				}
			}
			
			if (requestParam != null) {
				paramsAdaptors[count] = new RequestParamAdaptor(requestParam.value(), parameterTypes[count]);
			}else {
				if(Request.class.isAssignableFrom(parameterTypes[count])) {
					paramsAdaptors[count] = new RequestAdaptor(parameterTypes[count]);
				}
				if(Response.class.isAssignableFrom(parameterTypes[count])) {
					paramsAdaptors[count] = new ResponseAdaptor(parameterTypes[count]);
				}
			}
			
		}

	}
	
	private Object[] adapt(Request request, Response response) {
		if (paramsAdaptors.length <= 0) {
			return new Object[] {};
		}
		Object[] parameters = new Object[paramsAdaptors.length];
		for (int i = 0; i < paramsAdaptors.length; i++) {
			parameters[i] = paramsAdaptors[i].get(request, response);
		}
		return parameters;
	}
	
	public String toString() {
		return path + '.' + methodName;
	}

	public static void render(byte[] result, Request request, Response response) throws Exception {
		if (ServerProtocol.TCP.equals(response.getProtocol())) {
			response.write(WrapperUtil.wrapper(request.getCommand(), request.getRequestId(), result));
		} else if (ServerProtocol.HTTP.equals(response.getProtocol())) {
//			response.write(WrapperUtil.wrapper(request.getCommand(), request.getRequestId(), result));
			response.write(result);
		} else if (ServerProtocol.WEBSOCKET.equals(response.getProtocol())) {
			response.write(WrapperUtil.wrapperWebSocketTextFrame(request.getCommand(), request.getRequestId(), result));
		}
	}
	
	public void setNeedSkipDefault(boolean needSkip) {
		this.needValidate = needSkip;
	}

	public String getMethodName() {
		return methodName;
	}

//	// 针对对象数组，是DTO[]的过滤class字段
//    private void removeKey(Object[] array, String key, SameObjectChecker objs) {
//        if (array != null && objs.checkThenPush(array)) {
//            Class<?> componentClass = array.getClass().getComponentType();
//            if (componentClass != null && componentClass.isAssignableFrom(Map.class)) {
//                for (Object object : array)
//                    removeKey((Map<?, ?>) object, key, objs);
//            }
//            objs.pop();
//        }
//    }

//    private void removeKey(Map<?, ?> map, String key, SameObjectChecker objs) {
//        if (map != null && objs.checkThenPush(map)) {
//            map.remove(key);
//            for (Map.Entry<?, ?> entry : map.entrySet()) {
//                Object value = entry.getValue();
//                if (value instanceof Map<?, ?>) {
//                    removeKey((Map<?, ?>) value, key, objs);
//                } else if (value instanceof Collection<?>) {
//                    removeKey((Collection<?>) value, key, objs);
//                } else if (value instanceof Object[]) {
//                    removeKey((Object[]) value, key, objs);
//                } 
//            }
//            objs.pop();
//        }
//    }

//    private void removeKey(Collection<?> collection, String key, SameObjectChecker objs) {
//        if (collection != null && objs.checkThenPush(collection)) {
//            for (Object value : collection) {
//                if (value instanceof Map<?, ?>) {
//                    removeKey((Map<?, ?>) value, key, objs);
//                } else if (value instanceof Collection<?>) {
//                    removeKey((Collection<?>) value, key, objs);
//                } else if (value instanceof Object[]) {
//                    removeKey((Object[]) value, key, objs);
//                }
//            }
//            objs.pop();
//        }
//    }
	
}

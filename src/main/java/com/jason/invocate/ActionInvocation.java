package com.jason.invocate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;

import com.alibaba.fastjson.JSON;
import com.jason.adaptor.Adaptor;
import com.jason.annotation.RequestParam;
import com.jason.common.RequestParamAdaptor;
import com.jason.mvc.view.Result;
import com.jason.servlet.Request;
import com.jason.servlet.Response;
import com.jason.session.ServerProtocol;
import com.jason.util.WrapperUtil;

public class ActionInvocation {

	protected String path;
	
	protected Method method;
	
	protected String methodName;
	
	protected Adaptor[] paramsAdaptors;
	
	/** 是否同步 */
	protected boolean syn;
	
	protected boolean needValidate;
	
	protected List<Rule> validateInterceptor;
	
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
	
	public byte[] invoke(Request request, Response response) throws Exception {
		if (syn) {
			synchronized(request.getSession()) {
				return _invoke(request, response);
			}
		}else {
			return _invoke(request, response);
		}
	}

	protected byte[] _invoke(Request request, Response response) throws Exception {
		
		Object[] params = adapt(request);
		Result result = (Result) method.invoke(obj, params);
		return dealResult(result);
	}

	private byte[] dealResult(Object result) {
		if (result == null) {
			return null;
		}
//		if (result instanceof Map<?, ?>) {
//            removeKey((Map<?, ?>) result, CLASS_KEY, new SameObjectChecker());
//        } else if (result instanceof Collection<?>) {
//            removeKey((Collection<?>) result, CLASS_KEY, new SameObjectChecker());
//        } else if (result instanceof Object[]) {
//            removeKey((Object[]) result, CLASS_KEY, new SameObjectChecker());
//        }
		
		return JSON.toJSONBytes(result);
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
			
			if (requestParam == null) {
				continue;
			}
			
			if (requestParam != null) {
				paramsAdaptors[count] = new RequestParamAdaptor(requestParam.value(), parameterTypes[count]);
			}
			
		}

	}
	
	private Object[] adapt(Request request) {
		if (paramsAdaptors.length <= 0) {
			return new Object[] {};
		}
		Object[] parameters = new Object[paramsAdaptors.length];
		for (int i = 0; i < paramsAdaptors.length; i++) {
			parameters[i] = paramsAdaptors[i].get(request);
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
			// TODO:HTTP
//			response.write(WrapperUtil.wrapper(request.getCommand(), request.getRequestId(), result));
			response.write(result);
		} else if (ServerProtocol.WEBSOCKET.equals(response.getProtocol())) {
			// TODO:WEB SOCKET
			response.write(WrapperUtil.wrapperWebSocketTextFrame(request.getCommand(), request.getRequestId(), result));
		}
	}
	
	public void addInterceptor(Rule rule) {
		if (validateInterceptor == null) {
			validateInterceptor = new ArrayList<Rule>();
		}
		validateInterceptor.add(rule);
	}
	
	public void setNeedSkipDefault(boolean needSkip) {
		this.needValidate = needSkip;
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

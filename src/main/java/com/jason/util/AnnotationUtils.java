package com.jason.util;

import java.lang.annotation.Annotation;

public class AnnotationUtils {

	public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> anClass) {
		Class<?> cc = clazz;
		T annotation = null;
		
		while(cc != null && cc != Object.class) {
			annotation = cc.getAnnotation(anClass);
			if(annotation != null) {
				return annotation;
			}
			cc = cc.getSuperclass();
		}
		return null;
	}
	
}

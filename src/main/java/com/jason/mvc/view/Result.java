package com.jason.mvc.view;

public interface Result<T> {

	String getViewName();
	
	T getResult();
	
}

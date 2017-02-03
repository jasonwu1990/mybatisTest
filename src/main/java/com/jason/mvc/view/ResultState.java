package com.jason.mvc.view;

public enum ResultState {
	SUCCESS(1),
	FAIL(0),
	EXCEPTION(2),
	PUSH(3);
	
	private int value;
	
	private ResultState(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
}



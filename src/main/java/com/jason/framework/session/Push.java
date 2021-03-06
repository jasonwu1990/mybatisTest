package com.jason.framework.session;

public interface Push {
	
	void push(Session session, String command, byte[] body);
	
	boolean isPushable();
	
	void clear();
	
	void discard();
	
	void heartbeat();
	
	ServerProtocol getPushProtocol();
	
}

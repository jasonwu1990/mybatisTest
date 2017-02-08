package com.jason.framework.session.listener;

import java.util.EventListener;

import com.jason.framework.session.event.SessionEvent;

public interface SessionListener extends EventListener {
	
	public void sessionCreated(SessionEvent se);
	
	public void sessionDestoryed (SessionEvent se);
	
}

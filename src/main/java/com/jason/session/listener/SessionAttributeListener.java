package com.jason.session.listener;

import com.jason.session.event.SessionAttributeEvent;

public interface SessionAttributeListener {

	public void attributeAdd(SessionAttributeEvent event);
	
	public void attributeRemoved(SessionAttributeEvent event);
	
	public void attributeReplaced(SessionAttributeEvent event);
	
}

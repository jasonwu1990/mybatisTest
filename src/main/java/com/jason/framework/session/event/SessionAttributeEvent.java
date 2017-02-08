package com.jason.framework.session.event;

import com.jason.framework.session.Session;
/**
 * Session属性事件
 * @author kira
 *
 */
public class SessionAttributeEvent extends SessionEvent {
	
	public String key;
	
	public Object value;
	
	public SessionAttributeEvent(String key, Object value, Session session) {
		super(session);
		this.key = key;
		this.value = value;
	}
}

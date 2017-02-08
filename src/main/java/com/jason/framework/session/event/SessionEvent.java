package com.jason.framework.session.event;

import com.jason.framework.session.Session;

/**
 * Session事件
 * @author kira
 *
 */
public class SessionEvent {

	public Session session;

	public SessionEvent(Session session) {
		this.session = session;
	}
	
}

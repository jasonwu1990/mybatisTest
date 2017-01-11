package com.jason.session.event;

import com.jason.session.Session;

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

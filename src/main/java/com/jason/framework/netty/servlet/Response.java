package com.jason.framework.netty.servlet;

import java.io.IOException;
import java.util.Map;

import com.jason.framework.session.ServerProtocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.cookie.Cookie;

public interface Response {
	
	public Channel getChannel();
	
	public boolean isWritable();
	
	public ChannelFuture write(Object obj) throws IOException;
	
	public ServerProtocol getProtocol();
	
	public void addCookie(Cookie cookie);
	
	public Map<String, Cookie> getCookies();
	
	public Map<String, String> getHeaders();
	
	public void addHeader(String name, String value);
	
	public byte[] getContent();
	
	public void setStatus(HttpResponseStatus status);
	
	public HttpResponseStatus getStatus();
	
	public void markClose();
	
}

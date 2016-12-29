package com.jason.servlet;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;

import com.jason.session.ServerProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.cookie.Cookie;

public interface Request {

	public Map<String, Object> getParamMap();
	
	public String getCommand();
	
	public Channel getChannel();
	
	public int getRequestId();
	
	public ServerProtocol getProtocol();
	
	public String getHeader(String key);
	
	public String getCookieValue(String key);
	
	public Collection<Cookie> getCookies();
	
	public boolean isLongHttp();
	
	public InetSocketAddress getRemoteAddress();
	
	public byte[] getContent();
	
	public void pushAndClose(ByteBuf buffer);
	
	public String getIp();
}

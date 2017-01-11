package com.jason.servlet.http;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;

import com.jason.servlet.Request;
import com.jason.servlet.Response;
import com.jason.session.ServerProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.cookie.Cookie;

public class HttpRequest implements Request{

	private ChannelHandlerContext ctx;
	
	private Map<String, Object> paramMap;
	
	private String command;
	
	private Channel channel;
	
	private int requestId;
	
	private Map<String, Cookie> cookies;
	
	private Map<String, String> headers;
	
	private Response response;
	
	private byte[] postContent;
	
	private byte[] getContent;
	
	private volatile boolean getParse;
	
	private volatile boolean postParse;
	
	private String url;
	
	private boolean longHttp = false;
	
	public HttpRequest(ChannelHandlerContext ctx,
			byte[] getContent, byte[] postContent,
			String command,
			Map<String, Cookie> cookies,
			Map<String, String> headers,
			Response response, String uri) {
		this.ctx = ctx;
		this.getContent = getContent;
		this.postContent = postContent;
		this.command = command;
		this.cookies = cookies;
		this.headers = headers;
		this.response = response;
		this.url = uri;
	}
	
	
	@Override
	public Map<String, Object> getParamMap() {
		return null;
	}

	@Override
	public String getCommand() {
		return null;
	}

	@Override
	public Channel getChannel() {
		return null;
	}

	@Override
	public int getRequestId() {
		return 0;
	}

	@Override
	public ServerProtocol getProtocol() {
		return null;
	}

	@Override
	public String getHeader(String key) {
		return null;
	}

	@Override
	public String getCookieValue(String key) {
		return null;
	}

	@Override
	public Collection<Cookie> getCookies() {
		return null;
	}

	@Override
	public boolean isLongHttp() {
		return false;
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return null;
	}

	@Override
	public byte[] getContent() {
		return null;
	}

	@Override
	public void pushAndClose(ByteBuf buffer) {
		
	}

	@Override
	public String getIp() {
		return null;
	}

}

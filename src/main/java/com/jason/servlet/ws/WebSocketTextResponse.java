package com.jason.servlet.ws;

import java.io.IOException;
import java.util.Map;

import com.jason.servlet.Response;
import com.jason.session.ServerProtocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.cookie.Cookie;

public class WebSocketTextResponse implements Response {

	/** channel */
	private Channel channel;
	
	/** 是否关闭 */
	private boolean close = false;
	
	public WebSocketTextResponse(Channel channel) {
		this.channel = channel;
	}

	@Override
	public Channel getChannel() {
		return channel;
	}

	@Override
	public boolean isWritable() {
		return channel != null && channel.isWritable();
	}

	@Override
	public ChannelFuture write(Object obj) throws IOException {
		if (isWritable()) {
			ChannelFuture future = channel.write(obj);
			if (close) {
				future.addListener(ChannelFutureListener.CLOSE);
			}
		}
		return null;
	}

	@Override
	public ServerProtocol getProtocol() {
		return ServerProtocol.WEBSOCKET;
	}

	@Override
	public void addCookie(Cookie cookie) {
		throw new UnsupportedOperationException("ws response can not support this operation!");		
	}

	@Override
	public Map<String, Cookie> getCookies() {
		throw new UnsupportedOperationException("ws response can not support this operation!");
	}

	@Override
	public Map<String, String> getHeaders() {
		throw new UnsupportedOperationException("ws response can not support this operation!");
	}

	@Override
	public void addHeader(String name, String value) {
		throw new UnsupportedOperationException("ws response can not support this operation!");
	}

	@Override
	public byte[] getContent() {
		throw new UnsupportedOperationException("ws response can not support this operation!");
	}

	@Override
	public void setStatus(HttpResponseStatus status) {
		throw new UnsupportedOperationException("ws response can not support this operation!");		
	}

	@Override
	public HttpResponseStatus getStatus() {
		throw new UnsupportedOperationException("ws response can not support this operation!");
	}

	@Override
	public void markClose() {
		this.close = true;
	}

}

package com.jason.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.jason.servlet.Request;
import com.jason.servlet.Response;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;

public class HttpUtil {

	private static final String WEBSOCKET_PATH = "/websocket";

	public static boolean isWebSocketRequest(HttpRequest req) {
		return HttpHeaderValues.UPGRADE.toString().equalsIgnoreCase(req.headers().get(HttpHeaderNames.CONNECTION))
				|| HttpHeaderValues.WEBSOCKET.toString().equalsIgnoreCase(req.headers().get(HttpHeaderNames.UPGRADE));
	}

	public static String getWebSocketLocation(HttpRequest httpRequest) {
		return "ws://" + httpRequest.headers().get(HttpHeaderNames.HOST) + WEBSOCKET_PATH;
	}

	public static Tuple<byte[], byte[]> getRequestContent(FullHttpRequest httpRequest) {
		Tuple<byte[], byte[]> tuple = new Tuple<byte[], byte[]>();
		// GET
		String uri = httpRequest.uri();
		if (!uri.endsWith(".action")) {
			String params = uri.substring(uri.indexOf(".action") +  8);
			tuple.left = params.getBytes();
		}

		// POST
		if (httpRequest.method().equals(HttpMethod.POST)) {
			// 获取消息内容
			ByteBuf b = httpRequest.content();
			byte[] body = new byte[b.readableBytes()];
			httpRequest.content().getBytes(b.readerIndex(), body, 0, body.length);
			tuple.right = body;
		}

		return tuple;
	}

	public static Map<String, Cookie> getCookies(FullHttpRequest httpRequest) {
		Map<String, Cookie> cookies = new HashMap<String, Cookie>(16);
		String value = httpRequest.headers().get(HttpHeaderNames.COOKIE);
		if (value != null) {
			Set<Cookie> cookieSet = ServerCookieDecoder.STRICT.decode(value);
			if (cookieSet != null) {
				for (Cookie cookie : cookieSet) {
					Cookie temp = new DefaultCookie(cookie.name(), cookie.value());
					temp.setPath(cookie.path());
					temp.setDomain(cookie.domain());
					temp.setSecure(cookie.isSecure());
					temp.setHttpOnly(cookie.isHttpOnly());

					cookies.put(temp.name(), temp);
				}
			}
		}
		return cookies;
	}

	public static Map<String, String> getHeaders(FullHttpRequest httpRequest) {
		Map<String, String> headers = new HashMap<String, String>(16);
		for (Entry<String, String> entry : httpRequest.headers()) {
			headers.put(entry.getKey(), entry.getValue());
		}
		return headers;
	}

	public static void doResponse(ChannelHandlerContext ctx, Request request, Response response, HttpRequest httpRequest) throws Exception {
		FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		httpResponse.headers().add(HttpHeaderNames.CONTENT_TYPE, WrapperUtil.getContentType());

		final boolean keepAlive = isKeepAlive(httpRequest);
		if (keepAlive && httpRequest.protocolVersion().equals(HttpVersion.HTTP_1_0)) {
			// http 1.0协议
			httpResponse.headers().add(HttpHeaderNames.CONNECTION, "Keep-Alive");
		}
		
		// copy 头和协议
		addHeadAndCookieToResponse(response, httpResponse);
		// 写回response
		writeResponse(ctx, response, httpResponse, httpRequest);

	}

	private static void writeResponse(ChannelHandlerContext ctx,
			Response response, FullHttpResponse httpResponse,
			HttpRequest httpRequest) {
		byte[] content = null;

		final boolean isKeepAlive = isKeepAlive(httpRequest);
		if (httpRequest.method().equals(HttpMethod.HEAD)) {
			content = new byte[0];
		}else {
			content = response.getContent();
		}
		System.out.println(new String(content));
		httpResponse.content().writeBytes(content);

		httpResponse.headers().add(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(content.length));
		httpResponse.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		httpResponse.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, true);
		httpResponse.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "POST,GET");

		ChannelFuture f = ctx.channel().writeAndFlush(httpResponse);

		if (!isKeepAlive) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private static void addHeadAndCookieToResponse(Response response,
			HttpResponse httpResponse) {
		Map<String, String> headers = response.getHeaders();
		if (null != headers) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpResponse.headers().add(entry.getKey(), entry.getValue());
			}
		}

		Map<String, Cookie> cookies = response.getCookies();
		if (null != cookies) {
			for (Map.Entry<String, Cookie> entry : cookies.entrySet()) {
				httpResponse.headers().add(HttpHeaderNames.SET_COOKIE, entry.getValue());
				System.out.println(entry.getValue());
			}
		}

		if (null != headers) {
			if (!headers.containsKey(HttpHeaderNames.CACHE_CONTROL) && !headers.containsKey(HttpHeaderNames.EXPIRES)) {
				httpResponse.headers().add(HttpHeaderNames.CACHE_CONTROL, "no-cache");
			}
		}

		httpResponse.setStatus(response.getStatus());
	}

	private static boolean isKeepAlive(HttpRequest httpRequest) {
		return io.netty.handler.codec.http.HttpUtil.isKeepAlive(httpRequest);
	}

}

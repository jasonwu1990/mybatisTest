package com.jason.servlet.http;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jason.common.ServerConstants;
import com.jason.invocate.InvocationFactory;
import com.jason.servlet.Request;
import com.jason.servlet.Response;
import com.jason.servlet.ws.WebSocketServerHandler;
import com.jason.util.HttpUtil;
import com.jason.util.Tuple;
import com.jason.util.WebUtils;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
/**
 * http服务器处理器
 * @author wufan
 *
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter { // (1)  
	  
	private InvocationFactory servlet;
	
	/** uri正则表达 */
	private static final Pattern pattern = Pattern.compile("^/root/([\\w-/]*)\\.action([\\s\\S]*)?$");
	
	public HttpServerHandler(InvocationFactory servlet) {
		this.servlet = servlet;
	}
	
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	System.out.println("http read!");
        if (msg instanceof FullHttpRequest) {
        	try {
        		final FullHttpRequest httpRequest = (FullHttpRequest) msg;
        		
        		// 处理跨域
        		if (httpRequest.uri().equalsIgnoreCase("/crossdomain.xml")) {
        			HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(ServerConstants.CROSSDOMAIN));
        			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF_8");
        			ctx.channel().write(response);
        			return;
        		}
        		
        		if (HttpUtil.isWebSocketRequest(httpRequest)) {
        			// websocket
        			WebSocketServerHandshakerFactory wsf = new WebSocketServerHandshakerFactory(HttpUtil.getWebSocketLocation(httpRequest), "default-protocol", false);
        			WebSocketServerHandshaker handshaker = wsf.newHandshaker(httpRequest);
        			if (handshaker == null) {
        				wsf.sendUnsupportedVersionResponse(ctx.channel());
        			}else {
        				handshaker.handshake(ctx.channel(), (FullHttpRequest) httpRequest);
        				
        				// 获取cookies
//        				Map<String, Cookie> cookies = HttpUtil.getCookies(httpRequest);
//        				String sessionId = ctx.attr(ServerConstants.SESSIONID).get();
        				
        				String ip = WebUtils.getIp(httpRequest, ctx.channel());
        				
        				ctx.channel().pipeline().replace(HttpServerHandler.class, "wshandler", new WebSocketServerHandler(servlet, handshaker, ip));
        			}
        			
        			return;
        		}
        		
        		String uri = httpRequest.uri();
        		Matcher matcher = pattern.matcher(uri);
        		
        		if (!matcher.find()) {
        			// 非正常请求
        			HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_GATEWAY);
        			ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        			return;
        		}
        		
        		// 获得命令
        		String command = matcher.group(1);
        		
        		// 获取参数
        		Tuple<byte[], byte[]> requestContent = HttpUtil.getRequestContent(httpRequest);
        		
        		// 获取cookie
        		Map<String, Cookie> cookies = HttpUtil.getCookies(httpRequest);
        		
        		// 获取head
        		Map<String, String> headers = HttpUtil.getHeaders(httpRequest);
        		
        		// 解析消息
        		final Response response = new HttpRespone(ctx.channel());
        		
        		final Request request = new HttpRequest(ctx, httpRequest, requestContent.left, requestContent.right, command, cookies, headers, response, uri);
        		
        		if (!request.isLongHttp()) {
        			// 处理请求
        			servlet.service(request, response);
        			// 响应
        			HttpUtil.doResponse(ctx, request, response, httpRequest);
        		}
        		
        	}catch (Exception e) {
        		e.printStackTrace();
        		throw e;
//        		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
//        		ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        	}
        }
    }
    
    @Override  
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {  
        System.out.println("read complete!");
        ctx.flush();  
    }  
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
//    	System.out.println("chanel actived "+ctx.name());
//    	String sessionId = ctx.channel().attr(ServerConstants.SESSIONID).get();
//    	if (sessionId == null) {
//    		Session session = SessionManager.getInstance().getSession(null, true);
//    		session.setPush(new TcpPush(ctx.channel()));
//    		ctx.channel().attr(ServerConstants.SESSIONID).set(session.getId());
//    		System.err.println("session create "+session.id);
//    	}
    }
  
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)  
        // Close the connection when an exception is raised.  
        cause.printStackTrace();  
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        if (cause instanceof TooLongFrameException) {
        	response.content().writeBytes("Too long frame".getBytes());
        }
		ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        ctx.close();  
    }
} 
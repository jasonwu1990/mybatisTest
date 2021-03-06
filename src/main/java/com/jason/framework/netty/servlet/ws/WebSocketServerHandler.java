package com.jason.framework.netty.servlet.ws;

import java.io.UnsupportedEncodingException;
import java.nio.channels.ClosedChannelException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.jason.framework.common.ServerConstants;
import com.jason.framework.netty.invocate.InvocationFactory;
import com.jason.framework.netty.protocol.RequestMessage;
import com.jason.framework.netty.servlet.Request;
import com.jason.framework.netty.servlet.Response;
import com.jason.util.RequestUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

public class WebSocketServerHandler extends ChannelInboundHandlerAdapter{

	private final InvocationFactory servlet;
	
	private WebSocketServerHandshaker handshaker;
	
	private String ip;
	
	public WebSocketServerHandler(InvocationFactory servlet, WebSocketServerHandshaker handshaker, String ip) {
		this.servlet = servlet;
		this.handshaker = handshaker;
		this.ip = ip;
	}
	
	@Override  
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		if (! (cause instanceof ClosedChannelException)) {
			System.out.println("websocket error channel, channel name="+ctx.name());
		}
	}
	
	@Override
	public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof WebSocketFrame) {
			WebSocketFrame frame = (WebSocketFrame) msg;
			if (frame instanceof CloseWebSocketFrame) {
				this.handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame);
			} else if (frame instanceof PingWebSocketFrame) {
				ctx.channel().write(new PongWebSocketFrame(frame.content()));
			} else if (frame instanceof BinaryWebSocketFrame) {
				// 二进制协议
				BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame) frame;
				RequestMessage message = decodeBinaryData(binaryFrame.content());
//				message.setSessionId(ctx.channel().attr(ServerConstants.SESSIONID).get());
				
//				Response response = new WebSocketResponse(ctx.channel(), message);
//				Request request = new WebSocketRequest(ctx, message, ip, true);
				
//				servlet.service(request, response);
			} else if (frame instanceof TextWebSocketFrame) {
				TextWebSocketFrame txtFrame = (TextWebSocketFrame)frame;
				RequestMessage message = decodeTextData(txtFrame.text());
				
				Response response = new WebSocketResponse(ctx.channel(), message);
				Request request = new WebSocketRequest(ctx, message, ip, true);
				
				servlet.service(request, response);
			} else {
				this.handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame);
			}
		}
	}
	
	private RequestMessage decodeBinaryData(ByteBuf buffer) {
		RequestMessage r = new RequestMessage();
		r.setCommand(new String(buffer.readBytes(32).array()).trim());
		r.setRequestId(buffer.readInt());
		r.setContent(buffer.readBytes(buffer.readableBytes()).array());
		return r;
	}
	
	private RequestMessage decodeTextData(String text) throws UnsupportedEncodingException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		RequestUtil.parseParam(text, paramMap);
		
		RequestMessage r = new RequestMessage();
		r.setCommand(((String[])paramMap.get("command"))[0]);
		String requestId = ((String[]) paramMap.get("requestId"))[0];
		if (!StringUtils.isEmpty(requestId)) {
			r.setSessionId(requestId);
		}
		r.setContent(text.getBytes());
		return r;
	}
}

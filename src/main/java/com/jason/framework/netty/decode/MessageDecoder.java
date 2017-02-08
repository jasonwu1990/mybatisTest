package com.jason.framework.netty.decode;

import java.util.List;

import com.jason.framework.netty.protocol.RequestMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MessageDecoder extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// 可读字节数
		if(in.readableBytes() < 4) {
			// 小于4字节为无用包
			return;
		}
		int datalen = in.getInt(in.readerIndex());
		if(in.readableBytes() < datalen + 4) {
			return;
		}
		// 跳过头
		in.skipBytes(4);
		RequestMessage requestMessage = new RequestMessage();
		byte[] commandBytes = new byte[32];
		in.readBytes(commandBytes);
		requestMessage.setCommand(new String(commandBytes));
		requestMessage.setRequestId(in.readInt());
		byte[] contentBytes = new byte[datalen-36];
		in.readBytes(contentBytes);
		requestMessage.setContent(contentBytes);
		out.add(requestMessage);
	}

}

package org.onetwo.app.posserver.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class PosMessageDecoder extends LengthFieldBasedFrameDecoder{

	public PosMessageDecoder(int maxFrameLength, int lengthFieldOffset,int lengthFieldLength) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf frame = (ByteBuf) super.decode(ctx, in);
		return null;
	}
	

}

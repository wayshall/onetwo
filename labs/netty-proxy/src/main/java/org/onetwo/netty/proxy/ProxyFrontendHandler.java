package org.onetwo.netty.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;

//@Sharable
public class ProxyFrontendHandler extends ChannelInboundHandlerAdapter {

	private String remoteHost;
	private int remotePort;
	
	private volatile Channel outboundChannel;
	
	public ProxyFrontendHandler(String remoteHost, int remotePort) {
		super();
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		final Channel inboundChannel = ctx.channel();
		
		Bootstrap remoteServer = new Bootstrap();
		remoteServer.group(inboundChannel.eventLoop())
					.channel(ctx.channel().getClass())
					.handler(new ProxyBackendHandler(inboundChannel))
					.option(ChannelOption.AUTO_READ, false);
		
		ChannelFuture cf = remoteServer.connect(remoteHost, remotePort);
		outboundChannel = cf.channel();
		cf.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if(future.isSuccess()){
					inboundChannel.read();
				}else{
					inboundChannel.close();
				}
			}
		});
					
	}

	@Override
	public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
		if(outboundChannel.isActive()){
			outboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
				
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if(future.isSuccess()){
						ctx.channel().read();
					}else{
						future.channel().close();
					}
				}
			});
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if(outboundChannel!=null){
			if(outboundChannel.isActive()){
				outboundChannel.writeAndFlush(Unpooled.EMPTY_BUFFER)
								.addListener(ChannelFutureListener.CLOSE);
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		if(ctx.channel().isActive()){
			ctx.channel().writeAndFlush(Unpooled.EMPTY_BUFFER)
							.addListener(ChannelFutureListener.CLOSE);
		}
	}

	
}

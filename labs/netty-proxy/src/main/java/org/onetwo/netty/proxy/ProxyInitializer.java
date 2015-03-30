package org.onetwo.netty.proxy;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ProxyInitializer extends ChannelInitializer<SocketChannel>{
	
	private String remoteHost;
	private int remotePort;
	
	public ProxyInitializer(String remoteHost, int remotePort) {
		super();
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO),
								new ProxyFrontendHandler(remoteHost, remotePort));
	}
	

}

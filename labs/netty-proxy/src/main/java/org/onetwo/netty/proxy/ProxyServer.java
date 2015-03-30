package org.onetwo.netty.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;

public class ProxyServer {
	
	private final static Logger logger = JFishLoggerFactory.getLogger(ProxyServer.class);
	public static void main(String[] args) throws Exception{
		int localPort = 8888;
		String remoteHost = "192.168.104.217";
		int remotePort = 8081;
		
		LangUtils.println("proxy ${0} to ${1}:${2} ...", localPort, remoteHost, remotePort);
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap boot = new ServerBootstrap();
			boot.group(bossGroup, workGroup)
			.channel(NioServerSocketChannel.class)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ProxyInitializer(remoteHost, remotePort))
			.childOption(ChannelOption.AUTO_READ, false)
			.bind(localPort).sync()
			.channel().closeFuture().sync();
			
		}finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
			
	}

}

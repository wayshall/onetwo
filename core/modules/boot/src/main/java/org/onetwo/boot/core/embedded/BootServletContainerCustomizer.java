package org.onetwo.boot.core.embedded;

import java.util.Arrays;

import org.apache.coyote.http11.Http11NioProtocol;
import org.onetwo.common.file.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;


/**
 * 
因为只配置下面这个not work:
spring.http.multipart -> MultipartProperties

所以增加自定义容器，设置server最大上传
根据spring.http.multipart自动设置内嵌tomcat的最大postsize
也可以自行配置server.maxHttpPostSize
server: 
	maxHttpPostSize: 10*1024*1024
serverProperties#maxHttpPostSize
ServerProperties#Tomcat#customizeMaxHttpPostSize


 * @author wayshall
 * <br/>
 */
public class BootServletContainerCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> /*EmbeddedServletContainerCustomizer*/  {
	
	@Autowired
	private MultipartProperties multipartProperties;
	@Autowired
	private TomcatProperties tomcatProperties;
	
	/***
	 * upgrade-sb2: 
	 * 
	 */
	@Override
	public void customize(TomcatServletWebServerFactory factory) {
		factory.setTomcatConnectorCustomizers(Arrays.asList(tomcatConnectorCustomizer()));
//		factory.setTomcatContextCustomizers(Arrays.asList(tomcatContextCustomizer()));
	}

	protected TomcatConnectorCustomizer tomcatConnectorCustomizer() {
		return (connector) -> {
        	//connector 本身默认是 2 mb
        	connector.setMaxPostSize(FileUtils.parseSize(multipartProperties.getMaxRequestSize()));
        	Http11NioProtocol handler = (Http11NioProtocol)connector.getProtocolHandler();
        	if(tomcatProperties.getBacklog()!=-1){
        		//socket 连接队列大小
//        		handler.setBacklog(tomcatProperties.getBacklog());
        		handler.setAcceptCount(tomcatProperties.getAcceptCount());
        	}
        	if(tomcatProperties.getMaxConnections()!=-1){
        		//最大连接数，默认10000
        		handler.setMaxConnections(tomcatProperties.getMaxConnections());
        	}
        	if(tomcatProperties.getConnectionTimeout()!=-1){
        		handler.setConnectionTimeout(tomcatProperties.getConnectionTimeout());
        	}
        	if(tomcatProperties.getConnectionUploadTimeout()>0){
        		//为true，则上传文件时使用connectionTimeout, 为false，则使用connectionUploadTimeout
        		handler.setDisableUploadTimeout(false);
        		handler.setConnectionUploadTimeout(tomcatProperties.getConnectionUploadTimeout());
        	}
        	connector.setAsyncTimeout(tomcatProperties.getAsyncTimeout());
        };
	}
	
	protected TomcatContextCustomizer tomcatContextCustomizer() {
		return context -> context.setReloadable(true);
	}

	/*@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		if (container instanceof TomcatEmbeddedServletContainerFactory) {
            TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
            tomcat.addConnectorCustomizers(
                (connector) -> {
                	//connector 本身默认是 2 mb
                	connector.setMaxPostSize(FileUtils.parseSize(multipartProperties.getMaxRequestSize()));
                	Http11NioProtocol handler = (Http11NioProtocol)connector.getProtocolHandler();
                	if(tomcatProperties.getBacklog()!=-1){
                		//socket 连接队列大小
//                		handler.setBacklog(tomcatProperties.getBacklog());
                		handler.setAcceptCount(tomcatProperties.getAcceptCount());
                	}
                	if(tomcatProperties.getMaxConnections()!=-1){
                		//最大连接数，默认10000
                		handler.setMaxConnections(tomcatProperties.getMaxConnections());
                	}
                	if(tomcatProperties.getConnectionTimeout()!=-1){
                		handler.setConnectionTimeout(tomcatProperties.getConnectionTimeout());
                	}
                	if(tomcatProperties.getConnectionUploadTimeout()>0){
                		//为true，则上传文件时使用connectionTimeout, 为false，则使用connectionUploadTimeout
                		handler.setDisableUploadTimeout(false);
                		handler.setConnectionUploadTimeout(tomcatProperties.getConnectionUploadTimeout());
                	}
                	connector.setAsyncTimeout(tomcatProperties.getAsyncTimeout());
                }
            );
        }
		if(container instanceof TomcatEmbeddedServletContainerFactory){
			TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
			tomcat.addContextCustomizers(context->{
				context.setReloadable(true);
			});
		}
	}*/

	
	

}

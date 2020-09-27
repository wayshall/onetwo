package org.onetwo.boot.core.embedded;

import org.apache.catalina.core.AprLifecycleListener;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.onetwo.common.file.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.MultipartProperties;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;


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
public class BootServletContainerCustomizer implements EmbeddedServletContainerCustomizer {
	
	@Autowired
	private MultipartProperties multipartProperties;
	@Autowired
	private TomcatProperties tomcatProperties;

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		if (container instanceof TomcatEmbeddedServletContainerFactory) {
            TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
            if (tomcatProperties.isAprProtocol()) {
            	tomcat.setProtocol("org.apache.coyote.http11.Http11AprProtocol");
            	tomcat.addContextLifecycleListeners(new AprLifecycleListener());
            	
            }
            tomcat.addConnectorCustomizers(
                (connector) -> {
                	//connector 本身默认是 2 mb
                	connector.setMaxPostSize(FileUtils.parseSize(multipartProperties.getMaxRequestSize()));
                	AbstractHttp11Protocol<?> handler = (AbstractHttp11Protocol<?>)connector.getProtocolHandler();
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
		/*if(container instanceof TomcatEmbeddedServletContainerFactory){
			TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
			tomcat.addContextCustomizers(context->{
				context.setReloadable(true);
			});
		}*/
	}

	
	

}

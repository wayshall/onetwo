package org.onetwo.boot.core.embedded;

import org.apache.coyote.http11.Http11NioProtocol;
import org.onetwo.common.file.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.MultipartProperties;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;


/**
 * 
设置server最大上传
根据spring.http.multipart自动设置内嵌tomcat的最后postsize
也可以自行配置server.maxHttpPostSize
server: 
	maxHttpPostSize: 10*1024*1024
serverProperties#maxHttpPostSize
ServerProperties#Tomcat#customizeMaxHttpPostSize

只配置下面这个not work:
spring.http.multipart -> MultipartProperties

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
            tomcat.addConnectorCustomizers(
                (connector) -> {
                	//connector 本身默认是 2 mb
                	connector.setMaxPostSize(FileUtils.parseSize(multipartProperties.getMaxRequestSize()));
                	Http11NioProtocol handler = (Http11NioProtocol)connector.getProtocolHandler();
                	if(tomcatProperties.getBacklog()!=-1){
                		//socket 连接队列大小
                		handler.setBacklog(tomcatProperties.getBacklog());
                	}
                	if(tomcatProperties.getMaxConnections()!=-1){
                		//最大连接数，默认10000
                		handler.setMaxConnections(tomcatProperties.getMaxConnections());
                	}
                	if(tomcatProperties.getConnectionTimeout()!=-1){
                		handler.setConnectionTimeout(tomcatProperties.getConnectionTimeout());
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

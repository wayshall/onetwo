package org.onetwo.boot.core;

import java.io.File;
import java.lang.management.ManagementFactory;

import javax.servlet.MultipartConfigElement;

import org.onetwo.boot.core.web.mvc.BootStandardServletMultipartResolver;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;

/**
 * 一些修改boot默认配置的配置
 * @author wayshall
 * <br/>
 */
@Configuration
@EnableConfigurationProperties(MultipartProperties.class)
public class BootFixedConfiguration {
	private static final String DEFAULT_TMP_DIR = "/data/tmp";
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MultipartProperties multipartProperties;
	@Autowired
	private ServerProperties serverProperties;
	

	/*@Bean
	@ConditionalOnProperty(value="maxHttpPostSize", prefix="server", matchIfMissing=true, havingValue="auto")
	public BootServletContainerCustomizer bootServletContainerCustomizer(){
		return new BootServletContainerCustomizer();
	}*/
	
	@Bean(name=MultipartFilter.DEFAULT_MULTIPART_RESOLVER_BEAN_NAME)
	public MultipartResolver filterMultipartResolver(){
		BootStandardServletMultipartResolver resolver = new BootStandardServletMultipartResolver();
		resolver.setMaxUploadSize(FileUtils.parseSize(multipartProperties.getMaxRequestSize()));
		return resolver;
	}
	
	@Bean
	@ConditionalOnMissingBean
	public MultipartConfigElement multipartConfigElement() {
		String location = multipartProperties.getLocation();
		if(StringUtils.isBlank(location)){
			location = DEFAULT_TMP_DIR;
			location = location + "/sb.tomcat."+serverProperties.getPort();
			String pid = getPid();
			if(StringUtils.isNotBlank(pid)){
				location = location + ".pid" + pid;
			}
			try {
				File dir = new File(FileUtils.convertDir(location));
				if(!dir.exists()){
					if(dir.mkdirs()){
						dir.deleteOnExit();
						this.multipartProperties.setLocation(dir.getPath());
					}
				}else{
					this.multipartProperties.setLocation(dir.getPath());
				}
			} catch (Exception e) {
				logger.error("create multipart temp location error: " + e.getMessage());
			}
		}
		if(logger.isInfoEnabled()){
			logger.info("multipart temp location: {}", this.multipartProperties.getLocation());
		}
		return this.multipartProperties.createMultipartConfig();
	}
	
	private String getPid() {
		try {
			String jvmName = ManagementFactory.getRuntimeMXBean().getName();
			return jvmName.split("@")[0];
		}
		catch (Throwable ex) {
			return null;
		}
	}
}

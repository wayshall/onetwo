package org.onetwo.boot.module.alioss;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aliyun.oss.OSSClient;

/**
 * @author wayshall
 * <br/>
 */
@ConditionalOnClass(OSSClient.class)
@Configuration
@EnableConfigurationProperties(OssProperties.class)
public class OssConfiguration {
	
	@Autowired
	private OssProperties ossProperties;

	@Bean
	@ConditionalOnProperty(name=BootSiteConfig.ENABLE_STORETYPE_PROPERTY, havingValue="alioss")
	public OssFileStore ossFileStore(@Autowired OssClientWrapper wrapper){
		OssFileStore storer = new OssFileStore(wrapper, ossProperties);
		return storer;
	}
	
	@Bean
	@ConditionalOnProperty(name=BootSiteConfig.ENABLE_STORETYPE_PROPERTY, havingValue="alioss")
	public OssClientWrapper OssClientWrapper(){
		OssClientWrapper wrapper = new OssClientWrapper(ossProperties);
		wrapper.setClinetConfig(ossProperties.getClient());
		return wrapper;
	}
}

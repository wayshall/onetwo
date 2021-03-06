package org.onetwo.boot.module.alioss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.aliyun.oss.OSSClient;

/**
 * @author wayshall
 * <br/>
 */
@ConditionalOnClass(OSSClient.class)
@Configuration
@EnableConfigurationProperties(OssProperties.class)
@Lazy
@ConditionalOnProperty(OssProperties.ENABLED_KEY)
public class OssConfiguration {
	
	@Autowired
	private OssProperties ossProperties;

	@Bean
//	@ConditionalOnProperty(name=BootSiteConfig.ENABLE_STORETYPE_PROPERTY, havingValue="alioss")
	public OssFileStore ossFileStore(){
		OssFileStore storer = new OssFileStore(ossClientWrapper(), ossProperties);
		return storer;
	}
	
	@Bean
//	@ConditionalOnProperty(name=BootSiteConfig.ENABLE_STORETYPE_PROPERTY, havingValue="alioss")
	public OssClientWrapper ossClientWrapper(){
		OssClientWrapper wrapper = new OssClientWrapper(ossProperties);
		wrapper.setClinetConfig(ossProperties.getClient());
		return wrapper;
	}
}

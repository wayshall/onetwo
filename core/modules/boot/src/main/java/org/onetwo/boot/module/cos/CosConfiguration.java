package org.onetwo.boot.module.cos;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qcloud.cos.COSClient;

/**
 * @author wayshall
 * <br/>
 */
@ConditionalOnClass(COSClient.class)
@Configuration
@EnableConfigurationProperties(CosProperties.class)
public class CosConfiguration {
	
	@Autowired
	private CosProperties cosProperties;

	@Bean
	@ConditionalOnProperty(name=BootSiteConfig.ENABLE_STORETYPE_PROPERTY, havingValue="cos")
	public CosFileStore cosFileStore(@Autowired CosClientWrapper wrapper){
		CosFileStore storer = new CosFileStore(wrapper, cosProperties);
		return storer;
	}
	
	@Bean
	@ConditionalOnProperty(name=BootSiteConfig.ENABLE_STORETYPE_PROPERTY, havingValue="cos")
	public CosClientWrapper OssClientWrapper(){
		CosClientWrapper wrapper = new CosClientWrapper(cosProperties);
		return wrapper;
	}
}

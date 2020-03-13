package org.onetwo.boot.module.cos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.qcloud.cos.COSClient;

/**
 * @author wayshall
 * <br/>
 */
@ConditionalOnClass(COSClient.class)
@Configuration
@EnableConfigurationProperties(CosProperties.class)
@Lazy
@ConditionalOnProperty(name=CosProperties.ENABLED_KEY)
public class CosConfiguration {
	
	@Autowired
	private CosProperties cosProperties;

	@Bean
//	@ConditionalOnProperty(name=BootSiteConfig.ENABLE_STORETYPE_PROPERTY, havingValue="cos")
	public CosFileStore cosFileStore(){
		CosFileStore storer = new CosFileStore(cosClientWrapper(), cosProperties);
		return storer;
	}
	
	@Bean
//	@ConditionalOnProperty(name=BootSiteConfig.ENABLE_STORETYPE_PROPERTY, havingValue="cos")
	public CosClientWrapper cosClientWrapper(){
		CosClientWrapper wrapper = new CosClientWrapper(cosProperties);
		return wrapper;
	}
}

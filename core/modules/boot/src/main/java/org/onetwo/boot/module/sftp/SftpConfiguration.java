package org.onetwo.boot.module.sftp;

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
@EnableConfigurationProperties(SftpProperties.class)
@Lazy
@ConditionalOnProperty(name=SftpProperties.ENABLED_KEY)
public class SftpConfiguration {
	
	@Bean
//	@ConditionalOnProperty(name=BootSiteConfig.ENABLE_STORETYPE_PROPERTY, havingValue="cos")
	public SftpFileStorer sftpFileStorer(SftpProperties sftpProperties){
		SftpFileStorer storer = new SftpFileStorer(sftpProperties);
		return storer;
	}
	
}

package org.onetwo.boot.core.web.service;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.web.service.impl.DbmFileStorerListener;
import org.onetwo.boot.core.web.service.impl.SimpleBootCommonService;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * 通用服务配置
 * @author wayshall
 *
 */
@Configuration
public class BootCommonServiceConfig {
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	@Autowired
	protected BootSiteConfig bootSiteConfig;
	
	
	/****
	 * 通过配置site.upload.fileStorePath启用
	 * site.upload.storeType存储方式：本地或者ftp
	 * site.upload.fileStorePath本地路径
	 * site.upload.appContextDir应用子目录
	 * @author wayshall
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(BootCommonService.class)
	@ConditionalOnProperty(BootSiteConfig.ENABLE_UPLOAD_PREFIX)
	public BootCommonService bootCommonService(){
		SimpleBootCommonService service = new SimpleBootCommonService();
		return service;
	}
	
	@Bean
	@ConditionalOnClass(BaseEntityManager.class)
	@ConditionalOnMissingBean({FileStorerListener.class})
	@ConditionalOnBean({BootCommonService.class, FileStorerListener.class, BaseEntityManager.class})
	@ConditionalOnProperty(value=BootSiteConfig.ENABLE_UPLOAD_STOREFILEMETATODATABASE, matchIfMissing=false)
	public FileStorerListener fileStorerListener(){
		FileStorerListener listener = new DbmFileStorerListener();
		return listener;
	}
}

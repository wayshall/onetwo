package org.onetwo.boot.core.web.service;

import net.coobird.thumbnailator.Thumbnails;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.BootSiteConfig.CompressConfig;
import org.onetwo.boot.core.web.controller.LoggerController;
import org.onetwo.boot.core.web.controller.SettingsController;
import org.onetwo.boot.core.web.controller.UploadViewController;
import org.onetwo.boot.core.web.service.impl.DbmFileStorerListener;
import org.onetwo.boot.core.web.service.impl.SettingsManager;
import org.onetwo.boot.core.web.service.impl.SimpleBootCommonService;
import org.onetwo.boot.core.web.service.impl.SimpleLoggerManager;
import org.onetwo.boot.utils.ImageCompressor;
import org.onetwo.boot.utils.ImageCompressor.ImageCompressorConfig;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.file.FileStorer;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.copier.CopyUtils;
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
		service.setCompressThresholdSize(bootSiteConfig.getUpload().getCompressImage().getThresholdSize());
		return service;
	}
	
	@Bean
	@ConditionalOnMissingBean(UploadViewController.class)
	@ConditionalOnProperty(BootSiteConfig.ENABLE_UPLOAD_PREFIX)
	public UploadViewController uploadViewController(){
		return new UploadViewController();
	}
	
	@Bean
	@ConditionalOnProperty(value=BootSiteConfig.ENABLE_COMPRESS_PREFIX, matchIfMissing=false)
	@ConditionalOnClass(Thumbnails.class)
	public ImageCompressor imageCompressor(){
		CompressConfig config = bootSiteConfig.getUpload().getCompressImage();
		ImageCompressorConfig compressorConfig = CopyUtils.copy(ImageCompressorConfig.class, config);
		ImageCompressor compressor = ImageCompressor.of(compressorConfig);
		return compressor;
	}
	
	@Bean
	@ConditionalOnClass(BaseEntityManager.class)
	@ConditionalOnMissingBean({FileStorerListener.class})
	@ConditionalOnBean({BootCommonService.class, FileStorer.class, BaseEntityManager.class})
	@ConditionalOnProperty(value=BootSiteConfig.ENABLE_UPLOAD_STOREFILEMETATODATABASE, matchIfMissing=false)
	public FileStorerListener fileStorerListener(){
		FileStorerListener listener = new DbmFileStorerListener();
		return listener;
	}
	
	/***
	 * 动态修改logger level
	 * @author wayshall
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(value=BootJFishConfig.ENABLE_DYNAMIC_LOGGER_LEVEL, matchIfMissing=false)
	public LoggerController loggerController(){
		return new LoggerController();
	}
	
	@Bean
	@ConditionalOnMissingBean({SimpleLoggerManager.class})
	@ConditionalOnProperty(value=BootJFishConfig.ENABLE_DYNAMIC_LOGGER_LEVEL, matchIfMissing=false)
	public SimpleLoggerManager simpleLoggerManager(){
		return new SimpleLoggerManager();
	}


	/***
	 * 动态修改默认配置
	 * @author wayshall
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(value=BootJFishConfig.ENABLE_DYNAMIC_SETTING, matchIfMissing=false)
	public SettingsController settingsController(){
		return new SettingsController();
	}

	@Bean
	@ConditionalOnMissingBean({SettingsManager.class})
	@ConditionalOnProperty(value=BootJFishConfig.ENABLE_DYNAMIC_SETTING, matchIfMissing=false)
	public SettingsManager settingsManager(){
		return new SettingsManager();
	}
}

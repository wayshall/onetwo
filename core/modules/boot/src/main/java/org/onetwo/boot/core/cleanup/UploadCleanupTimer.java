package org.onetwo.boot.core.cleanup;

import java.util.Map;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.BootSiteConfig.CleanupProps;
import org.onetwo.boot.core.config.BootSiteConfig.StoreType;
import org.onetwo.boot.core.config.BootSiteConfig.UploadConfig;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.beust.jcommander.internal.Maps;

//@ConditionalOnProperty(name = CleanupProps.ENABLED_KEY)
public class UploadCleanupTimer implements InitializingBean {
	private final Logger logger = JFishLoggerFactory.getLogger(getClass());
	
	@Autowired
	private BootSiteConfig siteConfig;
	private Map<StoreType, FileStoreCleanup> fileStoreCleanupMap = Maps.newHashMap();

	@Override
	public void afterPropertiesSet() throws Exception {
		UploadConfig uploadConfig = siteConfig.getUpload();
		fileStoreCleanupMap.put(StoreType.LOCAL, new LocalFileCleanup(uploadConfig));
	}
	
	@Scheduled(cron =CleanupProps.CRON)
	public void cleanup() {
		UploadConfig uploadConfig = siteConfig.getUpload();
		StoreType storeType = uploadConfig.getStoreType();
        if (uploadConfig.getCleanup().getExpiredInDays()<0) {
			logger.warn("{} cleanup ignore : site.upload.cleanup.expiredInDays is less than 0");
            return;
        }
        
        FileStoreCleanup fileStoreCleanup = fileStoreCleanupMap.get(storeType);
        if (fileStoreCleanup==null) {
			logger.error("cleanup ignore. unsupport storeType : {}", storeType);
			return ;
        }
        fileStoreCleanup.cleanup();
	}

}

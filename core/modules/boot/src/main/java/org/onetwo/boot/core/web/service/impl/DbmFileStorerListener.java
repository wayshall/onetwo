package org.onetwo.boot.core.web.service.impl;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.web.service.FileStorerListener;
import org.onetwo.boot.core.web.service.UploadResourceEntity;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.file.FileStoredMeta;
import org.onetwo.common.file.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wayshall
 * <br/>
 */
public class DbmFileStorerListener implements FileStorerListener {
	
	@Autowired
	private BaseEntityManager baseEntityManager;
	@Autowired
	private BootSiteConfig bootSiteConfig;

	@Override
	public void afterFileStored(FileStoredMeta meta) {
		if(!bootSiteConfig.getUpload().isStoreFileMetaToDatabase()){
			return ;
		}
		UploadResourceEntity resource = new UploadResourceEntity();
		resource.setFilePath(meta.getAccessablePath());
		resource.setOriginName(meta.getOriginalFilename());
		resource.setFileType(FileUtils.getExtendName(meta.getOriginalFilename()));
		baseEntityManager.save(resource);
	}
	

}

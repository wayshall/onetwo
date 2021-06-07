package org.onetwo.boot.module.sftp;

import java.io.InputStream;
import java.io.OutputStream;

import org.onetwo.boot.core.web.utils.PathTagResolver;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileStorer;
import org.onetwo.common.file.SimpleFileStoredMeta;
import org.onetwo.common.file.SimpleFileStorer;
import org.onetwo.common.file.SimpleFileStorer.SimpleStoreFilePathStrategy;
import org.onetwo.common.file.StoringFileContext;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * @author weishao zeng
 * <br/>
 */

public class SftpFileStorer /* extends SimpleFileStorer */implements FileStorer {
	
	private SftpProperties sftpProperties;
	@Autowired
	private PathTagResolver pathTagResolver;

	public SftpFileStorer(SftpProperties sftpProperties){
		Assert.notNull(sftpProperties, "sftpProperties can not be null");
		this.sftpProperties = sftpProperties;
	}

	public String getStoreType() {
		return "sftp";
	}
	
	@Override
	public SimpleFileStoredMeta write(StoringFileContext context) {
		String key = defaultStoreKey(context);
		context.setKey(key);
		
		String accessablePath = StringUtils.appendStartWithSlash(key);
		SimpleFileStoredMeta meta = new SimpleFileStoredMeta(context.getFileName(), accessablePath);
		
		doStoring(meta, context);
		
		if (StringUtils.isNotBlank(sftpProperties.getPathTag())) {
			pathTagResolver.checkPathTag(sftpProperties.getPathTag());
			accessablePath = sftpProperties.getPathTag() + accessablePath;
		}
		
		meta.setBaseUrl(sftpProperties.getEndPoint());
		meta.setSotredFileName(key);
		meta.setAccessablePath(accessablePath);
		meta.setFullAccessablePath(sftpProperties.getDownloadUrl(accessablePath));
		if(sftpProperties.isAlwaysStoreFullPath()){
			meta.setAccessablePath(meta.getFullAccessablePath());
		}
		meta.setStoredServerLocalPath(key);
		meta.setBizModule(context.getModule());
		meta.setSotredFileName(key);
		
		return meta;
	}

	protected SimpleFileStoredMeta getStoreDir(StoringFileContext context){
//		Assert.notNull(context.getStoreFilePathStrategy(), "strategy can not be null");
		if(StringUtils.isBlank(sftpProperties.getBaseDir())){
			throw new BaseException("store dir must be config, but blank ");
		}
		SimpleStoreFilePathStrategy strategy = SimpleFileStorer.SIMPLE_STORE_STRATEGY;
		/*if(strategy==null){
			strategy = SIMPLE_STORE_STRATEGY;
		}*/
		SimpleFileStoredMeta meta = (SimpleFileStoredMeta)strategy.getStoreFilePath(null, context);
		return meta;
	}
	
	protected void doStoring(SimpleFileStoredMeta meta, StoringFileContext context){
		SshClientManager ftpClientManager = new SshClientManager(sftpProperties);
		try {
			ftpClientManager.init();
			ftpClientManager.upload(meta.getStoredServerLocalPath(), context.getInputStream());
		} finally {
			ftpClientManager.destroy();
		}
	}

	@Override
	public void readFileTo(final String accessablePath, final OutputStream output){
		String fullPath = accessablePath;
		SshClientManager ftpClientManager = new SshClientManager(sftpProperties);
		try {
			ftpClientManager.init();
			ftpClientManager.retrieveFile(fullPath, output);
		} finally {
			ftpClientManager.destroy();
		}
	}

	@Override
	public InputStream readFileStream(final String accessablePath){
		String fullPath = accessablePath;
		SshClientManager ftpClientManager = new SshClientManager(sftpProperties);
		try {
			ftpClientManager.init();
			return ftpClientManager.retrieveFileStream(fullPath);
		} finally {
			ftpClientManager.destroy();
		}
	}

	//TODO
	@Override
	public long getLastModified(String accessablePath) {
		return -1;
	}

}

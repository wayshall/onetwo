package org.onetwo.boot.module.cos;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.onetwo.apache.io.IOUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileStoredMeta;
import org.onetwo.common.file.FileStorer;
import org.onetwo.common.file.SimpleFileStoredMeta;
import org.onetwo.common.file.StoreFilePathStrategy;
import org.onetwo.common.file.StoringFileContext;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author wayshall
 * <br/>
 */
public class CosFileStore implements FileStorer, InitializingBean {
	
	private CosClientWrapper wrapper;
	private CosProperties cosProperties;
	private String bucketName;

	public CosFileStore(CosClientWrapper wrapper, CosProperties ossProperties) {
		super();
		this.wrapper = wrapper;
		this.cosProperties = ossProperties;
		this.bucketName = ossProperties.getAppBucketName();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.wrapper.createBucketIfNotExists(cosProperties.getAppBucketName());
	}

	@Override
	public FileStoredMeta write(StoringFileContext context) {
		String key = context.getKey();
		if(StringUtils.isBlank(key)){
			key = defaultStoreKey(context);
		}
		wrapper.objectOperation(bucketName, key)
				.store(context.getInputStream());

		String accessablePath = StringUtils.appendStartWithSlash(key);

		FileStoredMeta fmeta = null;
		StoreFilePathStrategy strategy = context.getStoreFilePathStrategy();
		if(strategy==null){
			SimpleFileStoredMeta meta = new SimpleFileStoredMeta(context.getFileName(), accessablePath);
			meta.setBaseUrl(cosProperties.getDownloadEndPoint());
			meta.setSotredFileName(key);
			meta.setAccessablePath(accessablePath);
			meta.setFullAccessablePath(cosProperties.getDownloadUrl(key));
			if(cosProperties.isAlwaysStoreFullPath()){
				meta.setAccessablePath(meta.getFullAccessablePath());
			}
			meta.setStoredServerLocalPath(key);
			meta.setBizModule(context.getModule());
			meta.setSotredFileName(key);
			fmeta = meta;
		}else{
			fmeta = strategy.getStoreFilePath(context.getFileStoreBaseDir(), null, context);
		}
		
		return fmeta;
	}
	
	@Override
	public void readFileTo(String accessablePath, OutputStream output) {
		InputStream in = readFileStream(accessablePath);
		try {
			IOUtils.copy(in, output);
		} catch (IOException e) {
			throw new BaseException("copy inputstream error!", e);
		}
	}

	@Override
	public InputStream readFileStream(String accessablePath) {
		String key = accessablePath;
		InputStream in = wrapper.objectOperation(bucketName, key)
								.getCosObject()
								.orElseThrow(()->new BaseException("no file found for key: " + key))
								.getObjectContent();
		return in;
	}

	@Override
	public long getLastModified(String accessablePath) {
		return 0;
	}
	
	

}

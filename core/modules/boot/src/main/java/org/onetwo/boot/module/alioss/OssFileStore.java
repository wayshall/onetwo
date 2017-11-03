package org.onetwo.boot.module.alioss;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.onetwo.common.file.FileStorer;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.file.SimpleFileStoredMeta;
import org.onetwo.common.file.StoringFileContext;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author wayshall
 * <br/>
 */
public class OssFileStore implements FileStorer<SimpleFileStoredMeta>, InitializingBean {
	
	private OssClientWrapper wrapper;
	private OssProperties ossProperties;

	public OssFileStore(OssClientWrapper wrapper, OssProperties ossProperties) {
		super();
		this.wrapper = wrapper;
		this.ossProperties = ossProperties;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.wrapper.createBucketIfNotExists(ossProperties.getBucketName());
	}

	@Override
	public SimpleFileStoredMeta write(StoringFileContext context) {
		String key = context.getKey();
		if(StringUtils.isBlank(key)){
			key = StringUtils.emptyIfNull(context.getModule())+"-"+UUID.randomUUID().toString()+FileUtils.getExtendName(context.getFileName(), true);
		}
		wrapper.objectOperation(ossProperties.getBucketName(), key)
				.store(context.getInputStream());
		
		String accessablePath = key;
		SimpleFileStoredMeta meta = new SimpleFileStoredMeta(context.getFileName(), key);
		meta.setSotredFileName(key);
		meta.setAccessablePath(accessablePath);
		meta.setFullAccessablePath(accessablePath);
		meta.setBizModule(context.getModule());
		return meta;
	}

	@Override
	public void readFileTo(String accessablePath, OutputStream output) {
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream readFileStream(String accessablePath) {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getLastModified(String accessablePath) {
		return 0;
	}
	
	

}

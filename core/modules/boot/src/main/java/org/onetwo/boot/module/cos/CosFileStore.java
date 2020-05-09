package org.onetwo.boot.module.cos;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.onetwo.apache.io.IOUtils;
import org.onetwo.boot.core.config.BootSiteConfig.StoreType;
import org.onetwo.boot.module.cos.CosProperties.VideoConfig;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.expr.ExpressionFacotry;
import org.onetwo.common.file.FileStoredMeta;
import org.onetwo.common.file.FileStorer;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.file.SimpleFileStoredMeta;
import org.onetwo.common.file.StoringFileContext;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.collect.ImmutableMap;

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


	public String getStoreType() {
		return StoreType.COS.name().toLowerCase();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.wrapper.createBucketIfNotExists(cosProperties.getAppBucketName());
	}

	@Override
	public FileStoredMeta write(StoringFileContext context) {
		String key = defaultStoreKey(context);
		context.setKey(key);
		/*String key = context.getKey();
		if(StringUtils.isBlank(key)){
			key = defaultStoreKey(context);
		}*/
		wrapper.objectOperation(bucketName, key)
				.store(context.getInputStream());

		String accessablePath = StringUtils.appendStartWithSlash(key);

//		StoreFilePathStrategy strategy = context.getStoreFilePathStrategy();
		SimpleFileStoredMeta meta = new SimpleFileStoredMeta(context.getFileName(), accessablePath);
		meta.setBaseUrl(cosProperties.getDownloadEndPoint());
		meta.setSotredFileName(key);
		meta.setAccessablePath(accessablePath);
		meta.setFullAccessablePath(cosProperties.getDownloadUrl(accessablePath));
		if(cosProperties.isAlwaysStoreFullPath()){
			meta.setAccessablePath(meta.getFullAccessablePath());
		}
		meta.setStoredServerLocalPath(key);
		meta.setBizModule(context.getModule());
		meta.setSotredFileName(key);
		
		processVideo(meta);
		
		return meta;
	}
	
	/***
	 * 处理视频，如水印、截图
	 * @author weishao zeng
	 * @param meta
	 */
	private void processVideo(SimpleFileStoredMeta meta) {
		String accessablePath = meta.getAccessablePath();
		int lastDot = accessablePath.lastIndexOf(FileUtils.DOT_CHAR);
		String filename = accessablePath.substring(0, lastDot);
		String format = accessablePath.substring(lastDot+1);
		VideoConfig videoConfig = cosProperties.getVideo();
		if (videoConfig.isEnabled() && videoConfig.getPostfix().contains(format)) {
			Map<String, String> parseCtx = ImmutableMap.of("filename", filename, "format", format);
			
			String snapshotFileName = ExpressionFacotry.BRACE.parseByProvider(videoConfig.getSnapshotFileName(), parseCtx);
			SimpleFileStoredMeta cutMeta = new SimpleFileStoredMeta(meta.getOriginalFilename(), snapshotFileName);
			cutMeta.setSotredFileName(snapshotFileName);
			cutMeta.setAccessablePath(snapshotFileName);
			meta.setSnapshotStoredMeta(cutMeta);
			
			// 覆盖非水印视频
			String waterMaskFileName = ExpressionFacotry.BRACE.parseByProvider(videoConfig.getWaterMaskFileName(), parseCtx);
			meta.setSotredFileName(waterMaskFileName);
			meta.setAccessablePath(waterMaskFileName);
			meta.setFullAccessablePath(cosProperties.getDownloadUrl(waterMaskFileName));
			
			// 轮询……
			Logger logger = JFishLoggerFactory.getCommonLogger();
			while(!wrapper.getCosClient().doesObjectExist(bucketName, accessablePath)) {
				logger.info("正在检查转码后的视频是否存在, accessablePath: {} ……", accessablePath);
				LangUtils.awaitInMillis(300);
			}
			logger.info("获取转码后的时间成功, accessablePath: {} ", accessablePath);
		}
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

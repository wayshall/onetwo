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
	private Logger logger = JFishLoggerFactory.getLogger(getClass());
	
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
		meta.setStoredServerLocalPath(key);
		meta.setBizModule(context.getModule());
		meta.setSotredFileName(key);
		
		processVideo(meta);
		
		if(cosProperties.isAlwaysStoreFullPath()){
			meta.setAccessablePath(meta.getFullAccessablePath());
		}
		
		return meta;
	}
	
	/***
	 * 处理视频，如水印、截图
	 * @author weishao zeng
	 * @param meta
	 */
	private void processVideo(SimpleFileStoredMeta meta) {
		String accessablePath = meta.getAccessablePath();
//		int lastDot = accessablePath.lastIndexOf(FileUtils.DOT_CHAR);
//		String filename = accessablePath.substring(0, lastDot);
//		String format = accessablePath.substring(lastDot+1).toLowerCase();
		String filename = FileUtils.getFileNameWithoutExt(accessablePath);
		String format = FileUtils.getExtendName(accessablePath).toLowerCase();
		VideoConfig videoConfig = cosProperties.getVideo();
		if (videoConfig.isEnabled() 
				&& accessablePath.startsWith(videoConfig.getTriggerDir())
				&& videoConfig.getPostfix().contains(format)) {
			Map<String, String> parseCtx = ImmutableMap.of("filename", filename, "format", format);
			
			String snapshotFileName = ExpressionFacotry.BRACE.parseByProvider(videoConfig.getSnapshotFileName(), parseCtx);
			snapshotFileName = videoConfig.getOutputDir() + snapshotFileName;
			SimpleFileStoredMeta cutMeta = new SimpleFileStoredMeta(meta.getOriginalFilename(), snapshotFileName);
			cutMeta.setSotredFileName(snapshotFileName);
			cutMeta.setAccessablePath(snapshotFileName);
			cutMeta.setFullAccessablePath(cosProperties.getDownloadUrl(snapshotFileName));
			if(cosProperties.isAlwaysStoreFullPath()){
				cutMeta.setAccessablePath(cutMeta.getFullAccessablePath());
			}
			meta.setSnapshotStoredMeta(cutMeta);
			
			// 覆盖非水印视频
			String waterMaskFileName = ExpressionFacotry.BRACE.parseByProvider(videoConfig.getWaterMaskFileName(), parseCtx);
			waterMaskFileName = videoConfig.getOutputDir() + waterMaskFileName;
			meta.setSotredFileName(waterMaskFileName);
			meta.setAccessablePath(waterMaskFileName);
			meta.setFullAccessablePath(cosProperties.getDownloadUrl(waterMaskFileName));
			
			// 是否轮询异步结果，默认为1000毫秒……
			int checkInMillis = videoConfig.getCheckTaskInMillis();
			int totalInMillis = 0;
			if (checkInMillis > 0) {
				while(!wrapper.getCosClient().doesObjectExist(bucketName, waterMaskFileName)) {
					logger.info("正在检查转码后的视频是否存在, waterMaskFileName: {} ……", waterMaskFileName);
					LangUtils.awaitInMillis(checkInMillis);
					totalInMillis += checkInMillis;
					if (totalInMillis>=videoConfig.getTotalTaskInMillis()) {
						logger.info("获取转码后的视频达到最大时间, waterMaskFileName: {} ", waterMaskFileName);
						break;
					}
				}
				logger.info("获取转码后的视频成功, waterMaskFileName: {} ", waterMaskFileName);
			}
			logger.info("上传视频成功, 视频: {}, 水印: {} ", waterMaskFileName, snapshotFileName);
		} else {
			logger.info("忽略处理视频：enabled: {}, accessablePath: {}, format: {}", videoConfig.isEnabled(), accessablePath, format);
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

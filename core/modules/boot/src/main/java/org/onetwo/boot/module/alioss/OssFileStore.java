package org.onetwo.boot.module.alioss;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.onetwo.apache.io.IOUtils;
import org.onetwo.boot.core.web.service.impl.BootStoringFileContext;
import org.onetwo.boot.module.alioss.OssClientWrapper.ObjectOperation;
import org.onetwo.boot.module.alioss.OssProperties.WaterMaskProperties;
import org.onetwo.boot.module.alioss.video.SnapshotProperties;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileStoredMeta;
import org.onetwo.common.file.FileStorer;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.file.SimpleFileStoredMeta;
import org.onetwo.common.file.StoringFileContext;
import org.onetwo.common.spring.copier.CopyUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author wayshall
 * <br/>
 */
public class OssFileStore implements FileStorer, InitializingBean {
	
	private OssClientWrapper wrapper;
	private OssProperties ossProperties;
//	private String bucketName;

	public OssFileStore(OssClientWrapper wrapper, OssProperties ossProperties) {
		super();
		this.wrapper = wrapper;
		this.ossProperties = ossProperties;
//		this.bucketName = ossProperties.getBucketName();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.wrapper.createBucketIfNotExists(ossProperties.getBucketName());
	}
	
	public void delete(String key) {
		key = StringUtils.trimStartWith(key, FileUtils.SLASH);
		ObjectOperation operation = wrapper.objectOperation(ossProperties.getBucketName(), key);
		operation.delete();
	}

	@Override
	public FileStoredMeta write(StoringFileContext ctx) {
		BootStoringFileContext context = (BootStoringFileContext) ctx;
		String key = defaultStoreKey(context);
		context.setKey(key);
		
		ResizeProperties resizeConfig = new ResizeProperties();
		CopyUtils.copyIgnoreNullAndBlank(resizeConfig, ossProperties.getResize());
		if (context.getResizeConfig()!=null) {
			// 如果显式传入了压缩参数，则必须生成缩略图
			resizeConfig.setEnabled(true);
			CopyUtils.copyIgnoreNullAndBlank(resizeConfig, context.getResizeConfig());
		}
		
		WaterMaskProperties waterMaskConfig = new WaterMaskProperties();
		CopyUtils.copyIgnoreNullAndBlank(waterMaskConfig, ossProperties.getWatermask());
		if (context.getWaterMaskConfig()!=null) {
			// 如果显式传入了水印参数，则必须生成水印
			waterMaskConfig.setEnabled(true);
			CopyUtils.copyIgnoreNullAndBlank(waterMaskConfig, context.getWaterMaskConfig());
		}
		
		ObjectOperation operation = wrapper.objectOperation(ossProperties.getBucketName(), key)
				.store(context.getInputStream())
				.watermask(waterMaskConfig);

		String accessablePath = StringUtils.appendStartWithSlash(key);

		SimpleFileStoredMeta meta = new SimpleFileStoredMeta(context.getFileName(), key);
		meta.setBaseUrl(ossProperties.getDownloadEndPoint());
		meta.setSotredFileName(key);
		meta.setAccessablePath(accessablePath);
		meta.setFullAccessablePath(ossProperties.getUrl(key));
		meta.setStoredServerLocalPath(key);
		meta.setBizModule(context.getModule());
		meta.setSotredFileName(key);
		
		operation.resize(resizeConfig, minKey -> {
			SimpleFileStoredMeta minMeta = new SimpleFileStoredMeta(meta.getOriginalFilename(), minKey);
			minMeta.setSotredFileName(minKey);
			minMeta.setAccessablePath(minKey);
			meta.setResizeStoredMeta(minMeta);
		});
		
		SnapshotProperties snapshotConfig = new SnapshotProperties();
		CopyUtils.copyIgnoreNullAndBlank(snapshotConfig, ossProperties.getSnapshot());
		if (context.getSnapshotConfig()!=null) {
			// 如果显式传入了截图参数，则必须生成截图
			snapshotConfig.setEnabled(true);
			CopyUtils.copyIgnoreNullAndBlank(snapshotConfig, context.getSnapshotConfig());
		}
		operation.videoSnapshot(snapshotConfig, cutImageKey -> {
			SimpleFileStoredMeta cutMeta = new SimpleFileStoredMeta(meta.getOriginalFilename(), cutImageKey);
			cutMeta.setSotredFileName(cutImageKey);
			cutMeta.setAccessablePath(cutImageKey);
			meta.setSnapshotStoredMeta(cutMeta);
		});
		
		return meta;
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
		InputStream in = wrapper.objectOperation(ossProperties.getBucketName(), key)
								.getOSSObject()
								.orElseThrow(()->new BaseException("no file found for key: " + key))
								.getObjectContent();
		return in;
	}

	@Override
	public long getLastModified(String accessablePath) {
		return 0;
	}

	public OssProperties getOssProperties() {
		return ossProperties;
	}
	
}

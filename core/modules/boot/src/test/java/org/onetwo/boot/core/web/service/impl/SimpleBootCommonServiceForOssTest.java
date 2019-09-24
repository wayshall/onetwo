package org.onetwo.boot.core.web.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.boot.core.web.utils.SimpleMultipartFile;
import org.onetwo.boot.core.web.utils.UploadOptions;
import org.onetwo.boot.core.web.utils.UploadOptions.ResizeConfig;
import org.onetwo.boot.core.web.utils.UploadOptions.WaterMaskConfig;
import org.onetwo.boot.module.alioss.OssClientWrapper;
import org.onetwo.boot.module.alioss.OssClientWrapperTest.TestOssUploadContextConfig;
import org.onetwo.boot.module.alioss.OssFileStore;
import org.onetwo.boot.module.alioss.OssProperties;
import org.onetwo.boot.module.alioss.OssProperties.WaterMaskProperties;
import org.onetwo.common.file.FileStoredMeta;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author weishao zeng
 * <br/>
 */
@ContextConfiguration(loader=AnnotationConfigContextLoader.class, classes=TestOssUploadContextConfig.class)
public class SimpleBootCommonServiceForOssTest extends AbstractJUnit4SpringContextTests {
private OssProperties ossProperties;
	
	@Value("${jfish.alioss.endpoint}")
	String endpoint;
	@Value("${jfish.alioss.accessKeyId}")
    String accessKeyId;
	@Value("${jfish.alioss.accessKeySecret}")
    String accessKeySecret;
	
    OssClientWrapper wraper;
    OssFileStore storer;
    
    String bucketName = "wxablum";
    
	@Before
	public void setupOss() throws Exception{
		ossProperties = new OssProperties();
		ossProperties.setEndpoint(endpoint);
		ossProperties.setAccessKeyId(accessKeyId);
		ossProperties.setAccessKeySecret(accessKeySecret);
		ossProperties.setBucketName(bucketName);
		
//		ossProperties.getResize().setEnabled(true);
		
		WaterMaskProperties watermask = ossProperties.getWatermask();
//		watermask.setEnabled(true);
		watermask.setText("测试oss~~");
		watermask.setSize(100);
//		watermask.setImageProcess("x-oss-process=image/resize,P_30");
		
		this.wraper = new OssClientWrapper(ossProperties);
		wraper.afterPropertiesSet();
//		this.wraper.createBucketIfNotExists(bucketName);
		
		storer = new OssFileStore(wraper, ossProperties);
		storer.afterPropertiesSet();
	}
	
	@Test
	public void testOss() throws Exception {
		
		Resource resource = SpringUtils.classpath("data/test.jpg");
		
		SimpleBootCommonService common = new SimpleBootCommonService();
		common.setFileStoreBaseDir("/test");
		common.setFileStorer(storer);
		
		SimpleMultipartFile upfile = new SimpleMultipartFile("test.jpg", resource.getFile());
		UploadOptions opts = UploadOptions.builder()		
											.multipartFile(upfile)
											.key("test-upload.jpg")
											.resizeConfig(ResizeConfig.builder().width(200).build())
											.build();
		FileStoredMeta meta = common.uploadFile(opts);
		System.out.println("meta: " + meta);
	}
	
	@Test
	public void testOssWithImageMask() throws Exception {
		
		Resource resource = SpringUtils.classpath("data/test.jpg");


		WaterMaskProperties watermask = new WaterMaskProperties();
//		watermask.setEnabled(true);
//		watermask.setText("测试oss~~");
//		watermask.setSize(100);
//		watermask.setImageProcess("x-oss-process=image/resize,P_15");
		
		ossProperties.setWatermask(watermask);
		storer = new OssFileStore(wraper, ossProperties);
		SimpleBootCommonService common = new SimpleBootCommonService();
		common.setFileStoreBaseDir("/test");
		common.setFileStorer(storer);
		
		SimpleMultipartFile upfile = new SimpleMultipartFile("test.jpg", resource.getFile());
		UploadOptions opts = UploadOptions.builder()		
											.multipartFile(upfile)
											.key("test-upload.jpg")
											.resizeConfig(ResizeConfig.builder().width(200).build())
											.waterMaskConfig(WaterMaskConfig.builder().image("mask/hyatt.png").build())
											.waterMaskConfig(WaterMaskConfig.builder().image("mask/tgts.png").build())
											.build();
		FileStoredMeta meta = common.uploadFile(opts);
		System.out.println("meta: " + meta);
	}
}

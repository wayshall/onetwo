package org.onetwo.boot.module.alioss;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.boot.module.alioss.OssClientWrapperTest.TestOssUploadContextConfig;
import org.onetwo.boot.module.alioss.OssProperties.WaterMaskProperties;
import org.onetwo.boot.module.alioss.WatermarkAction.WatermarkFonts;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.ProcessObjectRequest;

/**
 * @author wayshall
 * <br/>
 */
@ContextConfiguration(loader=AnnotationConfigContextLoader.class, classes=TestOssUploadContextConfig.class)
public class OssClientWrapperTest extends AbstractJUnit4SpringContextTests {
	private OssProperties ossProperties;
	
	@Value("${jfish.alioss.endpoint}")
	String endpoint;
	@Value("${jfish.alioss.accessKeyId}")
    String accessKeyId;
	@Value("${jfish.alioss.accessKeySecret}")
    String accessKeySecret;
	
    OssClientWrapper wraper;
    
    String bucketName = "wxablum";

	@Before
	public void setup() throws Exception{
		ossProperties = new OssProperties();
		ossProperties.setEndpoint(endpoint);
		ossProperties.setAccessKeyId(accessKeyId);
		ossProperties.setAccessKeySecret(accessKeySecret);
		ossProperties.setBucketName(bucketName);
		
		this.wraper = new OssClientWrapper(ossProperties);
		wraper.afterPropertiesSet();
//		this.wraper.createBucketIfNotExists(bucketName);
	}
	
	@Test
	public void listBuckets(){
		System.out.println("Listing buckets");
        for (Bucket bucket : wraper.listBuckets()) {
            System.out.println(" - " + bucket.getName());
        }
        System.out.println();
	}
	
	@Test
	public void testUpload() throws Exception {
		ResizeProperties config = new ResizeProperties();
		config.setWidth(200);
		Resource resource = SpringUtils.classpath("data/test.jpg");
		this.wraper.objectOperation(bucketName, "test/test.jpg")
					.store(resource.getFile(), null, res -> {
						System.out.println("res: " + res);
					})
					.resize(config);
	}
	
	@Test
	public void testUploadWithProcess() throws Exception {
		String text = "测试";
		text = LangUtils.newString(Base64.encodeBase64(text.getBytes()));
		String styleType = "image/watermark,text_" + text;
		String targetImage = "test/test2.jpg";
		String str = String.format("%s|sys/saveas,o_%s,b_%s", styleType,
				LangUtils.newString(Base64.encodeBase64(targetImage.getBytes())),
				LangUtils.newString(Base64.encodeBase64(bucketName.getBytes())));
		ProcessObjectRequest por = new ProcessObjectRequest(bucketName, "test/test.jpg", str);
		this.wraper.getOssClient().processObject(por);
	}
	
	@Test
	public void testWatermarkAction() throws Exception {
		WatermarkAction obj = new WatermarkAction();
		obj.setBucketName(bucketName);
		obj.setSourceKey("test/test.jpg");
		obj.setTargetKey("test/test2.jpg");
		obj.setText("测试又测试");
		obj.setFont(WatermarkFonts.FANGZHENGKAITI);
		obj.setSize(400);
		obj.setColor("FFFFFF");
		obj.setLocation("center");
//		obj.setFill(1);
		System.out.println("style: " + obj.toStyleWithName());
		this.wraper.getOssClient().processObject(obj.buildRequest());
	}
	@Test
	public void testWatermarkAction2() throws Exception {
		WaterMaskProperties config = new WaterMaskProperties();
		config.setText("测试了~~");
		config.setSize(100);
		this.wraper.objectOperation(bucketName, "test/test.jpg")
					.watermask(config);
	}
	
	@Test
	public void testResizeAction() throws Exception {
		ResizeAction obj = new ResizeAction();
		obj.setBucketName(bucketName);
		obj.setSourceKey("test/test.jpg");
		obj.setTargetKey("test/test2.jpg");
//		obj.setWidth(200);
		ResizeProperties config = new ResizeProperties();
		config.setWidth(200);
		obj.configBy(config);
		System.out.println("style: " + obj.toStyleWithName());
		this.wraper.getOssClient().processObject(obj.buildRequest());
	}
	
	@Test
	public void testResizeAction2() throws Exception {
		ResizeProperties config = new ResizeProperties();
		config.setWidth(200);
		this.wraper.objectOperation(bucketName, "test/test.jpg")
					.resize("test/test2.jpg", config);
	}

	@Configuration
	@PropertySource(value="classpath:application-product.properties")
	public static class TestOssUploadContextConfig {
		
	}

}

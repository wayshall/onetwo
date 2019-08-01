package org.onetwo.boot.core.web.service.impl;

import java.io.File;

import org.junit.Test;
import org.onetwo.boot.core.web.service.impl.SimpleBootCommonService;
import org.onetwo.boot.core.web.utils.SimpleMultipartFile;
import org.onetwo.boot.core.web.utils.UploadOptions;
import org.onetwo.boot.module.cos.CosClientWrapper;
import org.onetwo.boot.module.cos.CosFileStore;
import org.onetwo.boot.module.cos.CosProperties;
import org.onetwo.common.file.FileStoredMeta;

/**
 * @author weishao zeng
 * <br/>
 */
public class SimpleBootCommonServiceTest {

	@Test
	public void testCos() throws Exception {
		CosProperties cos = new CosProperties();
		
		String bucketName = "test"; //dangjian
		cos.setAccessKey("");
		cos.setSecretKey("");
		cos.setAppid("");
		cos.setRegionName("ap-guangzhou");
		cos.setBucketName(bucketName);
		cos.setEndPoint("cos."+cos.getRegionName()+".myqcloud.com");
		
		File file = new File("./logo.png");
		CosClientWrapper wrapper = new CosClientWrapper(cos);
		wrapper.afterPropertiesSet();
//		wrapper.objectOperation(bucketName + "-"+cos.getAppid(), "test.jpg")
//				.store(file)
//				.storeResult()
//				.ifPresent(res -> {
//					System.out.println("uplaod: " + res);
//				});
		
		CosFileStore storer = new CosFileStore(wrapper, cos);
		SimpleBootCommonService common = new SimpleBootCommonService();
		common.setFileStoreBaseDir("/test");
		common.setFileStorer(storer);
		
		SimpleMultipartFile upfile = new SimpleMultipartFile("test.png", file);
		UploadOptions opts = UploadOptions.builder()		
											.multipartFile(upfile)
											.module("/aa")
//											.key("test.png")
											.build();
		FileStoredMeta meta = common.uploadFile(opts);
		System.out.println("meta: " + meta);
	}
}

package org.onetwo.common.utils;
/**
 * @author weishao zeng
 * <br/>
 */

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ZipUtilsTest {

	@Test
	public void testZipFile() {
		String moduleList = "bff-canteen,parent,app-client,base-common,cms-client,cms-service,comment-mall-client,comment-mall-service,"
				+ "common-client,common-service,data-client,data-service,delivery-client,delivery-service,mall-client,mall-service,"
				+ "order-client,order-service,pay-client,pay-service,tenant-client,tenant-service,uaa-client,uaa-service,"
				+ "wechat-client,wechat-service,"
				+ "bff-canteen-store,print-client,print-service,"
				+ "bff-canteen-mgr,data-mgr,delivery-mgr,mall-canteen-mgr,mall-market-client,mall-market-mgr,mall-mgr,order-mgr,print-mgr,tcc-mgr,tenant-mgr";
		String zipfile = "canteen-src.zip";
		String baseDir = "D:\\mydev\\java\\odysseus-branch\\odysseus";
		List<String> subDirs = Arrays.asList(StringUtils.split(moduleList, ","));
		ZipUtils.zipfiles(zipfile, baseDir,
						(file, subFileName) -> {
							String[] paths = StringUtils.split(subFileName, File.separator);
							return subDirs.contains(paths[0]) && (paths[1].equals("src") || paths[1].equals("pom.xml"));
						});
	}
}

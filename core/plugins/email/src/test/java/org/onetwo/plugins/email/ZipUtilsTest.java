package org.onetwo.plugins.email;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.FileUtils;

public class ZipUtilsTest {

	@Test
	public void testZipfile2(){
		String filepath = "E:/mydev/java_workspace/xianda/files/公交消费月报表_清远市二运公交_201403.xls";
		File file = new File(filepath);
		String zipfilePath = FileUtils.getNewFilenameBy(file, ".zip");
		File zipfile = EmailUtils.zipfile(zipfilePath, file, true);
		System.out.println("zipfile: " + zipfile.getPath());
		System.out.println("zipfile: " + zipfile.getName());
		Assert.assertEquals("公交消费月报表_清远市二运公交_201403.zip", zipfile.getName());
	}
}

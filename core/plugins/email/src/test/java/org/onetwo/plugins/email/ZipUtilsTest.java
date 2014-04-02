package org.onetwo.plugins.email;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class ZipUtilsTest {

	@Test
	public void testZipfile2(){
		String dir = "E:/mydev/java_workspace/xianda/files/公交消费月报表_清远市二运公交_201403.xls";
		File zipfile = ZipUtils.zipfile(new File(dir), "utf-8");
		System.out.println("zipfile: " + zipfile.getPath());
		System.out.println("zipfile: " + zipfile.getName());
		Assert.assertEquals("公交消费月报表_清远市二运公交_201403.zip", zipfile.getName());
	}
}

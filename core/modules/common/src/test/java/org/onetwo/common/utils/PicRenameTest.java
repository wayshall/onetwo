package org.onetwo.common.utils;

import java.io.File;
import java.util.stream.Stream;

import org.junit.Test;
import org.onetwo.common.file.FileUtils;

/**
 * @author wayshall
 * <br/>
 */
public class PicRenameTest {
	
	@Test
	public void test(){
		File[] files = FileUtils.listFiles("F:/test/all", ".jpg");
		Stream.of(files).forEach(f->{
			System.out.println(f.getName());
			String name = f.getName().replaceAll("([\u4e00-\u9fa5]+)", "");
			System.out.println("newname:"+name);
			File renameFile = new File(f.getParentFile()+"/rename", name);
			renameFile.getParentFile().mkdirs();
			f.renameTo(renameFile);
		});
	}

}

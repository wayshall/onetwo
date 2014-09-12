package org.onetwo.common.utils;

import java.io.InputStream;
import java.util.List;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

import org.apache.tools.ant.filters.StringInputStream;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.profiling.TimeCounter;

public class SmbFileTest {
	
	@Test
	public void testSimple() throws Exception{
		TimeCounter t = new TimeCounter("217");
		t.start();
		String path = "smb://administrator:Admin123456@192.168.104.217/install/ftp/新建文本文档.txt";
		SmbFile smbf = new SmbFile(path);
		InputStream in = new SmbFileInputStream(smbf);
		List<String> list = FileUtils.readAsList(in, "GBK");
		for(String str : list){
			System.out.println(str);
		}
		t.stop();
		
		t.restart("local");
		String baseDir = "smb://administrator:isoiso@192.168.168.200/share/";
		path = "/新建文本文档.txt";
		in = FileUtils.newInputStream(baseDir, path);
		list = FileUtils.readAsList(in, "GBK");
		for(String str : list){
			System.out.println(str);
		}
		t.stop();
	}
	
	@Test
	public void testWriteFile(){
		String path = "smb://administrator:Admin123456@192.168.104.217\\install\\ftp\\email-attachements";
		String fn = "test.txt";
		String content = "test中文";
		InputStream in = new StringInputStream(content);
		FileUtils.writeInputStreamTo(in, path, fn);
		
		in = FileUtils.newSmbInputStream("administrator", "Admin123456", "192.168.104.217\\install\\ftp\\email-attachements\\"+fn);
		List<String> list = FileUtils.readAsList(in);
		System.out.println("list0: " + list.get(0));
		Assert.assertEquals(content, list.get(0));
	}

}

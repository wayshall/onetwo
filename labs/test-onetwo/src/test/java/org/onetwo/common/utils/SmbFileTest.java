package org.onetwo.common.utils;

import java.io.InputStream;
import java.util.List;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

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

}

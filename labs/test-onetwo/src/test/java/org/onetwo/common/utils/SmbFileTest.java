package org.onetwo.common.utils;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

import org.apache.tools.ant.filters.StringInputStream;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.profiling.TimeCounter;
import org.onetwo.common.spring.io.SmbToByteArrayInputStreamSource;
import org.springframework.core.io.InputStreamSource;

public class SmbFileTest {

	@Test
	public void testSimple() throws Exception{

		String path = "smb://administrator:Admin123456@192.168.104.217\\install\\ftp\\新建文本文档.txt";
		try {
			FileUtils.readAsString(path);
			Assert.fail("path is error, must error");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TimeCounter t = new TimeCounter("217");
		t.start();
		path = "smb://administrator:Admin123456@192.168.104.217/install/ftp/新建文本文档.txt";
		SmbFile smbf = new SmbFile(path);
		InputStream in = new SmbFileInputStream(smbf);
		List<String> list = FileUtils.readAsList(in);
		for(String str : list){
			System.out.println(str);
		}
		t.stop();
		
		/*t.restart("local");
		String baseDir = "smb://administrator:isoiso@192.168.168.200/share/";
		path = "/新建文本文档.txt";
		in = FileUtils.newInputStream(baseDir, path);
		list = FileUtils.readAsList(in, "GBK");
		for(String str : list){
			System.out.println(str);
		}
		t.stop();*/
	}

	@Test
	public void testSmbToByteArrayInputStream() throws Exception{
		TimeCounter t = new TimeCounter("testSmbToByteArrayInputStream");
		t.start();
		String path = "smb://administrator:Admin123456@192.168.104.217/install/ftp/新建文本文档.txt";
		InputStreamSource ins = new SmbToByteArrayInputStreamSource(path);
		List<String> list = FileUtils.readAsList(ins.getInputStream());
		for(String str : list){
			System.out.println(str);
		}
		t.stop();
		
	}
	@Test
	public void testCopyToLocal() throws Exception{
		TimeCounter t = new TimeCounter("testCopyToLocal");
		t.start();
		String path = "smb://administrator:Admin123456@192.168.104.217/install/ftp/email-attachements/批量受理模板2-20140911102904-023442.xlsx";
		InputStreamSource ins = new SmbToByteArrayInputStreamSource(path);
		FileUtils.writeInputStreamTo(ins.getInputStream(), "D:/email-attachements", "smbexcel.xls");
		
	}
	
	@Test
	public void testWriteFile(){
		String path = "smb://administrator:Admin123456@192.168.104.217/install/ftp/email-attachements";
		String fn = NiceDate.New().formatAsDate()+"test.txt";
		String content = "test中文";
		InputStream in = new StringInputStream(content);
		FileUtils.writeInputStreamTo(in, path, fn);
		
		in = FileUtils.newSmbInputStream("administrator", "Admin123456", "192.168.104.217/install/ftp/email-attachements/"+fn);
		List<String> list = FileUtils.readAsList(in);
		System.out.println("list0: " + list.get(0));
		Assert.assertEquals(content, list.get(0));
		
		String smbPath = path+"\\"+fn;
		String data = FileUtils.readAsString(smbPath);
		Assert.assertEquals(content, data);
		
		String srcDir = "D:/fileutil-test/smb";
		File localFile = FileUtils.copyFileToDir(FileUtils.newSmbFile(smbPath), srcDir);
		data = FileUtils.readAsString(localFile);
		Assert.assertEquals(content, data);
	}
	

}

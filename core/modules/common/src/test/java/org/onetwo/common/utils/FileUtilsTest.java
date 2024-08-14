package org.onetwo.common.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.utils.list.JFishList;

public class FileUtilsTest {
	
	@Test
	public void testWindowsPath() {
		String path = "C:\\Program Files\\test\\1.txt";
		boolean res = FileUtils.isWindowsPath(path);
		assertThat(res).isTrue();
		
		path = "C:/Program Files/test/1.txt";
		res = FileUtils.isWindowsPath(path);
		assertThat(res).isTrue();
		
		path = "C:/Program Files/test";
		res = FileUtils.isWindowsPath(path);
		assertThat(res).isTrue();
		
		path = "/home/user/test/1.txt";
		res = FileUtils.isWindowsPath(path);
		assertThat(res).isFalse();
		
		path = "http://host/test/1.txt";
		res = FileUtils.isWindowsPath(path);
		assertThat(res).isFalse();
		
	}
	
	@Test
	public void testTextToImagic() {
		InputStream input = FileUtils.getResourceAsStream("imagic-data.txt");
		String data = FileUtils.readAsString(input);
		byte[] bytes = LangUtils.hex2Bytes(data);
		FileUtils.writeByteArrayToFile(new File("/Users/way/data/testTextToImagic.jpg"), bytes);
	}
	@Test
	public void testBomAndZwsp() throws Exception{
		String str = "testC​,string";
		boolean res = FileUtils.hasZWSP(str);
		assertThat(res).isEqualTo(res);
		assertThat(str.length()).isEqualTo(13);
		
		int zwspValue = str.codePointAt(5);
		String unicodeStr = FileUtils.toUnicodeString(zwspValue);
		System.out.println("zwsValue:"+zwspValue+", unicodeStr:"+unicodeStr);
		assertThat(unicodeStr).isEqualTo(FileUtils.toUnicodeString(FileUtils.UNICODE_ZERO_WIDTH_SPACE.codePointAt(0)));
		
		String unicodeString = FileUtils.convertZWSP2Unicode(str);
		System.out.println("unicoeString:"+unicodeString);
	}
	
	@Test
	public void testAnsi(){
//		String path = FileUtils.getResourcePath("")+"/ansi.txt";
		String path = "/ansi.txt";
		List<String> datalist = FileUtils.readAsList(path, "gbk");
		for(String data : datalist){
//			System.out.println("data: " + data);
			Assert.assertTrue(data.startsWith("非记名卡"));
		}
		
		path = FileUtils.getResourcePath("")+"/ansi.txt";
		datalist = FileUtils.readAsList(path, "gbk");
		for(String data : datalist){
//			System.out.println("data: " + data);
			Assert.assertTrue(data.startsWith("非记名卡"));
		}
	}
	

	@Test
	public void testOverride(){
		String path = FileUtils.getResourcePath("") + "/override.txt";
		File override = new File(path);
		if(override.exists()){
			override.delete();
		}
		List<String> datas = Arrays.asList("aa", "bb");
		FileUtils.writeListTo(override, "gbk", datas, "\r\n");
		List<String> res = FileUtils.readAsList(override);
		assertThat(res).isEqualTo(datas);
		
		datas = Arrays.asList("cc", "dd");
		FileUtils.writeListTo(override, "gbk", datas, "\r\n");
		res = FileUtils.readAsList(override);
		assertThat(res).isEqualTo(datas);
	}
	
	@Test
	public void testParentPath(){
		String path = "/webapp/aa/bb/cc.jsp";
		String parent = FileUtils.getParentpath(path);
		System.out.println("parent:"+parent);
		Assert.assertEquals(parent, "/webapp/aa/bb");
		
		try {
			URL url = new URL("aa.gif");
			Assert.fail();
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testReadFile(){
		String str = FileUtils.readAsString("reservation_form.tpl");
		String content = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"><html>  <head>    <title>index.html</title>    <meta http-equiv=\"keywords\" content=\"keyword1,keyword2,keyword3\">    <meta http-equiv=\"description\" content=\"this is my page\">    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">    <!--<link rel=\"stylesheet\" type=\"text/css\" href=\"./styles.css\">-->	<style>		body{width:960px;}		table{border: 1px black solid;border-collapse: collapse;margin: 0 auto;width:100%;margin: 1em;}		.title{text-align:center;}		.title span{float:right;}	</style>  </head>  <body>	<div class=\"title\"><span>订单号：XXXXX001</span><h2>住房预订单</h2></div><table border=\"1\">	<tbody>		<tr>			<td>					收件公司： {r_company}			</td>			<td>					发件公司：广东信息有限公司			</td>		</tr>		<tr>			<td>					收件人： {r_linkman}			</td>			<td>					发件人：XXX			</td>		</tr>		<tr>			<td>					电话：{r_telephone}			</td>			<td>					电话：020-37038198-分机号			</td>		</tr>		<tr>			<td>					传真：{r_fax}			</td>			<td>					传真：020-37038530			</td>		</tr>		<tr>			<td>					收件日期：			</td>			<td>					发件日期：{s_date}			</td>		</tr>		<tr>			<td colspan=\"2\">			<table>				<tr>					<td></td>				</tr>			</table>				<p>					□新增&nbsp;□变更单&nbsp;□取消单 ：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;以此单为准________________________				</p>				<p>					您好！				</p>				<p>					请按以下订单要求安排，并确认回传，谢谢！				</p>				<p>					入住姓名：&nbsp;&nbsp;张三&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;，联系方式：&nbsp; 13800138000&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;，				</p>				<p>					入住日期：2012年7月5日，退房日期：2012年7月6日，共1&nbsp;晚；				</p>				<p>					入住数量：&nbsp;&nbsp;&nbsp;&nbsp;2&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;间，入住房型：&nbsp;&nbsp;标双&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;，房号：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;；				</p>				<p>					变更：				</p>				<p>					入住姓名：&nbsp;&nbsp;张三&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;，联系方式：&nbsp; 13800138000&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;，				</p>				<p>					入住日期：2012年7月5日，退房日期：2012年7月6日，共1&nbsp;晚；				</p>				<p>					入住数量：&nbsp;&nbsp;&nbsp;&nbsp;2&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;间，入住房型：&nbsp;&nbsp;标双&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;，房号：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;；				</p>				<p>					结 算 价：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;元/间（□包房□非包房），是否含早：□是&nbsp;□否				</p>				<p>					结算方式：与我司结□现付□代收；				</p>				<p>					备注：				</p>			</td>		</tr>	</tbody></table><br />  </body></html>";
		System.out.println("testReadFile:"+str.replace("\"", "\\\""));
		Assert.assertEquals(content, str);
	}
	
	@Test
	public void testReadFileWithVar(){
		String str = FileUtils.readAsStringWith("test.txt", "startDate", "2012-12-25", "userName", "way");
		System.out.println("test:" + str);
		String content = "测试文件测试文件测试文件测试文件测试文件2012-12-25测试文件测试文件测试文件way测试文件测试文件测试文件测试文件way测试文件测试文件测试文件测试文件测试文件";
//		System.out.println("parent:"+str.replace("\"", "\\\""));
		Assert.assertEquals(content, str);
	}
	
	@Test
	public void testListFile(){
		Map<String, File> map = FileUtils.listAsMap(FileUtils.getResourcePath(ClassUtils.getDefaultClassLoader(), "sql"), ".sql");
		System.out.println("map: " + map);
	}
	
	@Test
	public void testRegex(){
		String path = "E:\\mydev\\java_workspace\\new_yooyo\\mall\\assist\\src\\main\\java\\net\\yooyo\\mall\\assist\\model\\service\\impl\\SmsSendServiceImpl.java";
		Pattern p = Pattern.compile("^.+(Controller|Params|Data|Result)\\.java$");
		boolean rs = p.matcher(path).matches();
		System.out.println("rs: " + rs);
		System.out.println("rs: " + p.matcher(path).find());
		Assert.assertFalse(rs);
		
		path = "E:\\mydev\\java_workspace\\new_yooyo\\mall\\assist\\src\\main\\java\\net\\yooyo\\mall\\assist\\model\\service\\impl\\SmsSendController.java";
		rs = p.matcher(path).matches();
		System.out.println("c: " + rs);
		System.out.println("c: " + p.matcher(path).find());
		Assert.assertTrue(rs);
	}
	
	@Test
	public void testZipfile(){
		String dir = FileUtils.getResourcePath("")+"/ziptest";
		File zipfile = FileUtils.zipfile(new File(dir));
		System.out.println("zipfile: " + zipfile.getPath());
		System.out.println("zipfile: " + zipfile.getName());
		Assert.assertEquals("ziptest.zip", zipfile.getName());
	}
	
	
	@Test
	public void testGetNewFilenameBy(){
		String filepath = FileUtils.getResourcePath("")+"/org/onetwo/common/excel/bus_copy.xls";
		String zippath = FileUtils.getNewFilenameBy(new File(filepath), ".zip");
		System.out.println("zippath: " + zippath);
		Assert.assertTrue(zippath.endsWith("\\org\\onetwo\\common\\excel\\bus_copy.zip"));
	}
	
	@Test
	public void testSize(){
		String filepath = FileUtils.getResourcePath("")+"test-for-size.txt";
		Double size = Double.valueOf(FileUtils.size(new File(filepath)));
		System.out.println("size: " +size);
		Assert.assertEquals(1372962, size.longValue());
		
		size = FileUtils.sizeAsKb(new File(filepath));
		System.out.println("size kb: " +size);
		Assert.assertEquals(1340, size.longValue());
		
		size = FileUtils.sizeAsMb(new File(filepath));
		System.out.println("size mb: " +size);
		Assert.assertEquals(1, size.longValue());
	}
	
	@Test
	public void testWriteStringList(){
		String filepath = FileUtils.getResourcePath("")+"/org/onetwo/common/fileutils/string-list.txt";
		File file = new File(filepath);
		List<String> datas = Arrays.asList("aaa", "bbb", "cccc");
		FileUtils.writeListTo(file, datas);
		
		String actual = JFishList.wrap(FileUtils.readAsList(file)).join(",", ":this");
		Assert.assertEquals("aaa,bbb,cccc", actual);
		file.delete();
		
		file = new File(filepath);
		datas = Arrays.asList("1111", "2222", "cccc");
		OutputStream output = FileUtils.openOutputStream(file);
		FileUtils.writeListToWithClose(output, null, datas);
		actual = JFishList.wrap(FileUtils.readAsList(file)).join(",", ":this");
		Assert.assertEquals("1111,2222,cccc", actual);
		file.delete();
	}
	
	@Test
	public void testReadTxtFile(){
//		String filepath = FileUtils.getResourcePath("")+"/org/onetwo/common/fileutils/user-data.txt";
//		FileUtils.readAsList(filepath, UserEntity.class, "\t");
	}
	
	@Test
	public void testNewFileNameByDateAndRand(){
		String fn = "testfile.java";
		String newfn = FileUtils.newFileNameByDateAndRand(fn);
		System.out.println("newfn: " + newfn);
		Assert.assertTrue(newfn.startsWith("testfile"));
		Assert.assertTrue(StringUtils.split(newfn, "-").length==3);
		
		String path = this.getClass().getResource("FileUtilsTest.class").getFile();
		System.out.println("path: " + path);
		File file = new File(path);
		newfn = FileUtils.newFileNameAppendRepeatCount(file);
		System.out.println("newfn: " + newfn);
		Assert.assertEquals("FileUtilsTest(1).class", newfn);
	}
	
	@Test
	public void testCopyFile(){
		String content = "test";
		String fileName = "copy-test.txt";
		String srcDir = "D:/fileutil-test";
		File srcFile = new File(srcDir + File.separator+fileName);
		FileUtils.writeStringToFile(srcFile, content);
		String data = FileUtils.readAsString(srcFile);
		Assert.assertEquals(content, data);
		
		String targetDir = srcDir+File.separator+"/target";
		File targetFile = FileUtils.copyFileToDir(srcFile, targetDir);
		data = FileUtils.readAsString(targetFile);
		Assert.assertEquals(content, data);
	}
	
	@Test
	public void testConvertPath(){
		String dir = "D:\\attachmentdir\\web";
		String rdir = FileUtils.convertDir(dir);
		Assert.assertEquals("D:/attachmentdir/web/", rdir);

		dir = "D:\\attachmentdir\\web/2014-11-24";
		rdir = FileUtils.convertDir(dir);
		Assert.assertEquals("D:/attachmentdir/web/2014-11-24/", rdir);

		dir = "D:\\attachmentdir\\web//test//";
		rdir = FileUtils.convertDir(dir);
		Assert.assertEquals("D:/attachmentdir/web/test/", rdir);

		dir = "smb://attachmentdir\\web//test//";
		rdir = FileUtils.convertDir(dir);
		Assert.assertEquals("smb://attachmentdir/web/test/", rdir);

		dir = "smb://attachmentdir\\web//test";
		rdir = FileUtils.convertDir(dir);
		Assert.assertEquals("smb://attachmentdir/web/test/", rdir);
	}

}

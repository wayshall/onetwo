package org.onetwo.plugins.jdoc.jlexer;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.plugins.jdoc.JavaSourceScanner;
import org.onetwo.plugins.jdoc.data.JClassDoc;
import org.onetwo.plugins.jdoc.data.JFieldDoc;
import org.onetwo.plugins.jdoc.data.RestInterfaceDoc;
import org.onetwo.plugins.jdoc.utils.JDocPluginUtils;

public class JavaSourceDocScanTest {
	
	@Test
	public void testScanBaseClass(){
//		String path = "E:/mydev/java_workspace/new_yooyo/onetwo/labs/jfish-app/src/main/java/";
		String path = JDocPluginUtils.getProjectDir().getPath()+ "/src/test/java/";
		System.out.println("path:" + path);
		JavaSourceScanner scaner = new JavaSourceScanner();
		scaner.setSourceDir(path);
		List<JClassDoc> jdocs = scaner.scan("org.onetwo.plugins.jdoc.test");
		System.out.println("testScanBaseClass:" + getInfoDoc(jdocs).toString().trim());
		
		assertJDoc(jdocs);
	}
	
	private void assertJDoc(List<JClassDoc> jdocs){
		JClassDoc infoDoc = getInfoDoc(jdocs);
		Assert.assertEquals("org.onetwo.plugins.jdoc.test", infoDoc.getPackageName());
		Assert.assertEquals("InfoController", infoDoc.getName());
//		Assert.assertEquals("用户信息管理类", infoDoc.getDescription());
		Assert.assertTrue(infoDoc.getFields().size()==2);
		Assert.assertFalse(infoDoc.getMethods().isEmpty());
		RestInterfaceDoc mdoc = (RestInterfaceDoc)infoDoc.getMethods().get(0);
		Assert.assertEquals("save", mdoc.getName());
//		Assert.assertEquals("根据传入保存会员信息，如果id非0，先获取会员实体，把非空的字段赋值保存。", mdoc.getDescription());
		Assert.assertEquals("post", mdoc.getHttpMethod());
		Assert.assertEquals("/inner/member_info_save", mdoc.getUrl());
		
		Assert.assertEquals(2, mdoc.getParams().size());
		JFieldDoc fdoc = mdoc.getParams().get(0);
		Assert.assertEquals("userInfo", fdoc.getName());
		Assert.assertEquals("org.onetwo.plugins.jdoc.UserParams", fdoc.getTypeName());
		Assert.assertEquals(true, fdoc.isRequired());
		
		Assert.assertEquals(3, mdoc.getErrorCodes().size());
		Assert.assertEquals("biz_er_logincode_repeat", mdoc.getErrorCodes().get(0).getCode());
		Assert.assertEquals("biz_er_mobile_repeat", mdoc.getErrorCodes().get(1).getCode());
		Assert.assertEquals("biz_er_email_repeat", mdoc.getErrorCodes().get(2).getCode());
	}
	
	private JClassDoc getInfoDoc(List<JClassDoc> docs){
		return getDoc(docs, "InfoController");
	}
	
	private JClassDoc getDoc(List<JClassDoc> docs, String name){
		for(JClassDoc doc : docs){
			if(doc.getName().equals(name)){
				return doc;
			}
		}
		return null;
	}

}

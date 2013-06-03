package org.onetwo.plugins.jdoc.jlexer;

import java.io.File;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.plugins.jdoc.Lexer.DocDirective;
import org.onetwo.plugins.jdoc.Lexer.defined.AnnotationDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.ClassDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.FieldDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.JavaClassDefineImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.MethodDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.parser.JavaSourceParser;
import org.onetwo.plugins.jdoc.utils.JDocPluginUtils;

public class JParserTest {

	private JavaSourceParser parser;

	@Test
	public void testParseInfoController(){
		String path = JDocPluginUtils.getProejctSrcTestDir() + "/org/onetwo/plugins/jdoc/test/InfoController.java";
		File file = new File(path);
		parser = new JavaSourceParser(file);
		JavaClassDefineImpl jcd = parser.parse(null);

		ClassDefinedImpl pubClass = jcd.getPublicClassDefine();
		System.out.println("class doc: " + jcd.getPublicClassDefine().getDocument());
		
		Assert.assertNotNull(jcd.getPackageDefine());
		Assert.assertEquals("org.onetwo.plugins.jdoc.test", jcd.getPackageDefine().getName());
		
		Assert.assertTrue(jcd.getImports().size()==10);
		Assert.assertEquals("java.net.MalformedURLException", jcd.getImports().get(0).getName());
		
		Assert.assertTrue(pubClass.getAnnotations().size()==3);
		AnnotationDefinedImpl anno = pubClass.getAnnotationDefined("RequestMapping");
		Assert.assertNotNull(anno);
		Assert.assertEquals("\"/inner\"", anno.getAttribute("value"));
		Assert.assertNotNull(anno.getAttribute("method"));
		Assert.assertEquals("{RequestMethod.GET,RequestMethod.POST}", anno.getAttribute("method"));
		AnnotationDefinedImpl cache = jcd.getPublicClassDefine().getAnnotationDefined("Cacheable");
		Assert.assertNotNull(cache);
		Assert.assertEquals("111", cache.getAttribute("expire"));
		
		Assert.assertNotNull(jcd.getPublicClassDefine());
		Assert.assertEquals("InfoController", jcd.getPublicClassDefine().getName());
		
		Assert.assertEquals(2, jcd.getPublicClassDefine().getMethods().size());
		MethodDefinedImpl save = jcd.getPublicClassDefine().getMethods().get(0);
		Assert.assertEquals("save", save.getName());
		Assert.assertEquals(2, save.getParameters().size());
		Assert.assertEquals(2, save.getAnnotations().size());
		Assert.assertEquals(3, save.getDocument().getDirectiveInfos(DocDirective.THROWS).size());
		
		String rvalue = save.getAnnotationAttribute("RequestMapping", "value");
		Assert.assertEquals("\"/member_info_save\"", rvalue);
		System.out.println("save doc: " + save.getDocument());
		
		MethodDefinedImpl save2 = jcd.getPublicClassDefine().getMethods().get(1);
		Assert.assertEquals("save2", save2.getName());
		System.out.println("save2 doc: " + save2.getDocument());
		Assert.assertEquals(2, save2.getParameters().size());
	}
	
	@Test
	public void testParseTeamController(){
		String path = JDocPluginUtils.getProejctSrcTestDir() + "/org/onetwo/plugins/jdoc/test/TeamController.java";
		File file = new File(path);
		parser = new JavaSourceParser(file);
		JavaClassDefineImpl jcd = parser.parse(null);

		ClassDefinedImpl pubClass = jcd.getPublicClassDefine();
		System.out.println("class doc: " + jcd.getPublicClassDefine().getDocument());
		
		Assert.assertNotNull(jcd.getPackageDefine());
		Assert.assertEquals("org.onetwo.plugins.jdoc.test", jcd.getPackageDefine().getName());
		
		Assert.assertTrue(jcd.getImports().size()==18);
		Assert.assertEquals("java.util.HashMap", jcd.getImports().get(0).getName());
		
		Assert.assertTrue(pubClass.getAnnotations().size()==2);
		AnnotationDefinedImpl anno = pubClass.getAnnotationDefined("RequestMapping");
		Assert.assertNotNull(anno);
		Assert.assertEquals("\"/inner\"", anno.getAttribute("value"));
		
		Assert.assertNotNull(jcd.getPublicClassDefine());
		Assert.assertEquals("TeamController", jcd.getPublicClassDefine().getName());
		
		Assert.assertEquals(3, jcd.getPublicClassDefine().getMethods().size());
		MethodDefinedImpl save = jcd.getPublicClassDefine().getMethods().get(0);
		Assert.assertEquals("save", save.getName());
		Assert.assertEquals(1, save.getParameters().size());
		Assert.assertEquals(1, save.getAnnotations().size());
		Assert.assertEquals(1, save.getDocument().getDirectiveInfos(DocDirective.THROWS).size());
		
		String rvalue = save.getAnnotationAttribute("RequestMapping", "value");
		Assert.assertEquals("\"/route_team_save\"", rvalue);
		System.out.println("save doc: " + save.getDocument());
		
		MethodDefinedImpl teamFetch = jcd.getPublicClassDefine().getMethods().get(1);
		Assert.assertEquals("teamFetch", teamFetch.getName());
		System.out.println("save2 doc: " + teamFetch.getDocument());
		Assert.assertEquals(1, teamFetch.getParameters().size());
	}
	
	@Test
	public void testParseLoginParams(){
		String path = JDocPluginUtils.getProejctSrcTestDir() + "/org/onetwo/plugins/jdoc/test/LoginParams.java";
		File file = new File(path);
		parser = new JavaSourceParser(file);
		JavaClassDefineImpl jcd = parser.parse(null);

		ClassDefinedImpl pubClass = jcd.getPublicClassDefine();
		
		Assert.assertNotNull(jcd.getPackageDefine());
		Assert.assertEquals("org.onetwo.plugins.jdoc.test", jcd.getPackageDefine().getName());
		
		Assert.assertTrue(jcd.getImports().size()==4);
		Assert.assertEquals("java.io.Serializable", jcd.getImports().get(0).getName());
		
		Assert.assertTrue(pubClass.getAnnotations().size()==1);
		AnnotationDefinedImpl anno = pubClass.getAnnotationDefined("JFishEntity");
		Assert.assertNotNull(anno);
		Assert.assertEquals("\"t_user\"", anno.getAttribute("table"));
		
		Assert.assertNotNull(jcd.getPublicClassDefine());
		Assert.assertEquals("LoginParams", jcd.getPublicClassDefine().getName());
		
		Assert.assertEquals(16, jcd.getPublicClassDefine().getFields().size());
		FieldDefinedImpl field = jcd.getPublicClassDefine().getField("member_type");
		Assert.assertNotNull(field);
		Assert.assertEquals("Integer", field.getDeclareType());
		Assert.assertEquals(1, field.getAnnotations().size());
		AnnotationDefinedImpl notNull = field.getAnnotationDefined("NotNull");
		Assert.assertNotNull(notNull);
	}
	
	@Test
	public void testSendSmsResponse(){
		String path = JDocPluginUtils.getProejctSrcTestDir() + "/org/onetwo/plugins/jdoc/test/SendSmsResponse.java";
		File file = new File(path);
		parser = new JavaSourceParser(file);
		JavaClassDefineImpl jcd = parser.parse(null);

		ClassDefinedImpl pubClass = jcd.getPublicClassDefine();
		
		Assert.assertNotNull(jcd.getPackageDefine());
		Assert.assertEquals("org.onetwo.plugins.jdoc.test", jcd.getPackageDefine().getName());
		
		Assert.assertTrue(jcd.getImports().size()==3);
		Assert.assertEquals("javax.xml.bind.annotation.XmlAccessType", jcd.getImports().get(0).getName());
		
		Assert.assertTrue(pubClass.getAnnotations().size()==2);
		AnnotationDefinedImpl anno = pubClass.getAnnotationDefined("XmlAccessorType");
		Assert.assertNotNull(anno);
		Assert.assertEquals("XmlAccessType.FIELD", anno.getAttribute("value"));
		
		Assert.assertNotNull(jcd.getPublicClassDefine());
		Assert.assertEquals("SendSmsResponse", jcd.getPublicClassDefine().getName());
		
	}
	
}

package org.onetwo.plugins.jdoc.jlexer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.plugins.jdoc.JDocContext;
import org.onetwo.plugins.jdoc.Lexer.JavadocManager;
import org.onetwo.plugins.jdoc.data.JClassDoc;
import org.onetwo.plugins.jdoc.test.InfoController;
import org.onetwo.plugins.jdoc.utils.JDocPluginUtils;

public class JavadocManagerTest {

	JavadocManager javadocManager;
	private JDocContext context = new JDocContext();
	
	
	@Before
	public void setup(){
		javadocManager = context.javadocManager();
		javadocManager.setPathToScan(JDocPluginUtils.getProejctSrcTestDir());
	}
	
	@Test
	public void testJavadoc(){
		javadocManager.startScanDoc();
		JClassDoc jdoc = javadocManager.findClassDocByName(InfoController.class.getName());

		Assert.assertNotNull(jdoc);
		Assert.assertEquals("org.onetwo.plugins.jdoc.test", jdoc.getPackageName());
		
	}
}

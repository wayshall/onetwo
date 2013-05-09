package org.onetwo.plugins.jdoc.jlexer;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.plugins.jdoc.Lexer.DocDirective;
import org.onetwo.plugins.jdoc.Lexer.DocDirectiveInfo;
import org.onetwo.plugins.jdoc.Lexer.DocumentInfo;

public class DocumentInfoTest {
	
	DocumentInfo doc ;
	
	@Test
	public void testString(){
		String text = "策划师啊打发打发@param param1 -param2-param3 param4 @return return\nbbbbbbbbbbb adfsdfasd";
		doc = DocumentInfo.create(text);
		Assert.assertEquals("策划师啊打发打发", doc.getDocument());
		Assert.assertEquals(2, doc.getDirectives().size());
		DocDirectiveInfo param = doc.getDirectiveInfo(DocDirective.PARAM);
		Assert.assertNotNull(param);
		Assert.assertEquals(" param1 -param2-param3 param4 ", param.getDescription());
		param = doc.getDirectiveInfo(DocDirective.RETURN);
		Assert.assertNotNull(param);
	}

}

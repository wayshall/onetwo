package org.onetwo.plugins.jdoc.jlexer;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.lexer.FileSourceReader;
import org.onetwo.common.lexer.JLexerUtils;
import org.onetwo.common.lexer.StringSourceReader;
import org.onetwo.plugins.jdoc.Lexer.JLexer;
import org.onetwo.plugins.jdoc.Lexer.JToken;
import org.onetwo.plugins.jdoc.utils.JDocPluginUtils;

public class JLexerTest {
	
	private JLexer jlexer;
	
	@Test
	public void testJLexer(){
		String str = "package org.onetwo.plugins.jdoc.jlexer;";
		System.out.println("str length: " + str.length());
		this.jlexer = new JLexer(new StringSourceReader(str));
		int index = 0;
		while(jlexer.nextToken()){
			System.out.println(jlexer.getToken()+":"+jlexer.getStringValue());
			if(index==0){
				Assert.assertEquals(jlexer.getToken(), JToken.PACKAGE);
			}else if(index==1){
				Assert.assertEquals(jlexer.getToken(), JToken.IDENTIFIER);
				Assert.assertEquals("org", jlexer.getStringValue());
			}else if(index==2){
				Assert.assertEquals(jlexer.getToken(), JToken.DOT);
			}else if(index==3){
				Assert.assertEquals(jlexer.getToken(), JToken.IDENTIFIER);
				Assert.assertEquals("onetwo", jlexer.getStringValue());
			}else if(index==4){
				Assert.assertEquals(jlexer.getToken(), JToken.DOT);
			}else if(index==5){
				Assert.assertEquals(jlexer.getToken(), JToken.IDENTIFIER);
				Assert.assertEquals("plugins", jlexer.getStringValue());
			}else if(index==6){
				Assert.assertEquals(jlexer.getToken(), JToken.DOT);
			}else if(index==7){
				Assert.assertEquals(jlexer.getToken(), JToken.IDENTIFIER);
				Assert.assertEquals("jdoc", jlexer.getStringValue());
			}else if(index==8){
				Assert.assertEquals(jlexer.getToken(), JToken.DOT);
			}else if(index==9){
				Assert.assertEquals(jlexer.getToken(), JToken.IDENTIFIER);
				Assert.assertEquals("jlexer", jlexer.getStringValue());
			}else if(index==10){
				Assert.assertEquals(jlexer.getToken(), JToken.SEMI);
			}
			index++;
		}
	}
	
	@Test
	public void testInfoController(){
		String file = JDocPluginUtils.getProejctSrcTestDir() + "/org/onetwo/plugins/jdoc/test/InfoController.java";
		this.jlexer = new JLexer(new FileSourceReader(new File(file)));
		while(jlexer.nextToken()){
			System.out.println(jlexer.getToken()+":" + jlexer.getStringValue());
		}
	}
	
	@Test
	public void testEOI(){
		System.out.println(JLexerUtils.EOI);
	}

}

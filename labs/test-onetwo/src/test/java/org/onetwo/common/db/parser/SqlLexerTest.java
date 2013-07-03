package org.onetwo.common.db.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.lexer.StringSourceReader;

public class SqlLexerTest {

	SqlLexer lexer = null;

	@Before
	public void setup(){
	}
	
	@Test
	public void testSqlLexer(){
		String str = "select count(*) from t_user t where t.age >= ? and t.nick_name='test nick_name' and t.birth_day = :birth_day";
		this.lexer = new SqlLexer(new StringSourceReader(str));
		int index = 0;
		while(lexer.nextToken()){
			System.out.println("" + lexer.getToken()+": " + lexer.getStringValue());
			if(index==0){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.SELECT);
				Assert.assertEquals("select", lexer.getStringValue());
			}else if(index==1){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.IDENTIFIER);
				Assert.assertEquals("count", lexer.getStringValue());
			}else if(index==2){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.LPARENT);
			}else if(index==3){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.START);
				Assert.assertEquals("*", lexer.getStringValue());
			}else if(index==4){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.RPARENT);
			}else if(index==5){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.FROM);
				Assert.assertEquals("from", lexer.getStringValue());
			}else if(index==6){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.IDENTIFIER);
				Assert.assertEquals("t_user", lexer.getStringValue());
			}else if(index==7){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.IDENTIFIER);
				Assert.assertEquals("t", lexer.getStringValue());
			}else if(index==8){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.WHERE);
				Assert.assertEquals("where", lexer.getStringValue());
			}else if(index==9){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.IDENTIFIER);
				Assert.assertEquals("t.age", lexer.getStringValue());
			}else if(index==10){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.GTEQ);
			}else if(index==11){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.QUESTION);
			}else if(index==12){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.AND);
			}else if(index==13){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.IDENTIFIER);
				Assert.assertEquals("t.nick_name", lexer.getStringValue());
			}else if(index==14){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.EQ);
			}else if(index==15){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.STRING);
			}else if(index==16){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.AND);
			}else if(index==17){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.IDENTIFIER);
				Assert.assertEquals("t.birth_day", lexer.getStringValue());
			}else if(index==18){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.EQ);
			}else if(index==19){
				Assert.assertEquals(lexer.getToken(), SqlTokenKey.VARNAME);
				Assert.assertEquals(":birth_day", lexer.getStringValue());
			}
			index++;
		}
	}
	
}

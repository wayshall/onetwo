package org.onetwo.common.utils;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class StringSpliterTest {
	private static final List<String> OPERATORS = CUtils.trimAsList("=", "<", ">", ">=", "<=", "!=", "<>", "in", "like");
	private static final List<String> RETAIN_SEPS = CUtils.asList(" ", ",", "(", ")", "=", "<", ">", ">=", "<=", "!=", "<>", " in", " like ");//seperator
	
	@Test
	public void testSql(){
		String originalSql = "select * from t_user t where t.age >= ? and t.birth_day <= :birth_day and t.user_name = :userName";
		StringSpliter sg = StringSpliter.wrap(originalSql, null, RETAIN_SEPS);
		String[] strs = sg.getArrays();
		
		LangUtils.println("tokens: ", strs);
	}
	
	@Test
	public void testSplit(){
		List<String> strs = StringUtils.splitWithRetainSeparator("aaa bbb:cc|dd", " ", ":|");
		System.out.println("testSplit str: " + strs);
		Assert.assertEquals(6, strs.size());
	}
	@Test
	public void testBlank(){
		StringSpliter g = StringSpliter.wrap(" |testa|合作商家2|      ", "|");
		System.out.println("testBlank:"+g);
		Assert.assertTrue(g.contails("合作商家2"));
		Assert.assertTrue(g.contails("      "));
		Assert.assertEquals("testa|合作商家2", g.removeBlank().toString());
		g = StringSpliter.wrap(" |      ", "|");
		System.out.println("str: " + g.toString());
	}

	@Test
	public void testArray(){
		StringSpliter g = StringSpliter.wrap("合作商家|testa|合作商家2|testb", "|");
		Assert.assertTrue(g.contails("合作商家"));
		Assert.assertFalse(g.contails("合作商家3"));
		Assert.assertEquals("合作商家|testa|合作商家2|testb", g.addIfNotExist("testa").toString());
		Assert.assertEquals("合作商家|testa|合作商家2|testb|testc", g.addIfNotExist("testc").toString());
	}

	@Test
	public void testStr(){
		StringSpliter g = StringSpliter.wrap("合作商家|testa:合作商家2;testb", "|", ":", ";");
		System.out.println("testStr: " + g.toString());
		Assert.assertTrue(g.contails("合作商家"));
		Assert.assertFalse(g.contails("合作商家3"));
		g.addIfNotExist("testa");
		Assert.assertEquals("合作商家|testa|合作商家2|testb", g.toString());
		Assert.assertEquals("合作商家|testa|合作商家2|testb|testc", g.addIfNotExist("testc").toString());
	}

	@Test
	public void testRetainSeperator(){
		StringSpliter g = StringSpliter.wrap("select * from table where a=:aname and (c=:cname or d=:dname)", " ", "&(", "&)", "&=").removeBlank();
		System.out.println("str: " + g.toString());
		Assert.assertEquals("select * from table where a = :aname and ( c = :cname or d = :dname )", g.toString());
	}
}

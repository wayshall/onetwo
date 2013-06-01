package org.onetwo.common.utils;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.list.L;

@SuppressWarnings("unchecked")
public class StringerTest {
	private static final List<String> OPERATORS = L.aslist("=", "<", ">", ">=", "<=", "!=", "<>", "in", "like");
	private static final List<String> RETAIN_SEPS = L.aslistIfNull(false, " ", ",", "(", ")", "=", "<", ">", ">=", "<=", "!=", "<>", " in", " like ");//seperator
	
	@Test
	public void testSql(){
		String originalSql = "select * from t_user t where t.age >= ? and t.birth_day <= :birth_day and t.user_name = :userName";
		Stringer sg = Stringer.wrap(originalSql, null, RETAIN_SEPS);
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
		Stringer g = Stringer.wrap(" |testa|合作商家2|      ", "|");
		System.out.println("testBlank:"+g);
		Assert.assertTrue(g.contails("合作商家2"));
		Assert.assertTrue(g.contails("      "));
		Assert.assertEquals("testa|合作商家2", g.removeBlank().toString());
		g = Stringer.wrap(" |      ", "|");
		System.out.println("str: " + g.toString());
	}

	@Test
	public void testArray(){
		Stringer g = Stringer.wrap("合作商家|testa|合作商家2|testb", "|");
		Assert.assertTrue(g.contails("合作商家"));
		Assert.assertFalse(g.contails("合作商家3"));
		Assert.assertEquals("合作商家|testa|合作商家2|testb", g.addIfNotExist("testa").toString());
		Assert.assertEquals("合作商家|testa|合作商家2|testb|testc", g.addIfNotExist("testc").toString());
	}

	@Test
	public void testStr(){
		Stringer g = Stringer.wrap("合作商家|testa:合作商家2;testb", "|", ":", ";");
		System.out.println("testStr: " + g.toString());
		Assert.assertTrue(g.contails("合作商家"));
		Assert.assertFalse(g.contails("合作商家3"));
		g.addIfNotExist("testa");
		Assert.assertEquals("合作商家|testa|合作商家2|testb", g.toString());
		Assert.assertEquals("合作商家|testa|合作商家2|testb|testc", g.addIfNotExist("testc").toString());
	}

	@Test
	public void testRetainSeperator(){
		Stringer g = Stringer.wrap("select * from table where a=:aname and (c=:cname or d=:dname)", " ", "&(", "&)", "&=").removeBlank();
		System.out.println("str: " + g.toString());
		Assert.assertEquals("select * from table where a = :aname and ( c = :cname or d = :dname )", g.toString());
	}
}

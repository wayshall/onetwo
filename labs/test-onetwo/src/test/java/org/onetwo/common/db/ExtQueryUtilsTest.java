package org.onetwo.common.db;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import test.entity.UserEntity;

public class ExtQueryUtilsTest {
	
	@Test
	public void testField2Map(){
		UserEntity user = new UserEntity();
		user.setUserName("test");
		Map map = ExtQueryUtils.field2Map(user);
		System.out.println("map: " + map);
	}
	
	@Test
	public void testBuildCountSql(){
		String sql = "SELECT   * FROM zjk_supplier t " +
				"WHERE t.add_city = :add_city AND t.home_state = :home_state  AND t.type_id = :type_id  " +
				"AND t.ID IN (SELECT p.supplier_id  FROM zjk_supplier_product p WHERE p.supplier_id = t.ID) AND t.view_grade = :view_grade " +
				"ORDER BY t.create_time, t.ID ";
		String countSql = ExtQueryUtils.buildCountSql(sql.toLowerCase(), null);
		System.out.println("countSql: " + countSql);
		
		countSql = ExtQueryUtils.buildCountSql("select * from zjk_supplier where a.bb = :cc", null);
		System.out.println("countSql: " + countSql);
	}
	
	@Test
	public void testBuildGroupByCountSql(){
		String sql = "select * from a where a.aa=:aaName group by a.aa";
		String countSql = ExtQueryUtils.buildCountSql(sql, null);
		System.out.println("countSql: " + countSql);
		String groupBy = "select count(*) from ( " + sql + " )";
		Assert.assertEquals(groupBy, countSql);
	}

}

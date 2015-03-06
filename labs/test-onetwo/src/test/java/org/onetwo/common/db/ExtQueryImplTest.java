package org.onetwo.common.db;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.db.ExtQuery.K.IfNull;
import org.onetwo.common.db.ExtQueryUtils.F;
import org.onetwo.common.db.sqlext.SQLKeys;
import org.onetwo.common.db.sqlext.SQLSymbolManagerFactory;
import org.onetwo.common.hibernate.sql.HibernateSQLSymbolManagerImpl;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.list.L;
import org.onetwo.common.utils.map.M;

import test.entity.UserEntity;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ExtQueryImplTest {

	Map<Object, Object> properties;
	
	private SQLSymbolManagerFactory sqlSymbolManagerFactory;
	
	private class Magazine {
	}

	@Before
	public void setup(){
		this.properties = new LinkedHashMap<Object, Object>();
		this.properties.put(K.DESC, "id");
		sqlSymbolManagerFactory = SQLSymbolManagerFactory.getInstance();
//		sqlSymbolManagerFactory.register(EntityManagerProvider.JPA, JPASQLSymbolManager.create());
		sqlSymbolManagerFactory.register(EntityManagerProvider.Hibernate, HibernateSQLSymbolManagerImpl.create());
	}

	@Test
	public void testFindAll(){

		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, "mag", null);
		q.build();
		
		String sql = "select mag from Magazine mag";
		String paramsting = "{}";
//		System.out.println("testFindAll: " + q.getSql().trim());
//		System.out.println("testFindAll: " + q.getParamsValue().getValues().toString());
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}
	

	@Test
	public void testNull(){
		this.properties.put("name:is null", true);
		this.properties.put("nickname:is null", false);
		this.properties.put(K.DEBUG, true);
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, "mag", properties);
		q.build();
		
		String sql = "select mag from Magazine mag where mag.name is null and mag.nickname is not null order by mag.id desc";
		String paramsting = "{}";
//		System.out.println("testNull: " + q.getSql().trim());
//		System.out.println("testNull: " + q.getParamsValue().getValues().toString());
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}
	
	@Test
	public void testNull2(){

		this.properties.put("name:=", SQLKeys.Null);
		this.properties.put("nickname:!=", SQLKeys.Null);
		this.properties.put(K.DEBUG, true);
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, "mag", properties);
		q.build();
		
		String sql = "select mag from Magazine mag where mag.name is null and mag.nickname is not null order by mag.id desc";
		String paramsting = "{}";
//		System.out.println("testNull2: " + q.getSql().trim());
//		System.out.println("testNull2: " + q.getParamsValue().getValues().toString());
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}
	
	@Test
	public void testLike(){

		this.properties.put("name:like", "way%");
		this.properties.put(K.DEBUG, true);
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, "mag", properties);
		q.build();
		
		String sql = "select mag from Magazine mag where mag.name like :mag_name0 order by mag.id desc";
		String paramsting = "{mag_name0=way%}";
//		System.out.println("testNull2: " + q.getSql().trim());
//		System.out.println("testNull2: " + q.getParamsValue().getValues().toString());
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}
	
	@Test
	public void testEmpty(){

		this.properties.put("pages:is empty", true);
		this.properties.put("types:is empty", false);
		this.properties.put(K.DEBUG, true);
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, "mag", properties);
		q.build();
		
		String sql = "select mag from Magazine mag where mag.pages is empty and mag.types is not empty order by mag.id desc";
		String paramsting = "{}";
//		System.out.println("testEmpty: " + q.getSql().trim());
//		System.out.println("testEmpty: " + q.getParamsValue().getValues().toString());
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}
	
	@Test
	public void testCommon(){
		Map params = new LinkedHashMap();
		params.put(new String[]{"id", "name"}, new Object[]{11, 2l});
		params.put(":maxResults", 222333);
		params.put("column.id:in", new Object[]{222, 111});
		params.put("userName", null);
		params.put("id:!=", new Object[] { Long.class, "id", "aa", 1, "bb", "cc" });
		params.put(":fetch", "bid");
		params.put(K.DESC, "name, text");
		params.put(K.ORDERBY, "object.nameid desc, object.textid asc");
		params.put(K.IF_NULL, IfNull.Ignore);
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Object.class, params);
		q.build();
		
//		System.out.println("testCommon:" + q.getSql().trim());
//		System.out.println("testCommon:" + q.getParamsValue().getValues().toString());
		
		String sql = "select object from Object object left join fetch object.bid where ( object.id = :object_id0 or object.name = :object_name1 ) and object.column.id in ( :object_column_id2, :object_column_id3) and object.id != ( select sub_long.id from Long sub_long where sub_long.aa = :sub_long_aa4 and sub_long.bb = :sub_long_bb5 ) order by object.name desc, object.text desc, object.nameid desc, object.textid asc";
		String paramsting = "{object_id0=11, object_name1=2, object_column_id2=222, object_column_id3=111, sub_long_aa4=1, sub_long_bb5=cc}";
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}
	
	@Test
	public void testCommonIfNullCalm(){
		properties.put(new String[]{"id", "name"}, new Object[]{11, 2l});
		properties.put("column.id:in", new Object[]{222, 111});
		properties.put("userName", "");
		properties.put(":fetch", "bid");
		properties.put(K.IF_NULL, IfNull.Calm);
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Object.class, properties);
		q.build();

//		System.out.println("testCommonIfNullCalm:" + q.getSql().trim());
//		System.out.println("testCommonIfNullCalm:" + q.getParamsValue().getValues().toString());
		String sql = "select object from Object object left join fetch object.bid where ( object.id = :object_id0 or object.name = :object_name1 ) and object.column.id in ( :object_column_id2, :object_column_id3) and object.userName = :object_userName4 order by object.id desc";
		String paramsting = "{object_id0=11, object_name1=2, object_column_id2=222, object_column_id3=111, object_userName4=}";
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}

	@Test
	public void testBetweenDate(){
		properties.put("startTime:>=", new Date());
		properties.put("startTime:<=", new Date());
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Object.class, properties);
		q.build();
		
//		System.out.println("date: " + q.getSql());
		String sql = "select object from Object object where object.startTime >= :object_startTime0 and object.startTime <= :object_startTime1 order by object.id desc ";
		Assert.assertEquals(sql.trim(), q.getSql().trim());
	}
	

	@Test
	public void testSelectMap(){
		//or 示例用法
		properties.put(K.SELECT, new Object[]{HashMap.class, "aa", "bb"});
		properties.put("aa", "bb");
		
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Object.class, properties);
		q.build();
		
		String sql = "select new map(object.aa, object.bb) from Object object where object.aa = :object_aa0 order by object.id desc ";
		String paramsting = "{object_aa0=bb}";
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
		
		properties.clear();
		properties.put(K.SELECT, new Object[]{List.class, "aa", "bb"});
		properties.put("aa", "bb");
		
		q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Object.class, properties);
		q.build();
		
		sql = "select new list(object.aa, object.bb) from Object object where object.aa = :object_aa0";
		paramsting = "{object_aa0=bb}";
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
		

		
		properties.clear();
		properties.put(K.SELECT, new Object[]{UserEntity.class, "aa", "bb"});
		properties.put("aa", "bb");
		
		q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Object.class, properties);
		q.build();
		
		sql = "select new UserEntity(object.aa, object.bb) from Object object where object.aa = :object_aa0";
		paramsting = "{object_aa0=bb}";
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
		
	}
	


	@Test
	public void testSelectAlias(){
		//or 示例用法
		properties.put(K.SELECT, new Object[]{"aa:a1", "bb:b1"});
		properties.put("aa", "bb");
		
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Object.class, properties);
		q.build();
		
		String sql = "select object.aa as a1, object.bb as b1 from Object object where object.aa = :object_aa0 order by object.id desc ";
		String paramsting = "{object_aa0=bb}";
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
		
	}


	@Test
	public void testDistinctSelect(){
		//or 示例用法
		properties.put(K.SELECT, new String[]{"aa", "bb"});
		properties.put("aa", "bb");
		
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Object.class, properties);
		q.build();
		
		String sql = "select object.aa, object.bb from Object object where object.aa = :object_aa0 order by object.id desc ";
		String paramsting = "{object_aa0=bb}";
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
		

		properties.remove(K.SELECT);
		properties.put(K.DISTINCT, new String[]{"aa", "bb"});
		properties.put("aa", "bb");
		ExtQuery q2 = sqlSymbolManagerFactory.getJPA().createSelectQuery(Object.class, properties);
		q2.build();
		String sql2 = "select distinct object.aa, object.bb from Object object where object.aa = :object_aa0 order by object.id desc ";
		Assert.assertEquals(sql2.trim(), q2.getSql().trim());
		Assert.assertEquals(paramsting, q2.getParamsValue().getValues().toString());
		
	}

	@Test
	public void testDistinctSelect2(){
		//or 示例用法
		properties.put(K.SELECT, new String[]{"cc", "aa", "bb"});
		properties.put("aa", "bb");
		properties.put(K.DISTINCT, null);
		
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Object.class, properties);
		q.build();
		
		String sql = "select distinct object.cc, object.aa, object.bb from Object object where object.aa = :object_aa0 order by object.id desc ";
		String paramsting = "{object_aa0=bb}";
		System.out.println("testDistinctSelect2:"+q.getSql());
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
		
		
	}
	
	@Test
	public void testDistinctCount(){
		properties.remove(K.SELECT);
//		properties.put(K.DISTINCT, "object");
		properties.put(K.COUNT, "object.id");
		properties.put("aa", "bb");
		ExtQuery q2 = sqlSymbolManagerFactory.getJPA().createSelectQuery(Object.class, properties);
		q2.build();
		String paramsting = "{object_aa0=bb}";
		String sql2 = "select count(object.id) from Object object where object.aa = :object_aa0 order by object.id desc";
//		System.out.println("testDistinctCount: "+q2.getSql());
		System.out.println("testDistinctCount getSql: "+q2.getSql());
//		System.out.println("testDistinctCount paramsting: "+paramsting);
		Assert.assertEquals(sql2.trim(), q2.getSql().trim());
		Assert.assertEquals(paramsting, q2.getParamsValue().getValues().toString());
		
	}

	@Test
	public void testFetch(){
		properties.put(K.FETCH, "cc");
		properties.put("aa", "bb");
		properties.put("cc", 22);
		
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Object.class, properties);
		q.build();
		
		String sql = "select object from Object object left join fetch object.cc where object.aa = :object_aa0 and object.cc = :object_cc1 order by object.id desc";
		String paramsting = "{object_aa0=bb, object_cc1=22}";
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}

	@Test
	public void testJoinFetch(){
		properties.put(K.JOIN_FETCH, new String[]{"author:auth", "pages:page"});
		properties.put("aa", "bb");
		properties.put("cc", 22);
		
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, properties);
		q.build();
		
		String sql = "select magazine from Magazine magazine join fetch magazine.author auth join fetch magazine.pages page where magazine.aa = :magazine_aa0 and magazine.cc = :magazine_cc1 order by magazine.id desc";
		String paramsting = "{magazine_aa0=bb, magazine_cc1=22}";
		
//		System.out.println("testJoinFetch: " + q.getSql().trim());
//		System.out.println("testJoinFetch: " + q.getParamsValue().getValues().toString());
		
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}
	
	@Test
	public void testJoin(){
		properties.put(K.DISTINCT, null);
		properties.put(K.JOIN, new String[]{"articles:art", K.NO_PREFIX+"art.author:auth"});
		properties.put(K.NO_PREFIX+"auth.lastName", "Grisham");

		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, "mag", properties);
		q.build();
		
		String sql = "select distinct mag from Magazine mag join mag.articles art join art.author auth where auth.lastName = :auth_lastName0 order by mag.id desc";
		String paramsting = "{auth_lastName0=Grisham}";
		
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}
	
	@Test
	public void testLeftJoin(){
		properties.put(K.DISTINCT, null);
		properties.put(K.JOIN, new String[]{"user:u", "role:r"});
		properties.put(K.LEFT_JOIN, new String[]{"articles:art", K.NO_PREFIX+"art.author:auth"});
		properties.put(K.NO_PREFIX+"auth.lastName", "Grisham");

		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, "mag", properties);
		q.build();
		
		String sql = "select distinct mag from Magazine mag join mag.user u join mag.role r left join mag.articles art left join art.author auth where auth.lastName = :auth_lastName0 order by mag.id desc";
		String paramsting = "{auth_lastName0=Grisham}";

//		System.out.println("testLeftJoin: " + q.getSql().trim());
//		System.out.println("testLeftJoin: " + q.getParamsValue().getValues().toString());
		
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}
	
	@Test
	public void testInJoin(){
		properties.put(K.JOIN_IN, "articles:art");
		properties.put(K.NO_PREFIX+"art.lastName", "Grisham");

		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, "mag", properties);
		q.build();
		
		String sql = "select mag from Magazine mag , in(mag.articles) art where art.lastName = :art_lastName0 order by mag.id desc";
		String paramsting = "{art_lastName0=Grisham}";
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}

	@Test
	public void testSqlFuncFail(){
		properties.put(K.JOIN_IN, "articles:art");
		properties.put(K.NO_PREFIX+"art.lastName", "Grisham");
		properties.put(".LOWER(name)", "way");

		try {
			ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, "mag", properties);
			q.build();
			Assert.fail("it must thorw : [ERROR]:the field is inValid : lower(name)");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Test
	public void testSqlFunc(){
		properties.put(K.SQL_QUERY, true);
		properties.put(F.sqlFunc("LOWER(name)"), "way");
		properties.put(F.sqlFunc("substring(name, 5, 1)"), "w");
		properties.put(K.IF_NULL, IfNull.Ignore);

		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, "mag", properties);
		q.build();
		
		String sql = "select mag from magazine mag where LOWER(name) = ?1 and substring(name, 5, 1) = ?2 order by mag.id desc";
		String paramsting = "[way, w]";
		
		System.out.println("testFunc sql: " + q.getSql().trim());
//		System.out.println("testFunc values: " + q.getParamsValue().getValues());
		
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}
	
	@Test
	public void testFunc(){
//		properties.put(K.SELECT, ".max(@name, @age)");
		properties.put(K.JOIN_IN, "articles:art");
		properties.put(K.NO_PREFIX+"art.lastName", "Grisham");
		properties.put("&LOWER(@name)", "way");
		properties.put("&substring(@name, 5, 1)", "w");

		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, "mag", properties);
		q.build();
		
		String sql = "select mag from Magazine mag , in(mag.articles) art where art.lastName = :art_lastName0 and lower(mag.name) = :lower_mag_name_1 and substring(mag.name, 5, 1) = :substring_mag_name_5_1_2 order by mag.id desc ";
		String paramsting = "{art_lastName0=Grisham, lower_mag_name_1=way, substring_mag_name_5_1_2=w}";
		
//		System.out.println("testFunc sql: " + q.getSql().trim());
//		System.out.println("testFunc values: " + q.getParamsValue().getValues());
		
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}
	
	@Test
	public void testFunc2(){
		properties.put(K.JOIN_IN, "articles:art");
		properties.put(K.NO_PREFIX+"art.lastName", "Grisham");
		properties.put("&LOWER(@name)", "way");
		properties.put(("&substring(@name, 5, 1)"), "w");

		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, "mag", properties);
		q.build();
		
		String sql = "select mag from Magazine mag , in(mag.articles) art where art.lastName = :art_lastName0 and lower(mag.name) = :lower_mag_name_1 and substring(mag.name, 5, 1) = :substring_mag_name_5_1_2 order by mag.id desc";
		String paramsting = "{art_lastName0=Grisham, lower_mag_name_1=way, substring_mag_name_5_1_2=w}";
//		System.out.println("testFunc2 sql: " + q.getSql().trim());
//		System.out.println("testFunc2 values: " + q.getParamsValue().getValues());
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}
	
	@Test
	public void testDateIn(){

//		System.out.println("\ntestDateIn start ==================>>>>>>>>>");
		
//		Map<Object, Object> properties = new LinkedHashMap<Object, Object>();

		properties.put("createTime:date in", new String[]{":this-year", ":this-year :end"});
		
		properties.put("createTime:date in", new String[]{":this-year", ":this-year.end"});
		
		properties.put("createTime:date in", "2011-10-27");
		

		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, "mag", properties);
		q.build();

		String sql = "select mag from Magazine mag where ( mag.createTime >= :mag_createTime0 and mag.createTime < :mag_createTime1 ) order by mag.id desc";
		String paramsting = "{mag_createTime0=Thu Oct 27 00:00:00 CST 2011, mag_createTime1=Fri Oct 28 00:00:00 CST 2011}";
//		System.out.println("aa:"+q.getParamsValue().getValues().toString());
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
		

		properties.clear();
		properties.put(K.DESC, "id");
		
		properties.put("createTime:date in", new String[]{"2011-10-27", "2011-10-28"});
		properties.put("regiestTime:date in", DateUtil.parse("2011-10-27"));
		
		q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, "mag", properties);
		q.build();

		sql = "select mag from Magazine mag where ( mag.createTime >= :mag_createTime0 and mag.createTime < :mag_createTime1 ) and ( mag.regiestTime >= :mag_regiestTime2 and mag.regiestTime < :mag_regiestTime3 ) order by mag.id desc";
		paramsting = "{mag_createTime0=Thu Oct 27 00:00:00 CST 2011, mag_createTime1=Fri Oct 28 00:00:00 CST 2011, mag_regiestTime2=Thu Oct 27 00:00:00 CST 2011, mag_regiestTime3=Fri Oct 28 00:00:00 CST 2011}";

//		System.out.println(q.getSql());
//		System.out.println((Map)q.getParamsValue().getValues());
		
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());

		properties = M.c("createTime:date in", ":today",
						"&lower(name):like", "tom%", 
						"age:=", 17, 
						"lastUpdateTime:date in", ":yesterday", 
						"regiestTime:date in", new Date());
		properties.put(K.DESC, "id");
		
		q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, "mag", properties);
		q.build();
		String str = "select mag from Magazine mag where ( mag.lastUpdateTime >= :mag_lastUpdateTime0 and mag.lastUpdateTime < :mag_lastUpdateTime1 ) and ( mag.createTime >= :mag_createTime2 and mag.createTime < :mag_createTime3 ) and lower(mag.name) like :lower_mag_name_4 and mag.age = :mag_age5 and ( mag.regiestTime >= :mag_regiestTime6 and mag.regiestTime < :mag_regiestTime7 ) order by mag.id desc ";
//		System.out.println("testDateIn:" + q.getSql());
//		System.out.println("testDateIn:" +(Map)q.getParamsValue().getValues());
		Assert.assertEquals(str.trim(), q.getSql());
		
//		System.out.println("testDateIn end ==================>>>>>>>>>\n");
	}


	@Test
	public void testAnd(){
		properties.put("name:like", "way");
		properties.put(":and", M.c(new String[]{"age"}, new Object[]{17, 18}));

		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, "mag", properties);
		q.build();

//		System.out.println("testAnd:" + q.getSql());
//		System.out.println("testAnd:" + q.getParamsValue().getValues());
		
		String sql = "select mag from Magazine mag where mag.name like :mag_name0 and ( (mag.age = :mag_age1  or mag.age = :mag_age2 ) ) order by mag.id desc";
		String paramsting = "{mag_name0=%way%, mag_age1=17, mag_age2=18}";
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}
	

	@Test
	public void testOr(){
		//or 示例用法
		Map or1 = new LinkedHashMap();
		or1.put("columnId", 111);
		
		properties.put("siteId", 1);
		properties.put("title:like", "sdsd");
		
		properties.put(K.OR, or1);
		
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Object.class, properties);
		q.build();
		
		System.out.println("testOr sql: " + q.getSql());
		
		String sql = "select object from Object object where object.siteId = :object_siteId0 and object.title like :object_title1 or ( object.columnId = :object_columnId2 ) order by object.id desc ";
		String paramsting = "{object_siteId0=1, object_title1=%sdsd%, object_columnId2=111}";
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}

	@Test
	public void testOr2(){
		//or 示例用法
		Map or1 = new LinkedHashMap();
		Map and1 = new HashMap();
		and1.put("department.id", 11);
		and1.put("columns.id", 1);
		
		or1.put("columnId", 111);
		or1.put(":or", and1);
		
		properties.put("siteId", 1);
		properties.put("title:like", "sdsd");
		
		properties.put(":and", or1);
		
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Object.class, properties);
		q.build();
		
		String sql = "select object from Object object where object.siteId = :object_siteId0 and object.title like :object_title1 and ( object.columnId = :object_columnId2 or ( object.columns.id = :object_columns_id3 and object.department.id = :object_department_id4 ) ) order by object.id desc ";
		String paramsting = "{object_siteId0=1, object_title1=%sdsd%, object_columnId2=111, object_columns_id3=1, object_department_id4=11}";
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}

	@Test
	public void testHas(){
		this.properties.put("name:is null", true);
//		this.properties.put("address:!=", Keys.Empty);
		this.properties.put("address:has", "testAddress");
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, "mag", properties);
		q.build();
		
		String sql = "select mag from Magazine mag where mag.name is null and :mag_address0 member of mag.address order by mag.id desc";
		String paramsting = "{mag_address0=testAddress}";
//		System.out.println("testHas: " + q.getSql().trim());
//		System.out.println("testHas: " + q.getParamsValue().getValues().toString());
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}
	
	@Test
	public void testSql(){
		this.properties.put("name:is null", true);
		this.properties.put(K.RAW_QL, L.aslist("author=:author and (name=:name1 or name=name2)", "author", "testAuthor", "name1", "testName", "name2", "testName2"));
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, "mag", properties);
		q.build();
		
		String sql = "select mag from Magazine mag where mag.name is null and author=:author and (name=:name1 or name=name2) order by mag.id desc";
		String paramsting = "{author=testAuthor, name1=testName, name2=testName2}";
//		System.out.println("testSql: " + q.getSql().trim());
//		System.out.println("testSql: " + q.getParamsValue().getValues().toString());
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}
	

	
	@Test
	public void testExceptionIfNullValue(){
		try {
			this.properties.put("name:", null);
			this.properties.put("nickname:", "way");
			this.properties.put(K.DEBUG, true);
			this.properties.put(K.IF_NULL, K.IfNull.Throw);
			ExtQuery q = sqlSymbolManagerFactory.getJPA().createSelectQuery(Magazine.class, "mag", properties);
			q.build();
			TestCase.fail("null value should be fail!");
		} catch (Exception e) {
			e.printStackTrace();
//			Assert.assertEquals(BaseException.Prefix+ExtQuery.Msg.THROW_IF_NULL_MSG, e.getMessage());
		}
	}
	

	@Test
	public void testDeleteQuery(){
		this.properties.put("name:", null);
		this.properties.put("nickname:not in", "way");
		this.properties.put(K.DEBUG, true);
		this.properties.put(K.IF_NULL, IfNull.Ignore);
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createDeleteQuery(Magazine.class, properties);
		q.build();
		
		String sql = "delete from Magazine magazine where magazine.nickname not in ( :magazine_nickname0)";
		String paramsting = "{magazine_nickname0=way}";
		System.out.println("testHas: " + q.getSql().trim());
//		System.out.println("testHas: " + q.getParamsValue().getValues().toString());
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}
	
}

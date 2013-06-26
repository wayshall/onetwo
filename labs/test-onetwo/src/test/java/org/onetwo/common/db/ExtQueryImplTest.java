package org.onetwo.common.db;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.onetwo.common.ejb.jpa.JPASQLSymbolManager;
import org.onetwo.common.fish.JFishSQLSymbolManagerImpl;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.list.L;
import org.onetwo.common.utils.map.M;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ExtQueryImplTest {

	Map<Object, Object> properties;
	
	private SQLSymbolManagerFactory sqlSymbolManagerFactory;
	
	private class Magazine {
	}

	@Before
	public void setup(){
		this.properties = new LinkedHashMap<Object, Object>();
		sqlSymbolManagerFactory = SQLSymbolManagerFactory.getInstance();
		sqlSymbolManagerFactory.register(EntityManagerProvider.JPA, JPASQLSymbolManager.create());
	}

	@Test
	public void testNull(){

		this.properties.put("name:is null", true);
		this.properties.put("nickname:is null", false);
		this.properties.put(K.DEBUG, true);
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Magazine.class, "mag", properties);
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
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Magazine.class, "mag", properties);
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
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Magazine.class, "mag", properties);
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
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Magazine.class, "mag", properties);
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
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Object.class, params);
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
		Map params = new LinkedHashMap();
		params.put(new String[]{"id", "name"}, new Object[]{11, 2l});
		params.put("column.id:in", new Object[]{222, 111});
		params.put("userName", "");
		params.put(":fetch", "bid");
		params.put(K.IF_NULL, IfNull.Calm);
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Object.class, params);
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
		Map params = new LinkedHashMap();
		params.put("startTime:>=", new Date());
		params.put("startTime:<=", new Date());
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Object.class, params);
		q.build();
		
//		System.out.println("date: " + q.getSql());
		String sql = "select object from Object object where object.startTime >= :object_startTime0 and object.startTime <= :object_startTime1 order by object.id desc ";
		Assert.assertEquals(sql.trim(), q.getSql().trim());
	}


	@Test
	public void testDistinctSelect(){
		//or 示例用法
		properties.put(K.SELECT, new String[]{"aa", "bb"});
		properties.put("aa", "bb");
		
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Object.class, properties);
		q.build();
		
		String sql = "select object.aa, object.bb from Object object where object.aa = :object_aa0 order by object.id desc ";
		String paramsting = "{object_aa0=bb}";
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
		

		properties.remove(K.SELECT);
		properties.put(K.DISTINCT, new String[]{"aa", "bb"});
		properties.put("aa", "bb");
		ExtQuery q2 = sqlSymbolManagerFactory.getJPA().createQuery(Object.class, properties);
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
		
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Object.class, properties);
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
		ExtQuery q2 = sqlSymbolManagerFactory.getJPA().createQuery(Object.class, properties);
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
		
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Object.class, properties);
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
		
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Magazine.class, properties);
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

		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Magazine.class, "mag", properties);
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

		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Magazine.class, "mag", properties);
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

		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Magazine.class, "mag", properties);
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
			ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Magazine.class, "mag", properties);
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

		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Magazine.class, "mag", properties);
		q.build();
		
		String sql = "select mag from magazine mag where LOWER(name) = ?1 and substring(name, 5, 1) = ?2 order by mag.id desc";
		String paramsting = "[way, w]";
		
//		System.out.println("testFunc sql: " + q.getSql().trim());
//		System.out.println("testFunc values: " + q.getParamsValue().getValues());
		
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}
	
	@Test
	public void testFunc(){
		properties.put(K.JOIN_IN, "articles:art");
		properties.put(K.NO_PREFIX+"art.lastName", "Grisham");
		properties.put("&LOWER(@name)", "way");
		properties.put("&substring(@name, 5, 1)", "w");

		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Magazine.class, "mag", properties);
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

		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Magazine.class, "mag", properties);
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
		
		Map<Object, Object> properties = new LinkedHashMap<Object, Object>();

		properties.put("createTime:date in", new String[]{":this-year", ":this-year :end"});
		
		properties.put("createTime:date in", new String[]{":this-year", ":this-year.end"});
		
		properties.put("createTime:date in", "2011-10-27");
		

		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Magazine.class, "mag", properties);
		q.build();

		String sql = "select mag from Magazine mag where ( mag.createTime >= :mag_createTime0 and mag.createTime < :mag_createTime1 ) order by mag.id desc";
		String paramsting = "{mag_createTime0=Thu Oct 27 00:00:00 CST 2011, mag_createTime1=Thu Oct 27 23:59:59 CST 2011}";
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
		

		properties.clear();
		
		properties.put("createTime:date in", new String[]{"2011-10-27", "2011-10-28"});
		properties.put("regiestTime:date in", DateUtil.parse("2011-10-27"));
		
		q = sqlSymbolManagerFactory.getJPA().createQuery(Magazine.class, "mag", properties);
		q.build();

		sql = "select mag from Magazine mag where ( mag.createTime >= :mag_createTime0 and mag.createTime < :mag_createTime1 ) and ( mag.regiestTime >= :mag_regiestTime2 and mag.regiestTime < :mag_regiestTime3 ) order by mag.id desc";
		paramsting = "{mag_createTime0=Thu Oct 27 00:00:00 CST 2011, mag_createTime1=Fri Oct 28 00:00:00 CST 2011, mag_regiestTime2=Thu Oct 27 00:00:00 CST 2011, mag_regiestTime3=Thu Oct 27 23:59:59 CST 2011}";

//		System.out.println(q.getSql());
//		System.out.println((Map)q.getParamsValue().getValues());
		
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());

		properties = M.c("createTime:date in", ":today",
						"&lower(name):like", "tom%", 
						"age:=", 17, 
						"lastUpdateTime:date in", ":yesterday", 
						"regiestTime:date in", new Date());
		
		q = sqlSymbolManagerFactory.getJPA().createQuery(Magazine.class, "mag", properties);
		q.build();
		String str = "select mag from Magazine mag where ( mag.lastUpdateTime >= :mag_lastUpdateTime0 and mag.lastUpdateTime < :mag_lastUpdateTime1 ) and ( mag.createTime >= :mag_createTime2 and mag.createTime < :mag_createTime3 ) and lower(mag.name) like :lower_mag_name_4 and mag.age = :mag_age5 and ( mag.regiestTime >= :mag_regiestTime6 and mag.regiestTime < :mag_regiestTime7 ) order by mag.id desc ";
//		System.out.println("testDateIn:" + q.getSql());
//		System.out.println("testDateIn:" +(Map)q.getParamsValue().getValues());
		Assert.assertEquals(str, q.getSql());
		
//		System.out.println("testDateIn end ==================>>>>>>>>>\n");
	}


	@Test
	public void testAnd(){
		properties.put("name:like", "way");
		properties.put(":and", M.c(new String[]{"age"}, new Object[]{17, 18}));

		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Magazine.class, "mag", properties);
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
		
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Object.class, properties);
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
		
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Object.class, properties);
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
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Magazine.class, "mag", properties);
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
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Magazine.class, "mag", properties);
		q.build();
		
		String sql = "select mag from Magazine mag where mag.name is null and author=:author and (name=:name1 or name=name2) order by mag.id desc";
		String paramsting = "{author=testAuthor, name1=testName, name2=testName2}";
//		System.out.println("testSql: " + q.getSql().trim());
//		System.out.println("testSql: " + q.getParamsValue().getValues().toString());
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting, q.getParamsValue().getValues().toString());
	}
	
	
	@Test
	public void testSqlQueryJoin(){
		properties.put(K.SQL_JOIN, F.sqlJoin("left join syn_log_supplier sup on sup.id = ent.log_supplier_id"));
		properties.put("routeName", " ");
		properties.put("log_supplier_id", 22l);
		properties.put(".sup.supplierCode", "supplierCodeValue");
		properties.put(F.sqlFunc("ceil(@syn_end_time-@syn_start_time):>="), 1l);
		properties.put(K.SQL_QUERY, true);
		ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(LogRouteEntity.class, "ent",  properties);
		q.build();
		
		/*String sql2 = "select ent.CREATE_TIME as createTime, ent.DELETE_TOUR as deleteTour, ent.FAIL_REASON as failReason, ent.FAIL_TOUR as failTour, ent.ID as id, ent.LAST_UPDATE_TIME as lastUpdateTime, ent.NEW_TOUR as newTour, ent.REPET_LOG_SUPPLIER_ID as repetLogSupplierId, ent.ROUTE_NAME as routeName, ent.STATE as state, ent.SUPPLIER_ROUTE_CODE as supplierRouteCode, ent.SYN_END_TIME as synEndTime, ent.SYN_START_TIME as synStartTime, ent.TYPE as type, ent.UPDATE_TOUR as updateTour, ent.YOOYO_ROUTE_ID as yooyoRouteId from SYN_LOG_ROUTE ent " +
				"left join syn_log_supplier sup on sup.id = ent.log_supplier_id where ent.log_supplier_id = :ent_log_supplier_id0 and sup.supplierCode = :sup_supplierCode1 and ceil(t.syn_end_time-t.syn_start_time) >= :ceil_t_syn_end_time_t_syn_start_time_2 order by ent.ID desc";
		*/
		String sql = "select ent.CREATE_TIME, ent.DELETE_TOUR, ent.FAIL_REASON, ent.FAIL_TOUR, ent.ID, ent.LAST_UPDATE_TIME, ent.NEW_TOUR, ent.REPET_LOG_SUPPLIER_ID, ent.ROUTE_NAME, ent.STATE, ent.SUPPLIER_ROUTE_CODE, ent.SYN_END_TIME, ent.SYN_START_TIME, ent.TYPE, ent.UPDATE_TOUR, ent.YOOYO_ROUTE_ID from SYN_LOG_ROUTE ent left join syn_log_supplier sup on sup.id = ent.log_supplier_id where ent.log_supplier_id = ?1 and sup.supplierCode = ?2 and ceil(ent.syn_end_time-ent.syn_start_time) >= ?3 order by ent.ID desc";
		String paramsting = "[22, supplierCodeValue, 1]";
//		System.out.println("testSqlQueryJoin: " + q.getSql().trim());
//		System.out.println("testSqlQueryJoin: " + q.getParamsValue().getValues().toString());
		Assert.assertEquals(sql.trim(), q.getSql().trim());
		Assert.assertEquals(paramsting.trim(), q.getParamsValue().getValues().toString().trim());
	}
	
	@Test
	public void testExceptionIfNullValue(){
		try {
			this.properties.put("name:", null);
			this.properties.put("nickname:", "way");
			this.properties.put(K.DEBUG, true);
			this.properties.put(K.IF_NULL, K.IfNull.Throw);
			ExtQuery q = sqlSymbolManagerFactory.getJPA().createQuery(Magazine.class, "mag", properties);
			q.build();
			TestCase.fail("null value should be fail!");
		} catch (Exception e) {
			e.printStackTrace();
//			Assert.assertEquals(BaseException.Prefix+ExtQuery.Msg.THROW_IF_NULL_MSG, e.getMessage());
		}
	}
	
	@Test
	public void testJFishExtQuery(){
		JFishSQLSymbolManagerImpl jqm = JFishSQLSymbolManagerImpl.create();
		this.properties.put("name:", null);
		this.properties.put("nickname:", "way");
		//left join table:alias on maintable.id=tur.magazin_id
		this.properties.put(K.LEFT_JOIN, CUtils.newArray("t_user_role:tur", new Object[]{"id", "tur.magazin_id"}));
		ExtQuery query = jqm.createQuery(Magazine.class, properties);
		query.build();
		String tsql = query.getSql();
		System.out.println("testJFishExtQuery:"+tsql);
		String expected = "select magazine.* from magazine magazine left join t_user_role tur on (magazine.id = tur.magazin_id) where magazine.nickname = :magazine_nickname0";
		Assert.assertEquals(expected, tsql.trim());
	}
	
	
}

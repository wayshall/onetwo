package org.onetwo.common.db.parser;

import java.util.Date;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.onetwo.common.db.sql.DynamicQuery;
import org.onetwo.common.db.sql.DynamicQueryFactory;
import org.onetwo.common.db.sqlext.SQLKeys;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.test.BaseTest;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.list.JFishList;

public class JFishDynamicQueryImplTest extends BaseTest {

	@Test
	public void testCount(){
		String sql = "select count(*) from t_user t where t.age = ? and t.birth_day = :birth_day and t.user_name = :userName";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter(0, 1);
		q.setParameter("birth_day", "2012-7-5");
		q.setParameter(2, "wayshall");
		q.compile();
		
		System.out.println("testCount sql: " + q.getTransitionSql());
//		System.out.println("values: " + q.getValues());
		
		String expected = "select count( * ) from t_user t where t.age = ? and t.birth_day = ? and t.user_name = ?";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[1, 2012-7-5, wayshall]", q.getValues().toString());
	}
	
	/*@Test
	public void testOrderBy(){
		String sql = "select count(*) from t_user t where t.age = ? and t.birth_day = :birth_day and t.user_name = :userName order by :fieldName";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter(0, 1);
		q.setParameter("birth_day", "2012-7-5");
		q.setParameter(2, "wayshall");
		q.setParameter("fieldName", "id desc");
		q.compile();
		
		System.out.println("testCount sql: " + q.getTransitionSql());
//		System.out.println("values: " + q.getValues());
		
		String expected = "select count( * ) from t_user t where t.age = ? and t.birth_day = ? and t.user_name = ?";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[1, 2012-7-5, wayshall]", q.getValues().toString());
	}*/
	

	@Test
	public void test(){
		String sql = "SELECT t.*, rs.logo_rsurl "
										  + "FROM ym_merchant t "
										  + " LEFT JOIN "
										  + " (SELECT   t.merchant_id, MAX (t.rs_url) AS logo_rsurl"
										           + " FROM ym_merchant_photo t"
										       + " GROUP BY t.merchant_id) rs ON t.ID = rs.merchant_id"
										+ " WHERE t.sign_category = :sign_category AND t.business_name like :business_name"
												+ " AND t.prm_category_id IN (SELECT mpr.ID"
		                                  + " FROM ym_merchant_prmcategory mpr"
		                            + " START WITH mpr.ID = :category_id"
		                            + " CONNECT BY PRIOR mpr.ID = mpr.parent_id)";
		
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter("sign_category", 1);
		q.setParameter("category_id", null);
		q.setParameter("business_name", null);
		q.ignoreIfNull().compile();
		

		String count = "select count(*) from ym_merchant t LEFT JOIN ( SELECT t.merchant_id, MAX( t.rs_url ) AS logo_rsurl FROM ym_merchant_photo t GROUP BY t.merchant_id ) rs ON t.ID = rs.merchant_id WHERE t.sign_category = ? AND 1=1 AND t.prm_category_id IN ( SELECT mpr.ID FROM ym_merchant_prmcategory mpr START WITH 1=1 CONNECT BY PRIOR mpr.ID = mpr.parent_id )";
//		System.out.println("sql: " + q.getCountSql());
//		System.out.println("values: " + q.getValues());
		
		Assert.assertEquals(count, q.getCountSql());
	}
	
	@Test
	public void testNullValue(){
		String sql = "select * from t_user t where t.age = ? and t.birth_day = ? and t.user_name = :userName";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter(0, 1);
		q.setParameter(1, null);
		q.setParameter(2, "wayshall");
		q.ignoreIfNull().compile();
		
//		System.out.println("testNullValue sql: " + q.getTransitionSql());
//		System.out.println("testNullValue values: " + q.getValues());

		String expected = "select * from t_user t where t.age = ? and 1=1 and t.user_name = ?";
		String count = "select count(*) from t_user t where t.age = ? and 1=1 and t.user_name = ?";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[1, wayshall]", q.getValues().toString());
		Assert.assertEquals(count, q.getCountSql());
	}
	
	@Test
	public void testSqlNullWithIn(){
		String sql = "select * from t_user t where t.age in (?) and t.birth_day=? and t.user_name in :userName";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter(0, SQLKeys.Null);
		q.setParameter(1, SQLKeys.Empty);
		q.setParameter(2, SQLKeys.Null);
		q.ignoreIfNull().compile();
		
//		System.out.println("testNullValue2 sql: " + q.getTransitionSql());
//		System.out.println("values: " + q.getValues());
		
		String expected = "select * from t_user t where t.age is null and t.birth_day = ? and t.user_name is null";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals(1, q.getValues().size());
		Assert.assertEquals("[]", q.getValues().toString());
	}
	
	@Test
	public void testThrowNullValue(){
		String sql = "select * from t_user t where t.age = ? and t.birth_day = ? and t.user_name = :userName";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter(0, 1);
		q.setParameter(1, null);
		q.setParameter(2, "wayshall");
		try {
			q.compile();
			Assert.fail("should throw null value");
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testParamValueArray(){
		String sql = "select * from t_user t where t.age in :age and t.birth_day=? and t.user_name like :userName";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter(0, new Object[]{1, 2, 3});
		q.setParameter(1, null);
		q.setParameter(2, "wayshall");
		q.ignoreIfNull().compile();

//		System.out.println("testParamValueArray sql: " + q.getTransitionSql());
//		System.out.println("testParamValueArray values: " + q.getValues());
		
		String expected = "select * from t_user t where t.age in (?, ?, ?) and 1=1 and t.user_name like ?";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[1, 2, 3, %wayshall%]", q.getValues().toString());
	}
	
	@Test
	public void testUpdateIn(){
		String sql = "update tb_route_info set state = -40 where id in :routeIds";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter("routeIds", LangUtils.newArrayList(1, 12, 13, 14, 15, 16, 33, 44));
		q.ignoreIfNull().compile();

		System.out.println("testIn sql: " + q.getTransitionSql());
		Assert.assertEquals("update tb_route_info set state = -40 where id in (?, ?, ?, ?, ?, ?, ?, ?)", q.getTransitionSql());
	}

	@Test
	public void testIn(){
		String sql = "select * from t_user t where t.age in (:age) order by t.id";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter(0, new Object[]{1, 2});
		q.ignoreIfNull().compile();

		System.out.println("testIn sql: " + q.getTransitionSql());
//		System.out.println("values: " + q.getValues());
		
		String expected = "select * from t_user t where t.age in ( ?, ? ) order by t.id";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[1, 2]", q.getValues().toString());

		sql = "select * from t_user t where t.age in (:age) order by t.id";
		q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter(0, 1);
		q.ignoreIfNull().compile();

		System.out.println("testIn sql: " + q.getTransitionSql());
//		System.out.println("values: " + q.getValues());
		
		expected = "select * from t_user t where t.age in ( ? ) order by t.id";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[1]", q.getValues().toString());
	}
	@Test
	public void testInLike(){
		String sql = "select * from t_user t where t.age in :age and t.birth_day=? and t.user_name like :userName";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter(0, new Object[]{1, 2});
		q.setParameter(1, null);
		q.setParameter(2, "%wayshall");
		q.ignoreIfNull().compile();

//		System.out.println("testInLike sql: " + q.getTransitionSql());
//		System.out.println("values: " + q.getValues());

		String expected = "select * from t_user t where t.age in (?, ?) and 1=1 and t.user_name like ?";
		String count = "select count(*) from t_user t where t.age in (?, ?) and 1=1 and t.user_name like ?";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[1, 2, %wayshall]", q.getValues().toString());
		Assert.assertEquals(count, q.getCountSql());
	}

	/*@Test
	public void testOrderby(){
		String sql = "select * from t_user t where t.age = ? and t.birth_day = ? and t.user_name = :userName";
		AnotherQuery q = AnotherQueryFactory.create(sql);
		q.setParameter(0, 1);
		q.setParameter(1, null);
		q.setParameter(2, "wayshall");
		q.asc("id", "user_id");
		q.desc("user_name");
		q.ignoreIfNull().compile();
		
//		System.out.println("sql: " + q.getTransitionSql());
//		System.out.println("values: " + q.getValues());
		
		String expected = "select * from t_user t where t.age = ? and 1=1 and t.user_name = ? order by id asc, user_id asc, user_name desc";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[1, wayshall]", q.getValues().toString());
	}*/
	
	@Test
	public void testLimited(){
		String sql = "select * from t_user t where t.age = ? and t.birth_day = ? and t.user_name = :userName";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter(0, 1);
		q.setParameter(1, null);
		q.setParameter(2, "wayshall");
		q.setFirstRecord(1).setMaxRecord(10).ignoreIfNull().compile();
		
//		System.out.println("sql: " + q.getTransitionSql());
//		System.out.println("values: " + q.getPageValues());
		
		String expected = "select * from t_user t where t.age = ? and 1=1 and t.user_name = ?";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[1, wayshall]", q.getValues().toString());
//		Assert.assertEquals("[1, wayshall, 1, 10]", q.getPageValues().toString());
	}
	
	@Test
	public void testInsert(){
		String sql = "insert T_USER(id, user_name) values (:id, :userName)";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter("id", 11l);
		q.setParameter("userName", "wayshall");
		
		q.compile();
		
//		System.out.println("testInsert sql: " + q.getTransitionSql());
		String expected = "insert T_USER( id, user_name ) values ( ?, ? )";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[11, wayshall]", q.getValues().toString());
	}

	@Test
	public void testIsNull(){
		String sql = "select * from t_user t where t.age = ? and t.birth_day != ? and t.user_name = :userName";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter(0, 11l);
		q.setParameter("userName", SQLKeys.Null);
		q.setParameter(1, SQLKeys.Null);
		
		q.ignoreIfNull().compile();
		
//		System.out.println("testIsNull sql: " + q.getTransitionSql());
		String expected = "select * from t_user t where t.age = ? and t.birth_day is not null and t.user_name is null";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[11]", q.getValues().toString());
	}
	
	@Test
	public void testOrderBy(){
		String sql = "select * from t_user t where t.age = ? and t.birth_day != ? and t.user_name = :userName";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter(0, 11l);
		q.setParameter("userName", SQLKeys.Null);
		q.setParameter(1, SQLKeys.Null);
		q.asc("id", "userName");
		
		q.ignoreIfNull().compile();
		
//		System.out.println("testOrderBy sql: " + q.getTransitionSql());
		String expected = "select * from t_user t where t.age = ? and t.birth_day is not null and t.user_name is null order by id, userName asc";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[11]", q.getValues().toString());
		
		q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter(0, 11l);
		q.setParameter("userName", SQLKeys.Null);
		q.setParameter(1, SQLKeys.Null);
		q.desc("id", "userName");
		
		q.ignoreIfNull().compile();
		
//		System.out.println("testOrderBy sql: " + q.getTransitionSql());
		expected = "select * from t_user t where t.age = ? and t.birth_day is not null and t.user_name is null order by id, userName desc";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[11]", q.getValues().toString());
		
		q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter(0, 11l);
		q.setParameter("userName", SQLKeys.Null);
		q.setParameter(1, SQLKeys.Null);
		q.desc("id", "userName");
		q.asc("t.age", "t.birth_day");
		
		q.ignoreIfNull().compile();
		
//		System.out.println("testOrderBy sql: " + q.getTransitionSql());
		expected = "select * from t_user t where t.age = ? and t.birth_day is not null and t.user_name is null order by id, userName desc, t.age, t.birth_day asc";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[11]", q.getValues().toString());
		

		try {
			q = DynamicQueryFactory.createJFishDynamicQuery(sql);
			q.setParameter(0, 11l);
			q.setParameter("userName", SQLKeys.Null);
			q.setParameter(1, SQLKeys.Null);
			q.desc("insert");
			
			q.ignoreIfNull().compile();
			Assert.fail("should be failed!");
		} catch (Exception e) {
			System.out.println("order by error: " + e.getMessage());
			Assert.assertTrue(BaseException.class.isAssignableFrom(e.getClass()));
		}
		

		try {
			q = DynamicQueryFactory.createJFishDynamicQuery(sql);
			q.setParameter(0, 11l);
			q.setParameter("userName", SQLKeys.Null);
			q.setParameter(1, SQLKeys.Null);
			q.desc(";");
			
			q.ignoreIfNull().compile();
			Assert.fail("should be failed!");
		} catch (Exception e) {
			System.out.println("order by error: " + e.getMessage());
			Assert.assertTrue(BaseException.class.isAssignableFrom(e.getClass()));
		}
	}
	
	@Test
	public void testMutilValue(){
		String sql = "select * from t_user t where t.age in ? and t.birth_day != ? and t.user_name = :userName";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter(0, new Object[]{11, 22});
		q.setParameter("userName", new Object[]{"way", "wayshall", SQLKeys.Null});
		q.setParameter(1, SQLKeys.Null);
		
		q.ignoreIfNull().compile();
		
//		System.out.println("testMutilValue sql: " + q.getTransitionSql());
		String expected = "select * from t_user t where t.age in (?, ?) and t.birth_day is not null and ( t.user_name = ? or t.user_name = ? or t.user_name is null )";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[11, 22, way, wayshall]", q.getValues().toString());
	}
	
	@Test
	public void testSqlFuntion(){
		String sql = "select * from t_user t where lower( t.age ) = :age and to_date(t.birth_day, 'yyyy-MM-dd') = :birthday and to_char(t.user_name) = :userName";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter(0, 11l);
		q.setParameter("userName", "wayshall");
		
		q.ignoreIfNull().compile();
		
//		System.out.println("testSqlFuntion sql: " + q.getTransitionSql());
		String expected = "select * from t_user t where lower( t.age ) = ? and 1=1 and to_char( t.user_name ) = ?";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[11, wayshall]", q.getValues().toString());
	}
	
	@Test
	public void testSqlFuntionInValue(){

//		JFishSqlParserManager.getInstance().setDebug(true);
		String sql = "select * from t_user t where lower( t.age ) = :age and t.birth_day=to_date(:birthday, 'yyyy-MM-dd') and t.user_name=to_char(:userName)";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter(0, 11l);
		q.setParameter("userName", "wayshall");
		
		q.ignoreIfNull().compile();
		
//		System.out.println("testSqlFuntionInValue sql: " + q.getTransitionSql());
		String expected = "select * from t_user t where lower( t.age ) = ? and 1=1 and t.user_name = to_char( ? )";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[11, wayshall]", q.getValues().toString());
		
		sql = "select count(route_id) as visit_count,ro.route_id,ri.name as route_name from " +
				"tb_route_outlink ro inner join tb_route_info ri on ro.route_id=ri.id " +
				"where ro.company_id=:company_id and " +
				"ro.click_time between to_date(:start_date, 'yyyy-mm-dd') " +
				"and to_date(:end_date, 'yyyy-mm-dd') group by ro.route_id, ri.name order by total desc";
		
		JFishSqlParserManager.getInstance().setDebug(true);
		q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter(0, 11l);
		String dateStr = "2013-06-27";
		Date date = DateUtil.parse(dateStr);
		q.setParameter("start_date", date);
		q.setParameter("end_date", date);
		
		q.ignoreIfNull().compile();
		
//		System.out.println("testSqlFuntionInValue sql: " + q.getTransitionSql());
		expected = "select count( route_id ) as visit_count,ro.route_id,ri.name as route_name from " +
				"tb_route_outlink ro inner join tb_route_info ri on ro.route_id=ri.id " +
				"where ro.company_id = ? and " +
				"ro.click_time between to_date( ?, 'yyyy-mm-dd') " +
				"and to_date( ?, 'yyyy-mm-dd') group by ro.route_id, ri.name order by total desc";
		System.out.println("q.getTransitionSql(): " + q.getTransitionSql());
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[11, 2013-06-27, 2013-06-27]", q.getValues().toString());
	}
	
	@Test
	public void testSqlFuntionInValue2(){
		String sql = "select af.customer_name,af.mobile,af.email,af.state,ae.* from  ym_apply_form af left join ym_apply_extend ae on af.id=ae.id where af.state=:state and ae.apply_no=:apply_no and af.customer_name like :customer_name and af.mobile like :mobile and ae.pay_flag=:payFlag and ae.yooyo_check_flag=:yooyo_check_flag and ae.to_ym_flag=:to_ym_flag and ae.ym_check_flag=:ym_check_flag and af.create_time>=to_date(:mintime,'yyyy-mm-dd hh24:mi:ss') and af.create_time<=to_date(:maxtime,'yyyy-mm-dd hh24:mi:ss') order by ae.create_time desc,ae.id desc";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter("maxtime", new Date());
		q.ignoreIfNull().compile();
		
//		System.out.println("testSqlFuntionInValue2 sql: " + q.getTransitionSql());
		String expected = "select af.customer_name, af.mobile, af.email, af.state, ae.* from ym_apply_form af left join ym_apply_extend ae on af.id = ae.id where 1=1 and 1=1 and 1=1 and 1=1 and 1=1 and 1=1 and 1=1 and 1=1 and 1=1 and af.create_time <= to_date( ?, 'yyyy-mm-dd hh24:mi:ss' ) order by ae.create_time desc , ae.id desc";
		Assert.assertEquals(expected, q.getTransitionSql());
//		Assert.assertEquals("[]", q.getValues().toString());
	}
	
	@Test
	public void testSqlFuntionInValueCache(){
		String sql = "select af.customer_name,af.mobile,af.email,af.state,ae.* from  ym_apply_form af left join ym_apply_extend ae on af.id=ae.id where af.state=:state and ae.apply_no=:apply_no and af.customer_name like :customer_name and af.mobile like :mobile and ae.pay_flag=:payFlag and ae.yooyo_check_flag=:yooyo_check_flag and ae.to_ym_flag=:to_ym_flag and ae.ym_check_flag=:ym_check_flag and af.create_time>=to_date(:mintime,'yyyy-mm-dd hh24:mi:ss') and af.create_time<=to_date(:maxtime,'yyyy-mm-dd hh24:mi:ss') order by ae.create_time desc,ae.id desc";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter("maxtime", DateUtil.date("2012-8-17"));
		q.ignoreIfNull().compile();
		
//		System.out.println("testSqlFuntionInValue2 sql: " + q.getTransitionSql());
//		System.out.println("testSqlFuntionInValue2 sql: " + q.getValues().toString());
		String expected = "select af.customer_name, af.mobile, af.email, af.state, ae.* from ym_apply_form af left join ym_apply_extend ae on af.id = ae.id where 1=1 and 1=1 and 1=1 and 1=1 and 1=1 and 1=1 and 1=1 and 1=1 and 1=1 and af.create_time <= to_date( ?, 'yyyy-mm-dd hh24:mi:ss' ) order by ae.create_time desc , ae.id desc";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[Fri Aug 17 00:00:00 CST 2012]", q.getValues().toString());
	}
	
	@Test
	public void testSqlFuntionInValueCache2(){
		String sql = "select af.customer_name,af.mobile,af.email,af.state,ae.* from  ym_apply_form af left join ym_apply_extend ae on af.id=ae.id where af.state=:state and ae.apply_no=:apply_no and af.customer_name like :customer_name and af.mobile like :mobile and ae.pay_flag=:payFlag and ae.yooyo_check_flag=:yooyo_check_flag and ae.to_ym_flag=:to_ym_flag and ae.ym_check_flag=:ym_check_flag and af.create_time>=to_date(:mintime,'yyyy-mm-dd hh24:mi:ss') and af.create_time<=to_date(:maxtime,'yyyy-mm-dd hh24:mi:ss') order by ae.create_time desc,ae.id desc";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter("state", 1);
		q.setParameter("maxtime", DateUtil.date("2012-8-17"));
		q.ignoreIfNull().compile();
		
//		System.out.println("testSqlFuntionInValue2 sql: " + q.getTransitionSql());
		String expected = "select af.customer_name, af.mobile, af.email, af.state, ae.* from ym_apply_form af left join ym_apply_extend ae on af.id = ae.id where af.state = ? and 1=1 and 1=1 and 1=1 and 1=1 and 1=1 and 1=1 and 1=1 and 1=1 and af.create_time <= to_date( ?, 'yyyy-mm-dd hh24:mi:ss' ) order by ae.create_time desc , ae.id desc";
		Assert.assertEquals(expected, q.getTransitionSql());
		Assert.assertEquals("[1, Fri Aug 17 00:00:00 CST 2012]", q.getValues().toString());
	}
	
	@Test
	public void testSubSql(){
		String sql = "select * from (select w.* from WEIBO_USER w where w.YOOYO_STATE = 50 and w.exp is not null and w.tweetnum is not null order by w.exp, w.tweetnum desc) where rownum < 6";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
//		q.setParameter("state", 1);
//		q.setParameter("maxtime", DateUtil.date("2012-8-17"));
		q.ignoreIfNull().compile();
		
		System.out.println("testSubSql sql: " + q.getTransitionSql());
		String expected = "select * from (select w.* from WEIBO_USER w where w.YOOYO_STATE = 50 and w.exp is not null and w.tweetnum is not null order by w.exp, w.tweetnum desc) where rownum < 6";
		Assert.assertEquals(expected, q.getTransitionSql());
//		Assert.assertEquals("[1, Fri Aug 17 00:00:00 CST 2012]", q.getValues().toString());
	}
	
	public void testCauseSql(){
		String sql = "SELECT* FROM (SELECT l.lottery_date, u.belong_region, l.mobile, l.user_name,p.lottery_prize_name, " +
				"CASE WHEN u.product_id = '120101592010000006169'      THEN '免费'   " +
				"ELSE '收费' END AS TYPE, l.state FROM yzl_lottery_list l LEFT JOIN uc_user u ON l.mobile = u.user_moblie " +
				"LEFT JOIN yzl_lottery_result_prize p ON l.lottery_result_id = p.lottery_result_id ) t " +
				"WHERE t.lottery_date >= :start_time AND t.lottery_date <= :end_time " +
				"AND t.belong_region LIKE :region AND t.mobile = :mobile " +
				"AND t.lottery_prize_name LIKE :prize AND t.user_name = :name AND TYPE = :type AND t.state = :state";
	}
	
	@Test
	public void testSelectInsert(){
		String sql = "INSERT INTO sms_message " +
				"(ID, phone_nos, title, MESSAGE, plan_send_time, from_app, state, sms_type, create_time) " +
				"SELECT seq_sms_message.NEXTVAL, u.user_moblie, :title, :content, SYSDATE, 'db_sql', -2, :sms_type, SYSDATE " +
				"FROM uc_user u WHERE u.state IN (1, 50) AND u.state = :state AND u.product_id = :product_id AND u.belong_region = :belong_region";
		System.out.println("testSelectInsert sql : " + sql);
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		Integer belong_region = 1;
		q.setParameter("belong_region", 1);
		q.setParameter("title", SQLKeys.Null);
		q.setParameter("content", SQLKeys.Null);
		q.setParameter("sms_type", SQLKeys.Null);
		q.ignoreIfNull().compile();
		String tranSql = "INSERT INTO sms_message( ID, phone_nos, title, MESSAGE, plan_send_time, from_app, state, sms_type, create_time ) " +
				"SELECT seq_sms_message.NEXTVAL, u.user_moblie, ?, ?, SYSDATE, 'db_sql', -2, ?, SYSDATE " +
				"FROM uc_user u WHERE u.state in ( 1, 50 ) AND 1=1 AND 1=1 AND u.belong_region = ?";
//		System.out.println("testSelectInsert getTransitionSql: " + q.getTransitionSql());;
//		System.out.println("testSelectInsert getValues: " + q.getValues());
		Assert.assertTrue(q.getValues().size()==4);
		Assert.assertNull(q.getValues().get(0));
		Assert.assertNull(q.getValues().get(1));
		Assert.assertNull(q.getValues().get(2));
		Assert.assertEquals(belong_region, q.getValues().get(3));
		Assert.assertEquals(tranSql, q.getTransitionSql());
	}
	
	@Test
	public void testStringLike(){
		String sql = "SELECT * FROM (SELECT l.lottery_date, u.belong_region, l.mobile, l.user_name, p.lottery_prize_name, " +
				"CASE WHEN u.product_id = '120101592010000006169' " +
					"THEN '免费' " +
					"ELSE '收费' " +
					"END AS TYPE, " +
				"CASE WHEN l.state = 0 " +
						"THEN '待抽奖' " +
					"WHEN l.state = 1 " +
					"THEN '已抽奖' WHEN l.state = 2 " +
					"THEN '中奖' END AS state " +
					"FROM yzl_lottery_list l LEFT JOIN uc_user u ON l.mobile = u.user_moblie " +
					"LEFT JOIN yzl_lottery_result_prize p ON l.lottery_result_id = p.lottery_result_id ) t " +
					"WHERE t.lottery_date >= :start_time " +
					"AND t.lottery_date <= :end_time " +
					"AND t.belong_region LIKE :region " +
					"AND :mobile LIKE CONCAT ('%', CONCAT (t.mobile, '%')) " +
					"AND t.lottery_prize_name LIKE :prize " +
					"AND t.user_name = :name " +
					"AND TYPE = :type " +
					"AND t.state = :state";
		
//		System.out.println("testStringLike sql : " + sql);
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		String region = "regionCode";
		String mobile = JFishList.wrap("13622267218", "13622267676").asString(", ");
		Integer type = 11;
		q.setParameter("region", region);
		q.setParameter("mobile", mobile);
		q.setParameter("type", type);
		q.ignoreIfNull().compile();

		String exceptedSql = "SELECT * FROM ( SELECT l.lottery_date, u.belong_region, l.mobile, l.user_name, p.lottery_prize_name, " +
				"CASE WHEN u.product_id = '120101592010000006169' " +
				"THEN '免费' " +
				"ELSE '收费' END AS TYPE, " +
				"CASE WHEN l.state = 0 THEN '待抽奖' " +
				"WHEN l.state = 1 THEN '已抽奖' " +
				"WHEN l.state = 2 THEN '中奖' END AS state " +
				"FROM yzl_lottery_list l LEFT JOIN uc_user u ON l.mobile = u.user_moblie " +
				"LEFT JOIN yzl_lottery_result_prize p ON l.lottery_result_id = p.lottery_result_id ) t " +
				"WHERE 1=1 AND 1=1 AND t.belong_region like ? AND ? like CONCAT( '%', CONCAT( t.mobile, '%' ) ) " +
				"AND 1=1 AND 1=1 AND TYPE = ? AND 1=1";
//		System.out.println("testStringLike: " + q.getTransitionSql());
//		System.out.println("testStringLike value: " + q.getValues());

		Assert.assertEquals(exceptedSql, q.getTransitionSql());
		Assert.assertEquals("%"+region+"%", q.getValues().get(0));
		Assert.assertEquals(mobile, q.getValues().get(1));
		Assert.assertEquals(type, q.getValues().get(2));
		
		q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter("region", region);
		q.setParameter("type", type);
		q.ignoreIfNull().compile();

		exceptedSql = "SELECT * FROM ( SELECT l.lottery_date, u.belong_region, l.mobile, l.user_name, p.lottery_prize_name, " +
				"CASE WHEN u.product_id = '120101592010000006169' " +
				"THEN '免费' " +
				"ELSE '收费' END AS TYPE, " +
				"CASE WHEN l.state = 0 THEN '待抽奖' " +
				"WHEN l.state = 1 THEN '已抽奖' " +
				"WHEN l.state = 2 THEN '中奖' END AS state " +
				"FROM yzl_lottery_list l LEFT JOIN uc_user u ON l.mobile = u.user_moblie " +
				"LEFT JOIN yzl_lottery_result_prize p ON l.lottery_result_id = p.lottery_result_id ) t " +
				"WHERE 1=1 AND 1=1 AND t.belong_region like ? AND 1=1 " +
				"AND 1=1 AND 1=1 AND TYPE = ? AND 1=1";
//		System.out.println("testStringLike: " + q.getTransitionSql());
//		System.out.println("testStringLike value: " + q.getValues());

		Assert.assertEquals(exceptedSql, q.getTransitionSql());
		Assert.assertEquals("%"+region+"%", q.getValues().get(0));
		Assert.assertEquals(type, q.getValues().get(1));
	}
	
	@Test
	public void testConnectBy(){
		String sql = "select r.cname from bm_region r start with r.ccode in :regionCodes connect by prior r.parent_code = r.ccode order siblings by r.ccode";
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter("regionCodes", LangUtils.newArrayList("aaa", "bbb", "ccc"));
		q.ignoreIfNull().compile();
		
		String exceptedSql = "select r.cname from bm_region r start with r.ccode in (?, ?, ?) connect by prior r.parent_code = r.ccode order siblings by r.ccode";
		Assert.assertEquals(exceptedSql, q.getTransitionSql());
		Assert.assertEquals("[aaa, bbb, ccc]", q.getValues().toString());
	}

	@Test
	public void testParentConditionSql(){
		JFishSqlParserManager.getInstance().setDebug(true);
		String sql = "select m.*, c.name as company_name from tb_member_info m " +
				"left join tb_store_company c on m.company_id=c.id " +
				"where m.member_type=:memberType and " +
				"(m.login_code=:loginCode or m.mobile=:loginCode or m.email=:loginCode)";
		
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter("memberType", 1);
		q.setParameter("loginCode", "test");
		q.ignoreIfNull().compile();
		
		System.out.println("testMemberSql:"+q.getTransitionSql());
		String exceptedSql = "select m.*, c.name as company_name from tb_member_info m left join tb_store_company c on m.company_id = c.id where m.member_type = ? and (m.login_code = ? or m.mobile = ? or m.email = ? )";
		Assert.assertEquals(exceptedSql, q.getTransitionSql());
		Assert.assertEquals("[1, test, test, test]", q.getValues().toString());
	}
	
	@Test
	public void testParentConditionSql2(){
		JFishSqlParserManager.getInstance().setDebug(true);
		String sql = "select m.*, c.name as company_name from tb_member_info m " +
				"left join tb_store_company c on m.company_id=c.id " +
				"where m.member_type=:memberType and " +
				"(m.login_code=:loginCode or m.mobile=:loginCode or m.email=:email)";
		
		DynamicQuery q = DynamicQueryFactory.createJFishDynamicQuery(sql);
		q.setParameter("memberType", 1);
		q.setParameter("loginCode", "test");
		q.ignoreIfNull().compile();
		
		System.out.println("testMemberSql:"+q.getTransitionSql());
		String exceptedSql = "select m.*, c.name as company_name from tb_member_info m left join tb_store_company c on m.company_id = c.id where m.member_type = ? and (m.login_code = ? or m.mobile = ? or 1=1 )";
		Assert.assertEquals(exceptedSql, q.getTransitionSql());
		Assert.assertEquals("[1, test, test]", q.getValues().toString());
	}

	@Test
	@Ignore
	public void testTimes(){
		times("testSqlFuntionInValueCache", 10000, true);
		times("testSqlFuntionInValueCache2", 10000, true);
		times("testSqlFuntionInValue2", 10000, true);
	}
	
	
}

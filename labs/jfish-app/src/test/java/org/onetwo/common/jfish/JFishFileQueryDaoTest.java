package org.onetwo.common.jfish;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.db.sqlext.SQLKeys;
import org.onetwo.common.fish.JFishQuery;
import org.onetwo.common.fish.spring.JFishDaoImpl;
import org.onetwo.common.fish.spring.JFishFileQueryDao;
import org.onetwo.common.fish.spring.JNamedQueryKey;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.list.JFishList;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JFishFileQueryDaoTest extends JFishBaseNGTest {
	
	@Resource
	private JFishFileQueryDao jFishFileQueryDaoImpl;

	@Resource
	private JFishDaoImpl jdao;
	
	private static int insertCount = 100;
	
	@Test(priority=-1)
	public void testInsertDatas(){
		if(jdao.getDialect().getDbmeta().isOracle())
			return ;
		List<BmRegionDTO> uclist = new ArrayList<BmRegionDTO>();
		for(int i=0; i<insertCount; i++){
			BmRegionDTO uc = new BmRegionDTO();
			uc.setCcode("00490000000001"+i);
			uc.setParentcode("0049000000000000");
			uc.setCname("test"+i);
			uc.setGrade(i);
			uclist.add(uc);
		}
		jdao.justInsert(uclist);
	}
	
	protected void printRegionList(List<BmRegionDTO> uclist){
		if(uclist==null)
			return ;
		for(BmRegionDTO u : uclist){
			LangUtils.println("${0} : ${1}", u.getId(), u);
		}
	}
	
	@Test
	public void testFindList(){
		List<BmRegionDTO> uclist = this.jFishFileQueryDaoImpl.findListByQName("BmRegionDTO.find.10");
		Assert.assertNotNull(uclist);
		Assert.assertEquals(10, uclist.size());
//		printUserList(uclist);
	}
	
	@Test
	public void testFindPageOrderBy(){
//		if(jdao.getDialect().getDbmeta().isOracle())
//			return ;
		
//		List<BmRegionDTO> regions = this.jFishFileQueryDaoImpl.findListByQName("BmRegionDTO.find.all", "orderField", "t.GRADE ");
//		printRegionList(regions);
		List<BmRegionDTO> regions = this.jFishFileQueryDaoImpl.findListByQName("BmRegionDTO.find.all", JNamedQueryKey.DESC, "id");
		Assert.assertTrue(regions.size()>0);
		Long pre = 0L;
		for(BmRegionDTO br : regions){
			if(pre.longValue()!=0){
				Assert.assertTrue(pre>br.getId());
			}
			pre = br.getId();
		}
		regions = this.jFishFileQueryDaoImpl.findListByQName("BmRegionDTO.find.all", JNamedQueryKey.ASC, "id");
		Assert.assertTrue(regions.size()>0);
		pre = 0L;
		for(BmRegionDTO br : regions){
			if(pre.longValue()!=0){
				Assert.assertTrue(pre<br.getId());
			}
			pre = br.getId();
		}
		
//		Page<BmRegionDTO> page = Page.create();
//		page.setPageSize(15);
//		page = this.jFishFileQueryDaoImpl.findPageByQName("BmRegionDTO.find.all", page, "orderField", "t.GRADE");
//		Assert.assertNotNull(page.getResult());
//		Assert.assertEquals(page.getPageSize(), page.getResult().size());
//		printUserList(page.getResult());
	}
	
	@Test
	public void testFindPage(){
		if(jdao.getDialect().getDbmeta().isOracle())
			return ;
		
		Page<BmRegionDTO> page = Page.create();
		page.setPageSize(15);
		page = this.jFishFileQueryDaoImpl.findPageByQName("BmRegionDTO.find.all", page);
		Assert.assertNotNull(page.getResult());
		Assert.assertEquals(page.getPageSize(), page.getResult().size());
//		printUserList(page.getResult());
	}
	
	@Test
	public void testFindPageWithNullCause(){
		if(jdao.getDialect().getDbmeta().isOracle())
			return ;
		
		UtilTimerStack.setActive(true);
		Page page = Page.create();
		page.setPageSize(15);
		
		page = this.jFishFileQueryDaoImpl.findPageByQName("BmRegionDTO.with.null.cause.page", page, "parentcode", "0049000000000000", "grade", null);
//			Assert.fail("should be fail");
		Assert.assertEquals(0, page.getTotalCount());
		Assert.assertEquals(0, page.getSize());
		Assert.assertEquals(0, page.getResult().size());
		
	}
	
	@Test
	public void testFindPageWithNullCause2(){
		if(jdao.getDialect().getDbmeta().isOracle())
			return ;
		
		UtilTimerStack.setActive(true);
		
		int size = 10;
		JFishQuery jq = this.jFishFileQueryDaoImpl.createJFishQueryByQName("BmRegionDTO.with.ignore.null.page", "parentcode", "  ", "grade", null);
		jq.setFirstResult(0);
		jq.setMaxResults(size);
		List<BmRegionDTO> datalist = jq.getResultList();
		Assert.assertNotNull(datalist);
		Assert.assertEquals(size, datalist.size());
		for(BmRegionDTO uc : datalist){
			System.out.println("testFindPageWithNullCause2 cname: " + uc.getCname());
		}
	}
	
	@Test
	public void testFindPageIgnoreNull(){
		if(jdao.getDialect().getDbmeta().isOracle())
			return ;
		
		UtilTimerStack.setActive(true);
		Page<BmRegionDTO> page = Page.create();
		page.setPageSize(15);
		
		page = this.jFishFileQueryDaoImpl.findPageByQName("BmRegionDTO.with.ignore.null.page", page, "parentcode", "0049000000000000", "grade", null);
		Assert.assertNotNull(page.getResult());
		Assert.assertEquals(page.getPageSize(), page.getResult().size());
		printRegionList(page.getResult());
	}
	
//	@Test
	//zjk db
	public void testJfishSqlFile(){
		if(jdao.getDialect().getDbmeta().isMySQL())
			return ;
		UtilTimerStack.setActive(true);
		Page<BmRegionDTO> page = Page.create();
		page.setPageSize(15);
		
		page = this.jFishFileQueryDaoImpl.findPageByQName("searchPageListSupplier_json", page);
		Assert.assertNotNull(page.getResult());
		Assert.assertEquals(page.getPageSize(), page.getResult().size());
	}

	@Test(priority=100)
	public void testDelete(){
		if(jdao.getDialect().getDbmeta().isOracle())
			return ;
		JFishQuery jq = this.jFishFileQueryDaoImpl.createJFishQueryByQName("BmRegionDTO.delete.all");
		int count = jq.executeUpdate();
		System.out.println("delete count: " + count);
		Assert.assertTrue(count>=insertCount);
	}
	
	@Test
	public void testCauseSql(){
		if(jdao.getDialect().getDbmeta().isMySQL())
			return ;
		JFishQuery jq = this.jFishFileQueryDaoImpl.createJFishQueryByQName("cause.sql");
		List datas = jq.getResultList();
		Assert.assertTrue(datas.size()>0);
	}
	
	@Test
	public void testSelectInsertSql(){
		if(jdao.getDialect().getDbmeta().isMySQL())
			return ;
		JFishQuery q = this.jFishFileQueryDaoImpl.createJFishQueryByQName("select.insert.sql");

		q.setParameter("title", SQLKeys.Empty);
		q.setParameter("content", SQLKeys.Null);
		q.setParameter("sms_type", SQLKeys.Null);
		q.setParameter("belong_region", "1");
		
		int count = q.executeUpdate();
		Assert.assertTrue(count==0);
	}
	
	@Test
	public void testParamLikeSql(){
		if(jdao.getDialect().getDbmeta().isMySQL())
			return ;
		JFishQuery q = this.jFishFileQueryDaoImpl.createJFishQueryByQName("param.like.sql");
		String region = "regionCode";
		String mobile = JFishList.wrap("13622267218", "13622267676").asString(", ");
		Integer type = 11;
		q.setParameter("region", region);
		q.setParameter("mobile", mobile);
		q.setParameter("type", type);
		
		List<?> datalist = q.getResultList();
		Assert.assertNotNull(datalist);
	}

}

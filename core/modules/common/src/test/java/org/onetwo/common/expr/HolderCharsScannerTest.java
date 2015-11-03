package org.onetwo.common.expr;


import org.junit.Assert;
import org.junit.Test;

public class HolderCharsScannerTest {
	
	@Test
	public void test(){
		String source = "select * from ( select est.`NAME`, house.HOUSE_TYPE, count(1) as amount from zyt_estate_house house left join zyt_estate est on est.ID = house.ESTATE_ID where house.HOUSE_TYPE!='NEW_HOUSE' and house.status = 'PUBLISHED' and est.DISTRICT_CODE like ? and (house.title like ?  or est.ADDRESS like ?) group by est.`NAME`, house.HOUSE_TYPE union all select est.`NAME` as `NAME`, 'NEW_HOUSE' as HOUSE_TYPE, count(1) as amount from zyt_estate est where est.status = 'PUBLISHED' and est.is_new_estate = 1 and est.SELL_STATUS != 'SOLDOUT' and est.DISTRICT_CODE like ? and (est.`NAME` like ?  or est.ADDRESS like ?) group by est.name ) t where t.amount > 0 order by t.`NAME` limit 10 ";
		String result = HolderCharsScanner.holder("?").parse(source, (index)->{
			return "?"+index;
		});
		System.out.println("result:"+result);
		Assert.assertEquals("select * from ( select est.`NAME`, house.HOUSE_TYPE, count(1) as amount from zyt_estate_house house left join zyt_estate est on est.ID = house.ESTATE_ID where house.HOUSE_TYPE!='NEW_HOUSE' and house.status = 'PUBLISHED' and est.DISTRICT_CODE like ?0 and (house.title like ?1  or est.ADDRESS like ?2) group by est.`NAME`, house.HOUSE_TYPE union all select est.`NAME` as `NAME`, 'NEW_HOUSE' as HOUSE_TYPE, count(1) as amount from zyt_estate est where est.status = 'PUBLISHED' and est.is_new_estate = 1 and est.SELL_STATUS != 'SOLDOUT' and est.DISTRICT_CODE like ?3 and (est.`NAME` like ?4  or est.ADDRESS like ?5) group by est.name ) t where t.amount > 0 order by t.`NAME` limit 10 ", result);
	}

}

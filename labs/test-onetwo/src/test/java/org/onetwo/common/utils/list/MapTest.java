package org.onetwo.common.utils.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.onetwo.common.excel.CardEntity;
import org.onetwo.common.profiling.TimeCounter;
import org.onetwo.common.utils.map.M;

public class MapTest {
	private String jsonString = "{'history.indictcomtype':1,'history.gender':'','history.aptcnt':'20','history.triptype':1,'history.localareapvn':'','history.sendnote':'directaccept','history.handlername':'','history.indictcomname':'广东国旅','history.sendbureaupvn':1102769,'history.indictcomphone':'202-33621566','history.happentime':'2011-12-19','history.bureauxian':'','history.sendbureauxian':'','history.companyname':'投诉者单位名称','history.authtype':'','history.aptinfotype':'','history.traveltype':2,'history.id':'','history.indicttypeid':4,'optype':'send','indict.id':'','history.sendbureaucity':1102773,'history.aptdeadline':'2011-12-20','history.bureaupvn':1102769,'history.outarea':'','history.reason':'事实与理由','history.indictcomaddr':'广东省广州市','history.phone':'202-33621566','history.name':'张三Send','history.request':'请求事项','history.bureaucity':'','history.aptdealdate':'2011-12-19','history.inarea':'','history.localareaxian':'','history.sendno':'','history.localareacity':'','history.sendtimelimit':'2012-1-1'}";
	private String jsonString2 = "{'history.indictcomtype':3,'history.gender':1,'indictinfos':[0],'history.triptype':1,'history.localareapvn':'','history.sendnote':'directaccept','history.handlername':'','history.indictcomname':'被投诉单位名称','history.sendbureaupvn':1102769,'history.happentime':'2011-12-06','history.bureauxian':'','history.companyname':'单位名称','history.sendbureauxian':'','history.authtype':'','history.aptinfotype':'','history.traveltype':1,'history.id':'','history.indicttypeid':4,'optype':'send','indict.id':'','history.sendbureaucity':1102773,'history.aptdeadline':'2011-12-06','history.bureaupvn':'','attachment':['bb-20111220160659.png','aa-20111220160659.txt'],'history.outarea':'','attachname':['bb','aa'],'history.name':'投诉者姓名2011-12-20 16:06:58Send','history.bureaucity':'','history.aptdealdate':'2011-12-06','history.inarea':10689,'history.localareaxian':'','history.sendno':'','history.localareacity':'','history.sendtimelimit':'2012-1-1'}";

	
	@Test
	public void testObjectMap(){
		CardEntity card = new CardEntity();
		card.setId(111l);
		card.setCardNo("testno");
		card.setState(1l);
		Map map = M.from(card);
		System.out.println("object map : "+map);
		Object[] objs = M.toArray(map);
		System.out.println(objs);
	}
	
	@Test
	public void testMap(){
		TimeCounter t = new TimeCounter("map");
		t.start();
		int count = 100000;
		Map<String, Integer> testMap = new HashMap<String, Integer>();
		for(int i=0; i<count; i++){
			testMap.put("key"+i, i);
		}
		t.stop();
	}
	
	@Test
	public void testMapAndList(){
		System.out.println("testMapAndList...................");
		TimeCounter t = new TimeCounter("map");
		t.start();
		int count = 500000;
		Map<String, Integer> testMap = new HashMap<String, Integer>();
		for(int i=0; i<count; i++){
			testMap.put("key"+i, i);
		}
		t.stop();

		t.restart("list");
		List<Integer> list = new ArrayList<Integer>();
		for(int i=0; i<count; i++){
			list.add(i);
		}
		t.stop();
		
		Integer target = 199999;
		t.restart("list get");
		for(Integer i : list){
			if(target.equals(i)){
				break;
			}
		}
		t.stop();
		
		t.restart("map get");
		if(testMap.get("key"+target)!=null)
			t.stop();
	}

}

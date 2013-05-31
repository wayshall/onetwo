package org.onetwo.common.utils.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.profiling.TimeCounter;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.User;
import org.onetwo.common.utils.map.IncreaseMap;

@SuppressWarnings("unchecked")
public class ListTest {
	
	@Test
	public void testExcludeClass(){
		Object[] objs = new Object[]{"test1", "test2", 1, 3, Integer.class, Long.class};
		List list = L.list(false, objs);
		System.out.println(list);
		
		List list2 = L.exclude(objs, Number.class);
		Assert.assertArrayEquals(list2.toArray(),  new Object[]{"test1", "test2", 1, 3});
		System.out.println(list2);
		List list22 = MyUtils.asListExclude(objs, Integer.class, Long.class);
//		Assert.assertArrayEquals(list2.toArray(), list22.toArray());
		System.out.println(list22);
		
		List list3 = L.exclude(objs, "test1", Number.class);
		Assert.assertArrayEquals(list3.toArray(), new Object[]{"test2", 1, 3});
	}
	
	@Test
	public void testArray(){
		String[] DEFAULT_EXCLUDE_SUFFIXS = { ".js", ".css", ".jpg", ".jpeg", ".gif", ".png",".htm" };
		List list = L.exclude(DEFAULT_EXCLUDE_SUFFIXS, true, null);
		Assert.assertArrayEquals(list.toArray(), DEFAULT_EXCLUDE_SUFFIXS);
		System.out.println(list);
	}
	
	@Test
	public void testEasyList(){
		final List<Long> list = L.aslist(1l, 3l, 4l);
		Long total = (Long)new EasyList<Long>(list).each(new ListFun<Long>(){
			public Long total = 0l;

			@Override
			public void exe(Long element, int index, EasyList easytor, Object... objects) {
				Iterator it = easytor.getIterator();
				total += element;
				it.remove();
				easytor.setResult(total);
			}
			
		}).getResult();
		System.out.println("tatal: " + total);
		Assert.assertEquals(total, (Long)(1l+3l+4l));
		Assert.assertEquals(list.size(), 0);
	}
	
	@Test
	public void testTrigger(){
		int result = (int)L.wrapNum(0, 100).trigger(new TriggerFun<Integer>(){
			@Override
			public boolean isTriggered(Integer element, int index, EasyList<Integer> easytor, Object... objects) {
				return index%10==0;
			}
			@Override
			public void exe(Integer element, int index, EasyList<Integer> easytor, Object... objects) {
				System.out.println("trigger.......");
				easytor.setResult(easytor.getResult(0)+200);
			}
		}).each(new ListFun<Integer>(){
			@Override
			public void exe(Integer element, int index, EasyList<Integer> easytor, Object... objects) {
				easytor.setResult(easytor.getResult(0)+100);
			}
		}).getResult(0);
		Assert.assertEquals((100*100+200*(100/10)), result);
	}
	
	@Test
	public void testAllAndAny(){
		List list = L.nList(1, 10);
		boolean rs = JFishList.wrap(list).all(new PredicateBlock<Integer>(){

			@Override
			public boolean evaluate(Integer element, int index) {
				return element>0;
			}
			
		});
		Assert.assertEquals(true, rs);
		
		rs = JFishList.wrap(list).all(new PredicateBlock<Integer>(){

			@Override
			public boolean evaluate(Integer element, int index) {
				return element>1;
			}
			
		});
		Assert.assertEquals(false, rs);
		
		rs =JFishList.wrap(list).any(new PredicateBlock<Integer>(){

			@Override
			public boolean evaluate(Integer element, int index) {
				return element>5;
			}
			
		});
		Assert.assertEquals(true, rs);
		
		rs = JFishList.wrap(list).any(new PredicateBlock<Integer>(){

			@Override
			public boolean evaluate(Integer element, int index) {
				return element>15;
			}
			
		});
		Assert.assertEquals(false, rs);
	}
	
	@Test
	public void testCompareList(){
		TimeCounter t1 = new TimeCounter("for");
		t1.start();
		int count = 100000;
		List<Integer> list1 = new ArrayList<Integer>();
		for(int i=0; i<count; i++){
			list1.add(i);
		}
		t1.stop();
		
		t1.restart();
		L.wrapNum(0, count);
		t1.stop();
	}
	
	@Test
	public void testGetList(){
		Map map1 = new HashMap();
		map1.put("name", "mapName1");
		List<Integer> list = L.nList(0, 10);
		map1.put("list", list);
		
		Map map2 = new HashMap();
		map2.put("name", "mapName2");
		List<Integer> list2 = L.nList(11, 20);
		map2.put("list", list2);
		
		Map map3 = new HashMap();
		map3.put("name", "mapName2");
		List<Integer> list3 = L.nList(11, 20);
		map3.put("list", list3);
		
		List<String> rs = JFishList.wrap(map1, map2, map3).getPropertyList("name");
		Assert.assertArrayEquals(rs.toArray(), new String[]{"mapName1", "mapName2", "mapName2"});
	}
	
	@Test
	public void testGroupby(){
		final List<User> datas = new ArrayList<User>();
		L.wrapNum(1, 100).each(new ListFun<Integer>(){

			@Override
			public void exe(Integer element, int index, EasyList<Integer> easytor, Object... objects) {
				User user = new User();
				user.setUserName("testName"+(index%3));
				datas.add(user);
			}
			
		});
		IncreaseMap result = L.wrap(datas).groupby("userName");
		Assert.assertEquals(result.size(), 3);
		Assert.assertEquals(33, result.getValueList("testName0").size());
		Assert.assertEquals(33, result.getValueList("testName1").size());
		Assert.assertEquals(33, result.getValueList("testName2").size());
	}

	@Test
	public void testIntArray(){
		int[] array = new int[]{0, 1};
		List list = L.aslist(array);
		LangUtils.println("list:", list);
		LangUtils.println("array:", array);
	}
}

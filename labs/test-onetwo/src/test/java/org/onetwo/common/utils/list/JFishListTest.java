package org.onetwo.common.utils.list;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.TestUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.SimpleBlock;

import test.entity.UserEntity;

public class JFishListTest {
	@Test
	public void testAllAndAny(){
		List<Integer> list = L.nList(1, 10);
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
	public void testGroupBy(){
		List<UserEntity> all = LangUtils.newArrayList();
		List<UserEntity> aa = TestUtils.createUserList("aa", 3);
		List<UserEntity> bb = TestUtils.createUserList("bb", 1);
		List<UserEntity> cc = TestUtils.createUserList("cc", 2);
		all.addAll(aa);
		all.addAll(bb);
		all.addAll(cc);
		
		Map<String, List<UserEntity>> groups = JFishList.wrap(all).groupBy(new SimpleBlock<UserEntity, String>() {

			@Override
			public String execute(UserEntity object) {
				return object.getUserName();
			}
			
		});
		
		System.out.println("groups:" + groups);
		Assert.assertEquals(3, groups.get("aa").size());
		Assert.assertEquals(1, groups.get("bb").size());
		Assert.assertEquals(2, groups.get("cc").size());
	}
}

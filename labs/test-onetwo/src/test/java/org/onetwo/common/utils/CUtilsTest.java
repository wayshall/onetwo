package org.onetwo.common.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.TestUtils;
import org.onetwo.common.utils.list.JFishList;

import test.entity.UserEntity;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;

public class CUtilsTest {

	@Test
	public void testAsArray(){
		String[] strs = new String[]{"aa", "bb"};
		Object[] objs = new Object[]{"aa", "bb"};
		
		String[] fields = (String[])CUtils.asArray(strs);
		Assert.assertTrue(strs==fields);
		
		try {
			fields = (String[])CUtils.asArray(objs);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertTrue(ClassCastException.class==e.getClass());
		}
		
		fields = CUtils.asStringArray(strs);
		Assert.assertTrue(strs==fields);
		
		fields = CUtils.asStringArray(objs);
		Assert.assertTrue(strs!=fields);
		Assert.assertEquals(strs[0], fields[0]);
		Assert.assertEquals(strs[1], fields[1]);
		

		fields = CUtils.asStringArray("aa");
		Assert.assertEquals(strs[0], fields[0]);

		Set<String> formats = CUtils.asSet("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "HH:mm:ss", "yyyy-MM-dd HH:mm");
		Assert.assertEquals(formats.size(), 4);
	}
	
	@Test
	public void testMap2Array(){
		Object[] values = new Object[]{"aa", "aavalue", "bb", 2L, "cc", new Object()};
		Map<Object, Object> map = CUtils.asMap(values);
		Object[] array = CUtils.map2Array(map);
		Assert.assertEquals(values.length, array.length);
		for (int i = 0; i < array.length; i++) {
			Assert.assertEquals(values[i], array[i]);
		}
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
		
		Map<String, List<UserEntity>> groups = CUtils.groupBy(all, new SimpleBlock<UserEntity, String>() {

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

package org.onetwo.common.spring;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.BeanWrapper;

import test.entity.RoleEntity;
import test.entity.UserEntity;

public class BeanWrapperTest {

	private static class UserData {
		private Map<String, String> attrs = LangUtils.newHashMap();

		public Map<String, String> getAttrs() {
			return attrs;
		}
		
		
	}
	private BeanWrapper bw;

	@Test
	public void testMap(){
		UserData u = new UserData();
//		bw = PropertyAccessorFactory.forBeanPropertyAccess(user);
		bw = SpringUtils.newBeanWrapper(u);
		bw.setAutoGrowNestedPaths(true);
		
		bw.setPropertyValue("attrs[id]", "11");
		Assert.assertEquals(u.getAttrs().get("id"), "11");
		
		Map<String, String> attrs = LangUtils.newHashMap();
		/*bw = SpringUtils.newBeanWrapper(attrs);
		bw.setAutoGrowNestedPaths(true);
		try {
			bw.setPropertyValue("id", "11");
			Assert.fail();
		} catch (Exception e) {
		}*/
		bw = SpringUtils.newBeanWrapper(attrs);
		bw.setPropertyValue("id", "11");
		Assert.assertEquals(attrs.get("id"), "11");
		
	}

	@Test
	public void testBw(){
		UserEntity user = new UserEntity();
//		bw = PropertyAccessorFactory.forBeanPropertyAccess(user);
		bw = new JFishBeanWrapper(user);
		bw.setAutoGrowNestedPaths(true);
		
		bw.setPropertyValue("id", "11");
		Assert.assertTrue(user.getId()==11L);
		
		bw.setPropertyValue("roles[0].id", 12L);
		bw.setPropertyValue("roles[0].name", "way");
		RoleEntity role = user.getRoles().get(0);
		Assert.assertNotNull(role);
		Assert.assertTrue(role.getId()==12L);
		Assert.assertEquals("way", role.getName());
		
		bw.setPropertyValue("roles[0][name]", "test");
		Assert.assertEquals("test", role.getName());
	}
	@Test
	public void testBw2(){
		UserEntity user = new UserEntity();
		bw = new JFishBeanWrapper(user);
		bw.setAutoGrowNestedPaths(true);
		
		bw.setPropertyValue("id", "11");
		Assert.assertTrue(user.getId()==11L);
		
		bw.setPropertyValue("roles[0][name]", "test");
		RoleEntity role = user.getRoles().get(0);
		Assert.assertEquals("test", role.getName());
	}
	
	@Test
	public void testBwMap(){
		Map<String, Object> map = LangUtils.newHashMap();
		UserEntity user = new UserEntity();
		user.setUserName("userName1");
		map.put("user", user);
//		bw = PropertyAccessorFactory.forBeanPropertyAccess(map);
		bw = SpringUtils.newBeanWrapper(map);
		bw.setAutoGrowNestedPaths(true);
		
		bw.setPropertyValue("id", 11L);
		Assert.assertEquals(map.get("id"), 11L);
		
		bw.setPropertyValue("name", "test");
		Assert.assertEquals("test", map.get("name"));
		
	}

	
	@Test
	public void testBwMap2(){
		Map<String, Object> map = LangUtils.newHashMap();
		UserEntity user = new UserEntity();
		user.setUserName("userName1");
		map.put("user", user);
		map.put("userMap", map);
//		bw = PropertyAccessorFactory.forBeanPropertyAccess(map);
		bw = SpringUtils.newBeanWrapper(map);
		bw.setAutoGrowNestedPaths(true);
		
		Object userName = bw.getPropertyValue("user.userName");
		System.out.println("userName:" + userName);
		Assert.assertEquals("userName1", userName);
		
		userName = bw.getPropertyValue("userMap.user.userName");
		System.out.println("userName:" + userName);
		Assert.assertEquals("userName1", userName);
	}


	@Test
	public void testBwList(){
		List<RoleEntity> list = LangUtils.newArrayList();
		UserEntity user = new UserEntity();
		user.setUserName("userName1");
		RoleEntity role = new RoleEntity();
		role.setName("roleName1");
		list.add(role);
		user.setRoles(list);
//		bw = PropertyAccessorFactory.forBeanPropertyAccess(map);
		bw = SpringUtils.newBeanWrapper(user);
		bw.setAutoGrowNestedPaths(true);
		
		Object userName = bw.getPropertyValue("roles[0].name");
		System.out.println("roleName1:" + userName);
		Assert.assertEquals("roleName1", userName);
		
	}
	@Test
	public void testBwList2(){
		List<UserEntity> userList = LangUtils.newArrayList();
		UserEntity user = new UserEntity();
		user.setUserName("userName1");
		userList.add(user);
		Map<String, Object> map = LangUtils.newHashMap();
		map.put("users", userList);
//		bw = PropertyAccessorFactory.forBeanPropertyAccess(map);
		bw = SpringUtils.newBeanWrapper(map);
		bw.setAutoGrowNestedPaths(true);
		
		Object userName = bw.getPropertyValue("users[0].userName");
		System.out.println("userName:" + userName);
		Assert.assertEquals("userName1", userName);
		
	}
	@Test
	public void testBwList3(){
		List<UserEntity> userList = LangUtils.newArrayList();
		Map<String, Object> map = LangUtils.newHashMap();
		map.put("users", userList);
//		bw = PropertyAccessorFactory.forBeanPropertyAccess(map);
		bw = SpringUtils.newBeanWrapper(map, "users", UserEntity.class);
		bw.setAutoGrowNestedPaths(true);
		bw.setPropertyValue("users[0].userName", "userName1");
		bw.setPropertyValue("users[0].age", 17);
		Object userName = bw.getPropertyValue("users[0].userName");
		System.out.println("userName:" + userName);
		Assert.assertEquals("userName1", userName);
		Assert.assertEquals(17, bw.getPropertyValue("users[0].age"));
		Assert.assertEquals(1, userList.size());
		
	}
	@Test
	public void testBwListWithSimple(){
		List<Long> userList = LangUtils.newArrayList();
		Map<String, Object> map = LangUtils.newHashMap();
		map.put("numbs", userList);
//		bw = PropertyAccessorFactory.forBeanPropertyAccess(map);
		bw = SpringUtils.newBeanWrapper(map);
		bw.setAutoGrowNestedPaths(true);
		bw.setPropertyValue("numbs[0]", 1L);
		Long number = (Long)bw.getPropertyValue("numbs[0]");
		System.out.println("number:" + number);
		Assert.assertTrue(number.equals(1L));
		
		userList = null;
		map.put("numbs", userList);
//		bw = PropertyAccessorFactory.forBeanPropertyAccess(map);
		bw = SpringUtils.newBeanWrapper(map);
		bw.setAutoGrowNestedPaths(true);
		bw.setPropertyValue("numbs[1]", 1L);
		number = (Long)bw.getPropertyValue("numbs[1]");
		System.out.println("number:" + number);
		Assert.assertTrue(number.equals(1L));
		List<?> list = (List<?>)bw.getPropertyValue("numbs");
		Assert.assertEquals(2, list.size());
		
	}
}

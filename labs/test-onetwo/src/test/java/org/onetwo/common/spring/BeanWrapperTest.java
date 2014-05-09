package org.onetwo.common.spring;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

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
		
		bw.setPropertyValue("roles[0][name][id]", "test");
		RoleEntity role = user.getRoles().get(0);
		Assert.assertEquals("test", role.getName());
	}
	
	@Test
	public void testBwMap(){
		Map<String, Object> map = LangUtils.newHashMap();
		bw = PropertyAccessorFactory.forBeanPropertyAccess(map);
		bw.setAutoGrowNestedPaths(true);
		
		bw.setPropertyValue("[id]", 11L);
		Assert.assertEquals(map.get("id"), 11L);
		
		bw.setPropertyValue("name", "test");
		Assert.assertEquals("test", map.get("name"));
	}
	
	@Test
	public void testNestedPathPattern(){
		final Pattern NESTED_PATH_PATTERN = Pattern.compile("(\\[([a-z]+\\w)\\])");
		
		String input = "aaa[0][bbbb]";
		Matcher matcher = NESTED_PATH_PATTERN.matcher(input);
		boolean rs = matcher.find();
		Assert.assertTrue(rs);
		Assert.assertEquals("bbbb", matcher.group(2));
		
		String str = input.replaceFirst("(\\[([a-z]+\\w)\\])", ".$2");
		Assert.assertEquals("aaa[0].bbbb", str);
		
		JFishBeanWrapper jbw = new JFishBeanWrapper(null);
		str = jbw.translatePropertyPath(input);
		Assert.assertEquals("aaa[0].bbbb", str);
	}
}

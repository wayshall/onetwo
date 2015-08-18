package org.onetwo.common.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import test.entity.UserEntity;

public class PropertyAccessorTest {
	
	@Test
	public void testProperties() {
		int count = 10;
		List<Map<String, Object>> datalist = new ArrayList<Map<String,Object>>();
		for (long i = 0; i < count; i++) {
			Map<String, Object> row = LangUtils.newHashMap();
			long id = i/2+1;
			row.put("id", id);
			row.put("userName", "testName-" + id);
			row.put("roles["+(i%2)+"].id", i);
			row.put("roles["+(i%2)+"].name", "name-"+i);
			
			datalist.add(row);
		}
		System.out.println(datalist);
		
		Map<Long, UserEntity> userMap = CUtils.newLinkedHashMap();
		for(Map<String, Object> row : datalist){
			UserEntity user = userMap.get(row.get("id"));
			if(user==null)
				user = new UserEntity();
			BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(user);
			bw.setAutoGrowNestedPaths(true);
			
			for(Entry<String, Object> entry : row.entrySet()){
				bw.setPropertyValue(entry.getKey(), entry.getValue());
			}
			
			userMap.put(user.getId(), user);
		}
		List<UserEntity> userlist = LangUtils.asList(userMap.values());
//		Assert.assertTrue(userlist.size()==count/2);
		System.out.println("userlist: " + userlist);
		System.out.println("roles: " + userlist.get(0).getRoles());
		Assert.assertEquals(2, userlist.get(0).getRoles().size());
		Assert.assertTrue(0==userlist.get(0).getRoles().get(0).getId());
		Assert.assertEquals("name-0", userlist.get(0).getRoles().get(0).getName());
	}

}

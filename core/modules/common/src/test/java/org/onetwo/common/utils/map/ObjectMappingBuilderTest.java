package org.onetwo.common.utils.map;


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ObjectMappingBuilderTest {
	
	@Test
	public void testMappingCopy(){
		UserBeanTest user = new UserBeanTest();
		user.setId(222);
		user.setAge(3333);
		user.setName("testUsername");
		UserBean2Test user2 = ObjectMappingBuilder.newBuilder(UserBeanTest.class, UserBean2Test.class)
							.mapAllFields()
//							.addMapping("name", "userName")
							.addMapping("userName", src->(src.getName()))
							.addMapping("year", "age")
							.bindValue(user);
		assertObject(user, user2);

		user2 = ObjectMappingBuilder.newBuilder(UserBeanTest.class, UserBean2Test.class)
							.mapAllFields()
//							.addMapping("name", "userName")
							.addMapping("userName", src->(src.getName()))
							.addMapping("year", "age")
							.bindValue(user, new UserBean2Test());
		assertObject(user, user2);
		
		
		List<UserBeanTest> userlist = new ArrayList<>();
		userlist.add(user);
		
		user = new UserBeanTest();
		user.setId(444);
		user.setAge(33433);
		user.setName("testUsername444");
		userlist.add(user);
		
		List<UserBean2Test> userlist2 = ObjectMappingBuilder.newBuilder(UserBeanTest.class, UserBean2Test.class)
							.mapAllFields()
//							.addMapping("name", "userName")
							.addMapping("userName", src->(src.getName()))
							.addMapping("year", "age")
							.bindValues(userlist);
		
		Assert.assertEquals(userlist.size(), userlist2.size());
		assertObject(userlist.get(0), userlist2.get(0));
		assertObject(userlist.get(1), userlist2.get(1));
	}
	
	private void assertObject(UserBeanTest user, UserBean2Test user2){
		Assert.assertEquals(user.getId(), user2.getId());
		Assert.assertEquals(user.getAge(), user2.getYear());
		Assert.assertEquals(user.getName(), user2.getUserName());
	}
	
	private class UserBeanTest {
		private long id;
		private String name;
		private int age;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		
	}
	

	public static class UserBean2Test {
		private long id;
		private String userName;
		private int year;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public int getYear() {
			return year;
		}
		public void setYear(int year) {
			this.year = year;
		}
		
	}

}

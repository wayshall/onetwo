package org.onetwo.common.utils.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.spring.dozer.DozerBean;
import org.onetwo.common.spring.dozer.DozerFacotry;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.DateUtilTest;
import org.onetwo.common.utils.ReflectUtils;

import test.entity.UserEntity;

public class DuplicatorTest {
	
	private DozerBean duplicator;
	
	@Before
	public void setup(){
//		this.duplicator = BeanDuplicatorFacotry.inst();
		this.duplicator = DozerFacotry.createDozerBean(this.getClass().getClassLoader(), DateUtilTest.class.getPackage().getName());
	}
	
	@Test
	public void testCopyDestClass(){
		Bean1 b1 = new Bean1();
		ReflectUtils.setFieldsDefaultValue(b1, String.class, ":name");
		Bean2 b2 = duplicator.map(b1, Bean2.class);
		Assert.assertEquals(b1.getName1(), b2.getName1());
		Assert.assertEquals(b1.getName2(), b2.getName2());
		Assert.assertEquals(b1.getName3(), "name3");
		Assert.assertNull(b2.getName4());
	}
	
	@Test
	public void testCopyDestObjectWithNull(){
		Bean1 b1 = new Bean1();
		ReflectUtils.setFieldsDefaultValue(b1, String.class, ":name");
		b1.setName1(null);
		
		Bean2 b2 = new Bean2();
		b2.setName1("b2-name1");
		duplicator.map(b1, b2);
		Assert.assertEquals(b1.getName1(), b2.getName1());
		Assert.assertEquals(b1.getName2(), b2.getName2());
		Assert.assertEquals(b1.getName3(), "name3");
		Assert.assertNull(b2.getName4());
	}

	
	@Test
	public void testCopyWithUnderLine(){
		Date now = new Date();
		TestBean1 t1 = new TestBean1();
		ReflectUtils.setFieldsDefaultValue(t1, String.class, ":name", Date.class, now);
		String username = "hello dozer";
		t1.setUserName(username);
		t1.setName2("");
		t1.setName4(null);
		
		TestBean2 t2 = new TestBean2();
		t2.setName2("name2");
		t2.setName4("name4");
		duplicator.map(t1, t2);
		Assert.assertEquals(t1.getName1(), t2.getName1());
		Assert.assertEquals(t1.getName2(), t2.getName2());
		Assert.assertEquals("name4", t2.getName4());
		Assert.assertEquals(username, t2.getUser_name());
		Assert.assertEquals(DateUtil.formatDateTime(t1.getBirthDay()), DateUtil.formatDateTime(t2.getBirth_day()));
	}
	
	@Test
	public void testCopyWithList(){
		Bean1 b1 = new Bean1();
		ReflectUtils.setFieldsDefaultValue(b1, String.class, ":name");
		List<TestBean1> list1 = new ArrayList<TestBean1>();
		TestBean1 test1 = new TestBean1();
		test1.setName1("test1Name1");
		test1.setName2("test1Name2");
		test1.setName4("test1Name4");
		list1.add(test1);
		
		TestBean1 test2 = new TestBean1();
		test2.setName1("test2Name1");
		test2.setName2("test2Name2");
		test2.setName4("test2Name4");
		list1.add(test2);
		
		b1.setList(list1);
		
		Bean2 b2 = duplicator.map(b1, Bean2.class);
		
		Assert.assertEquals(b1.getName1(), b2.getName1());
		Assert.assertEquals(b1.getName2(), b2.getName2());
		Assert.assertEquals(b1.getName3(), "name3");
		Assert.assertNull(b2.getName4());
		Assert.assertNotNull(b2.getList());
		Assert.assertEquals(b1.getList().size(), b2.getList().size());
		
		Assert.assertNotNull(b2.getList().get(0).getBean2());
		Assert.assertEquals(b2.getList().get(0).getBean2(), b2);
		
		Assert.assertNotNull(b2.getList().get(1).getBean2());
		Assert.assertEquals(b2.getList().get(1).getBean2(), b2);
	}
	
	@Test
	public void testCopyWithList2(){
		Bean1 b1 = new Bean1();
		ReflectUtils.setFieldsDefaultValue(b1, String.class, ":name");
		
		
		Bean2 b2 = new Bean2();

		List<TestBean2> list1 = new ArrayList<TestBean2>();
		TestBean2 test1 = new TestBean2();
		test1.setName1("test1Name1");
		test1.setName2("test1Name2");
		test1.setName4("test1Name4");
		list1.add(test1);
		
		TestBean2 test2 = new TestBean2();
		test2.setName1("test2Name1");
		test2.setName2("test2Name2");
		test2.setName4("test2Name4");
		list1.add(test2);
		
		b2.setList2(list1);
		duplicator.map(b1, b2);
		
		Assert.assertEquals(b1.getName1(), b2.getName1());
		Assert.assertEquals(b1.getName2(), b2.getName2());

		Assert.assertNull(b1.getList());
		Assert.assertNotNull(b2.getList2());
	}
	
	@Test
	public void testList(){
		List<Map> list = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 1l);
		map.put("userName", "test");
		list.add(map);
		
		List<UserEntity> userlist = duplicator.mapList(list, UserEntity.class);
		for(UserEntity u : userlist){
			System.out.println("id: " + u.getId());
			Assert.assertEquals(map.get("id"), u.getId());
		}
	}

}

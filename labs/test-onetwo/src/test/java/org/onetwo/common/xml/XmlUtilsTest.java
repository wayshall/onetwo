package org.onetwo.common.xml;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.xml.XmlUtils;

public class XmlUtilsTest {
	
	static class TestPerson {
		private String userName;
		private int age;
		private TestPerson parent;
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		public TestPerson getParent() {
			return parent;
		}
		public void setParent(TestPerson parent) {
			this.parent = parent;
		}
		
	}
	
	@Test
	public void testPerson(){
		TestPerson hh = new TestPerson();
		hh.setUserName("hanhan");
		hh.setAge(30);
		
		TestPerson parent = new TestPerson();
		parent.setUserName("hanrenjun");
		parent.setAge(60);
		
		hh.setParent(parent);
		
		String xmlStr = XmlUtils.toXML(hh, "person", TestPerson.class).trim();
		
		System.out.println("testPerson xml:\n " + xmlStr);
		
		TestPerson newHH = XmlUtils.toBean(xmlStr, "person", TestPerson.class);
		Assert.assertEquals(hh.getUserName(), newHH.getUserName());
	}
	
	@Test
	public void testPersonList(){
		TestPerson hh = new TestPerson();
		hh.setUserName("hanhan");
		hh.setAge(30);
		
		TestPerson parent = new TestPerson();
		parent.setUserName("hanrenjun");
		parent.setAge(60);
		
		hh.setParent(parent);
		
		Collection<TestPerson> lists = CUtils.newHashSet();
		lists.add(hh);
		
		String xmlStr = XmlUtils.toXML(lists, "personlist", Set.class, "person", TestPerson.class);
		
		System.out.println("testPersonList xml:\n " + xmlStr);
		
		Collection<TestPerson> list = XmlUtils.toBean(xmlStr, "personlist", List.class, "person", TestPerson.class);
		Assert.assertEquals(hh.getUserName(), list.iterator().next().getUserName());
	}

	
	@Test
	public void test2Bean(){
		String path = this.getClass().getResource("test.xml").getFile();
		System.out.println("path: " + path);
		String xml = FileUtils.readAsString(path);
		System.out.println("xml: " + xml);
		TestPerson person = XmlUtils.toBean(xml, "person", TestPerson.class);
//		System.out.println("person:" + LangUtils.toString(person));
		Assert.assertEquals(30, person.getAge());
		
		
		path = this.getClass().getResource("test_list.xml").getFile();
		System.out.println("path: " + path);
		xml = FileUtils.readAsString(path);
		System.out.println("xml: " + xml);
		
		Collection<TestPerson> list = XmlUtils.toBean(xml, "personlist", List.class, "person", TestPerson.class);
//		System.out.println("person:" + LangUtils.toString(person));
		Assert.assertEquals(30, list.iterator().next().getAge());
	}
}

package org.onetwo.common.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.utils.CUtils;

import com.google.common.collect.Lists;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class XmlUtilsTest {
	
	static class TestPerson {
		private String userName;
		private Integer age;
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
	
	@XStreamAlias("person")
	static class TestPersonAnnotation {
		private String userName;
		private int age;
		
		@XStreamAlias("parent")
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
	@XStreamAlias("persons")
	static class TestPersonAnnotationList {
		@XStreamImplicit
		List<TestPersonAnnotation> list = Lists.newArrayList();

	}
	
	static class EmptyTestPersonAnnotationConverter implements Converter {

		@Override
		public boolean canConvert(Class type) {
			return TestPersonAnnotation.class.equals(type);
		}

		@Override
		public void marshal(Object source, HierarchicalStreamWriter writer,
				MarshallingContext context) {
			TestPersonAnnotation p = (TestPersonAnnotation) source;
			writer.startNode("name");
			writer.setValue(p.getUserName());
			writer.endNode();
			writer.startNode("age");
			writer.setValue(""+p.getAge());
			writer.endNode();
			writer.startNode("parent");
			writer.setValue("");
			writer.endNode();
		}

		@Override
		public Object unmarshal(HierarchicalStreamReader reader,
				UnmarshallingContext context) {
			return null;
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
	public void testPersonListFormEmptyElement(){
//		XStream xstream = new XStream(new DomDriver());
		XStream xstream = new XStream();
		xstream.processAnnotations(TestPersonAnnotationList.class);
		xstream.processAnnotations(TestPersonAnnotation.class);
		xstream.processAnnotations(TestPerson.class);
//		xstream.registerConverter(new EmptyTestPersonAnnotationConverter());
		
		TestPersonAnnotation hh = new TestPersonAnnotation();
		hh.setUserName("hanhan");
		hh.setAge(30);
		TestPerson parent = new TestPerson();
//		parent.setAge(11);
		hh.setParent(null);
		
		TestPersonAnnotationList list = new TestPersonAnnotationList();
		list.list.add(hh);
		String xmlStr = xstream.toXML(list);
		/*StringWriter sw = new StringWriter();
		PrettyPrintWriter writer = new CompactWriter(sw);
		xstream.marshal(list, writer);
		String xmlStr = sw.toString();*/
		
		System.out.println("testPersonListFormEmptyElement xml:\n " + xmlStr);
		
		xmlStr = XmlUtils.toXML(Lists.newArrayList(hh), "list", ArrayList.class, 
				"person", TestPersonAnnotation.class);
		System.out.println("testPersonListFormEmptyElement xml22:\n " + xmlStr);
		
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

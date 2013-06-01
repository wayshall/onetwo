package org.onetwo.common.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Assert;
import org.junit.Test;

public class JaxbMapperTest {

	protected TestXmlBean createBean(){
		TestXmlBean bean = new TestXmlBean();
		bean.setName("勺子");
		bean.setEmail("test@email.com");
		bean.setBirthday(new Date());
		return bean;
	}

	@XmlRootElement
	public static class TestXmlBeanList {

		@XmlElement(name="testXmlBean")
		protected Collection<TestXmlBean> list;
	}
	
	@Test
	public void testJaxb(){
		TestXmlBean bean = createBean();
		
		String xml = JaxbMapperUtilsTest.toXml(bean);
		System.out.println(" testJaxb xml: " + xml);
		

		TestXmlBean b = JaxbMapperUtilsTest.fromXml(xml, TestXmlBean.class);
		Assert.assertNotNull(b);

		Assert.assertEquals(bean.getName(), b.getName());
	}
	
	@Test
	public void testJaxbList(){
		List<TestXmlBean> beans = new ArrayList<TestXmlBean>();
		TestXmlBean bean = createBean();
		beans.add(bean);
		bean = createBean();
		beans.add(bean);
		
		TestXmlBeanList t = new TestXmlBeanList();
		t.list = beans;
		String xml = JaxbMapperUtilsTest.toXml(t);
		System.out.println(" testJaxbList xml: " + xml);
		

		TestXmlBeanList obj = JaxbMapperUtilsTest.fromXml(xml, TestXmlBeanList.class);
		Assert.assertNotNull(obj);
		Assert.assertEquals(beans.size(), obj.list.size());
		
		for(TestXmlBean b : (Collection<TestXmlBean>) obj.list){
			Assert.assertEquals(bean.getName(), b.getName());
		}
	}
	
	@Test
	public void testFromFile(){
		String file = "testXmlBean.xml";
		TestXmlBeanList obj = JaxbMapperUtilsTest.fromFile(file, TestXmlBeanList.class);

		Assert.assertNotNull(obj);
		Assert.assertEquals(2, obj.list.size());
		
		for(TestXmlBean b : (Collection<TestXmlBean>) obj.list){
			Assert.assertEquals("勺子", b.getName());
		}
	}

}

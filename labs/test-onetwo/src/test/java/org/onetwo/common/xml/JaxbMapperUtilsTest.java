package org.onetwo.common.xml;

import java.util.Collection;
import java.util.List;

import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.xml.jaxb.JaxbMapper;
import org.onetwo.common.xml.JaxbMapperTest.TestXmlBeanList;

@SuppressWarnings("unchecked")
public class JaxbMapperUtilsTest {
	
	final private static JaxbMapper mapper;
	
	static {
		List<Class<?>> rootypes = CUtils.aslist(
				TestXmlBeanList.class,
					TestXmlBean.class
				);
		mapper = JaxbMapper.createMapper(rootypes);
	}

	public static JaxbMapper getMapper() {
		return mapper;
	}

	public static String toXml(Object root) {
		return mapper.toXml(root);
	}

	public static String toXml(Object root, String encoding) {
		return mapper.toXml(root, encoding);
	}

	public static String toXml(Collection<?> root, String rootName) {
		return mapper.toXml(root, rootName);
	}

	public static String toXml(Collection<?> root, String rootName, String encoding) {
		return mapper.toXml(root, rootName, encoding);
	}

	public static <T> T fromXml(String xml, Class<T> type) {
		return mapper.fromXml(xml, type);
	}

	public static <T> T fromFile(String path, Class<T> type) {
		return mapper.fromFile(path, type);
	}
	
}

package org.onetwo.common.spring.utils;

import javax.xml.bind.annotation.XmlRootElement;

import org.onetwo.common.utils.ReflectUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReader;

public class JaxbClassFilter implements ScanResourcesCallback<Class<?>> {
	
	public static final JaxbClassFilter Instance = new JaxbClassFilter();
	

	@Override
	public boolean isCandidate(MetadataReader metadataReader) {
		String clsName = metadataReader.getClassMetadata().getClassName();
		if(clsName.startsWith("org.junit.") || clsName.startsWith("org.onetwo.common.test.")){
			return false;
		}
		if (metadataReader.getAnnotationMetadata().hasAnnotation(XmlRootElement.class.getName()))
			return true;

		return false;
	}

	@Override
	public Class<?> doWithCandidate(MetadataReader metadataReader, Resource clazz, int count) {
		Class<?> cls = ReflectUtils.loadClass(metadataReader.getClassMetadata().getClassName());
		return cls;
	};

}

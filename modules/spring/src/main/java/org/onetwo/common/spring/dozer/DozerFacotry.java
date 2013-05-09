package org.onetwo.common.spring.dozer;

import java.util.List;
import java.util.Properties;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.utils.JFishResourcesScanner;
import org.onetwo.common.spring.utils.ResourcesScanner;
import org.onetwo.common.spring.utils.ScanResourcesCallback;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.bean.BeanDuplicator;
import org.onetwo.common.utils.bean.BeanDuplicatorFacotry;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReader;

public class DozerFacotry {

	private final static ResourcesScanner scaner = new JFishResourcesScanner();
	

	public static BeanDuplicator createBeanDuplicator(ClassLoader cld, Class<?> rootClass){
		return createBeanDuplicator(cld, rootClass.getPackage().getName());
	}
	public static BeanDuplicator createBeanDuplicator(ClassLoader cld, String...basePackages){
		List<DozerClassMapper> dozerClasses = scaner.scan(new ScanResourcesCallback<DozerClassMapper>() {

			@Override
			public boolean isCandidate(MetadataReader metadataReader) {
				String clsName = metadataReader.getClassMetadata().getClassName();
				if(clsName.startsWith("org.junit") || clsName.endsWith("Test")){
					return false;
				}
				if (metadataReader.getAnnotationMetadata().hasAnnotation(DozerMapping.class.getName()))
					return true;

				return false;
			}

			@Override
			public DozerClassMapper doWithCandidate(MetadataReader metadataReader, Resource clazz, int count) {
				Class<?> cls = ReflectUtils.loadClass(metadataReader.getClassMetadata().getClassName(), false);
				DozerClassMapper mapper = new DozerClassMapper(cls, cls.getAnnotation(DozerMapping.class));
				return mapper;
			};
			
		}, basePackages);
		
		/*if(LangUtils.isEmpty(dozerClasses))
			throw new JFishException("can not find any dozer class.");*/
		
		try {
			BeanDuplicator dupplicator = null;
			Properties props = BeanDuplicatorFacotry.loadConfig(cld, null);
			dupplicator = new JFishDozer(dozerClasses, props);
			dupplicator.initDozer();
			return dupplicator;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new BaseException("create dozer mapper error: " + e.getMessage(), e);
		}
	}
	
	private DozerFacotry(){
	}
}

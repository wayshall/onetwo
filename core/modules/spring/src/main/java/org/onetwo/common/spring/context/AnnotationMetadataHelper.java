package org.onetwo.common.spring.context;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.utils.IgnoreAnnotationClassPathScanningCandidateComponentProvider;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * @author wayshall
 * <br/>
 */
public class AnnotationMetadataHelper {
	
	public static ClassPathScanningCandidateComponentProvider createAnnotationScanner(ClassLoader classLoader, Class<? extends Annotation> annoClass){
		IgnoreAnnotationClassPathScanningCandidateComponentProvider scanner = new IgnoreAnnotationClassPathScanningCandidateComponentProvider(false);
		if(annoClass!=null){
			scanner.addIncludeFilter(new AnnotationTypeFilter(annoClass));
		}
		scanner.setClassLoader(classLoader);
		return scanner;
	}

	private final AnnotationMetadata importingClassMetadata;
//	private final Class<?> annotationType;
	private final AnnotationAttributes attributes;
	private ResourceLoader resourceLoader;
	private ClassLoader classLoader;
	
	public AnnotationMetadataHelper(AnnotationMetadata classMetadata, Class<?> annotationType) {
		super();
		this.importingClassMetadata = classMetadata;
//		this.annotationType = annotationType;
		
		AnnotationAttributes attributes = SpringUtils.getAnnotationAttributes(classMetadata, annotationType);
		if (attributes == null) {
			throw new IllegalArgumentException(String.format("@%s is not present on importing class '%s' as expected", annotationType.getSimpleName(), classMetadata.getClassName()));
		}
		this.attributes = attributes;
	}
	
	public Stream<BeanDefinition> scanBeanDefinitions(Class<? extends Annotation> annoClass, String...extraPackagesToScans){
		ClassPathScanningCandidateComponentProvider scanner = createAnnotationScanner(classLoader, annoClass);
		if(resourceLoader!=null){
			scanner.setResourceLoader(resourceLoader);
		}
		/*Set<String> basePackages = getBasePackages();
		for (String basePackage : basePackages) {
			Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
			for (BeanDefinition candidateComponent : candidateComponents) {
				consumer.accept(candidateComponent);
			}
		}*/
		Set<String> basePackages = getBasePackages();
		if(!LangUtils.isEmpty(extraPackagesToScans)){
			basePackages.addAll(Arrays.asList(extraPackagesToScans));
		}
		return basePackages.stream()
							.flatMap(pack->scanner.findCandidateComponents(pack).stream());
	}
	

	protected Set<String> getBasePackages() {
		Set<String> basePackages = new HashSet<>();
		
		Consumer<String[]> appendPackagesFunc = values -> {
			if(values==null){
				return ;
			}
			Stream.of(values)
					.filter(StringUtils::hasText)
					.forEach(basePackages::add);
		};
		appendPackagesFunc.accept((String[]) attributes.get("value"));
		appendPackagesFunc.accept((String[]) attributes.get("basePackages"));
		
		Stream.of((Class[]) attributes.get("basePackageClasses"))
										.map(ClassUtils::getPackageName)
										.forEach(basePackages::add);

		// 如果注解没有设置属性，则默认使用启用了这个注解的类所在的包
		if(LangUtils.isEmpty(basePackages)) {
			basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
		}
		return basePackages;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public AnnotationAttributes getAttributes() {
		return attributes;
	}

}

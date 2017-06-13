package org.onetwo.common.spring.utils;

import java.lang.annotation.Annotation;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.util.ClassUtils;

/**
 * @author wayshall
 * <br/>
 */
public class IgnoreAnnotationClassPathScanningCandidateComponentProvider extends ClassPathScanningCandidateComponentProvider {

	private ClassLoader classLoader;
	
	public IgnoreAnnotationClassPathScanningCandidateComponentProvider(boolean useDefaultFilters) {
		super(useDefaultFilters);
	}
	
	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		if (beanDefinition.getMetadata().isIndependent()) {
			// TODO until SPR-11711 will be resolved
			if (beanDefinition.getMetadata().isInterface()
					&& beanDefinition.getMetadata()
							.getInterfaceNames().length == 1
					&& Annotation.class.getName().equals(beanDefinition
							.getMetadata().getInterfaceNames()[0])) {
				try {
					Class<?> target = ClassUtils.forName(
							beanDefinition.getMetadata().getClassName(), classLoader);
					return !target.isAnnotation();
				}
				catch (Exception ex) {
					this.logger.error("Could not load target class: " + beanDefinition.getMetadata().getClassName(), ex);

				}
			}
			return true;
		}
		return false;

	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	
}

package org.onetwo.common.spring.ejbcontext;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.ejb.Remote;

import org.apache.commons.lang.StringUtils;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopeMetadataResolver;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;

public class ExtClassPathBeanScanner  extends ClassPathBeanDefinitionScanner implements ResourceLoaderAware {
	
	private static class SimpleBeanNameGenerator implements BeanNameGenerator {

		@Override
		public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
			String beanName = (String)definition.getAttribute("name");
			if(beanName.endsWith(EJB_INTERFACE_POSTFIX))
				beanName = beanName.substring(0, beanName.length()-EJB_INTERFACE_POSTFIX.length());
			if(!beanName.endsWith(EJB_NAME_POSTFIX))
				beanName += EJB_NAME_POSTFIX ;
			return beanName;
		}
	}
		

	protected static final String DEFAULT_PARENT_NAME = "abstractSLSB";
	protected static final String DEFAULT_EJB_RESOURCE_PATTERN = "**/*Remote.class";
	protected static final String EJB_INTERFACE_POSTFIX = "Remote";
	protected static final String EJB_NAME_POSTFIX = "SLSB";
	
	private String resourcePattern = DEFAULT_EJB_RESOURCE_PATTERN;
	private String parent = DEFAULT_PARENT_NAME;
	
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	private BeanNameGenerator beanNameGenerator = new SimpleBeanNameGenerator();
	private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
	
	public ExtClassPathBeanScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
		super(registry, useDefaultFilters);
	}


	public ExtClassPathBeanScanner(BeanDefinitionRegistry registry) {
		super(registry, true);
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
	
	protected void registerDefaultFilters() {
		this.addIncludeFilter(new AnnotationTypeFilter(Remote.class));
	}


	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<BeanDefinitionHolder>();
		for (int i = 0; i < basePackages.length; i++) {
			Set<BeanDefinition> candidates = findCandidateComponents(basePackages[i]);
			for (BeanDefinition candidate : candidates) {
				String beanName = this.beanNameGenerator.generateBeanName(candidate, this.getRegistry());
				if (candidate instanceof AbstractBeanDefinition) {
					postProcessBeanDefinition((AbstractBeanDefinition) candidate, beanName);
				}
				ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(candidate);
				if (checkCandidate(beanName, candidate)) {
					BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(candidate, beanName);
					definitionHolder = applyScope(definitionHolder, scopeMetadata);
					beanDefinitions.add(definitionHolder);
					registerBeanDefinition(definitionHolder, this.getRegistry());
				}
			}
		}
		return beanDefinitions;
	}
	
	private BeanDefinitionHolder applyScope(BeanDefinitionHolder definitionHolder, ScopeMetadata scopeMetadata) {
		String scope = scopeMetadata.getScopeName();
		ScopedProxyMode scopedProxyMode = scopeMetadata.getScopedProxyMode();
		definitionHolder.getBeanDefinition().setScope(scope);
		if (BeanDefinition.SCOPE_SINGLETON.equals(scope) || BeanDefinition.SCOPE_PROTOTYPE.equals(scope) ||
				scopedProxyMode.equals(ScopedProxyMode.NO)) {
			return definitionHolder;
		}
		boolean proxyTargetClass = scopedProxyMode.equals(ScopedProxyMode.TARGET_CLASS);
		return ScopedProxyCreator.createScopedProxy(definitionHolder, this.getRegistry(), proxyTargetClass);
	}
	
	protected BeanDefinition createBeanDefinition(MetadataReader metadataReader){
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.childBeanDefinition(this.parent);
		String clsName = metadataReader.getClassMetadata().getClassName();
		String beanName = StringUtils.substringAfterLast(clsName, ".");
		bdb.getBeanDefinition().setAttribute("name", StringUtils.uncapitalize(beanName));
		bdb.addPropertyValue("businessInterface", metadataReader.getClassMetadata().getClassName());
		bdb.addPropertyValue("jndiName", clsName);
		BeanDefinition bdf = bdb.getBeanDefinition();
		
		return bdf;
	}
	
	public Set<BeanDefinition> findCandidateComponents(String basePackage) {
		Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>();
		try {
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
					resolveBasePackage(basePackage) + "/" + this.resourcePattern;
			Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
			boolean traceEnabled = logger.isTraceEnabled();
			for (int i = 0; i < resources.length; i++) {
				Resource resource = resources[i];
				if (traceEnabled) {
					logger.trace("Scanning " + resource);
				}
				if (resource.isReadable()) {
					MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
					if (isCandidateComponent(metadataReader)) {
//						ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
						BeanDefinition bdf = this.createBeanDefinition(metadataReader);
						candidates.add(bdf);
					}
					else {
						if (traceEnabled) {
							logger.trace("Ignored because not matching any filter: " + resource);
						}
					}
				}
				else {
					if (traceEnabled) {
						logger.trace("Ignored because not readable: " + resource);
					}
				}
			}
		}
		catch (IOException ex) {
			throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
		}
		return candidates;
	}

	protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
		String clsName = metadataReader.getClassMetadata().getClassName();
		System.out.println("clsName:" + clsName);
		if(clsName.endsWith("Remote"))
			return true;
		return super.isCandidateComponent(metadataReader);
	}
	public void setResourcePattern(String resourcePattern) {
		Assert.notNull(resourcePattern, "'resourcePattern' must not be null");
		super.setResourcePattern(resourcePattern);
		this.resourcePattern = resourcePattern;
	}
	private static class ScopedProxyCreator {

		public static BeanDefinitionHolder createScopedProxy(
				BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry, boolean proxyTargetClass) {

			return ScopedProxyUtils.createScopedProxy(definitionHolder, registry, proxyTargetClass);
		}
	}
}

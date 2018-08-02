package org.onetwo.boot.module.swagger;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.onetwo.boot.plugin.core.PluginManager;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ClassUtils;

import springfox.documentation.RequestHandler;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Sets;

public class AutoScanPluginSwaggerConfig extends AbstractSwaggerConfig {
	
	@Autowired(required=false)
	private PluginManager pluginManager;
	
	protected Set<Predicate<RequestHandler>> getPluginBasePackages(){
    	if(pluginManager!=null){
	    	return pluginManager.getPlugins()
	    				.stream()
	    				.map(p->RequestHandlerSelectors.basePackage(ClassUtils.getPackageName(p.getRootClass())))
	    				.collect(Collectors.toSet());
    	}
    	return Collections.emptySet();
	}
	
	protected Set<Predicate<RequestHandler>> getAllApiDocPackages(){
		Set<Predicate<RequestHandler>> packages = Sets.newHashSet();
    	packages.addAll(getPluginBasePackages());
    	String scanPackageName = getScanPackageName();//ClassUtils.getPackageName(ServiceApplication.class);
    	if(StringUtils.isNotBlank(scanPackageName)){
    		packages.add(RequestHandlerSelectors.basePackage(scanPackageName));
    	}
    	return packages;
	}
	
	protected Predicate<RequestHandler> webApi(){
		Set<Predicate<RequestHandler>> packages = getAllApiDocPackages();
    	return webApi(packages);
	}

	protected Predicate<RequestHandler> notWebApi(){
		Set<Predicate<RequestHandler>> packages = getAllApiDocPackages();
    	return notWebApi(packages);
	}

    @Bean
    public Docket api(){
    	Set<Predicate<RequestHandler>> packages = getAllApiDocPackages();
    	Docket docket = createDocket(packages);
    	return docket;
    }
    
    protected Docket createDocket(Set<Predicate<RequestHandler>> packages){
    	Docket docket = new Docket(DocumentationType.SWAGGER_2)
								.ignoredParameterTypes(ApiIgnore.class)
						//		.pathProvider(pathProvider)
						        .select()
						            .apis(Predicates.or(packages))
						            .paths(PathSelectors.any())
						        .build()
						        .apiInfo(apiInfo(getServiceName()));
    	return docket;
    }
    
    protected String getScanPackageName(){
    	return null;
    }
    
    protected String getServiceName(){
    	return "JFish Service";
    }
}

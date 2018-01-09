package org.onetwo.boot.module.swagger;

import java.util.ArrayList;
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
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Sets;

public class AutoScanPluginSwaggerConfig {
	
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

    @Bean
    public Docket api(){
    	Set<Predicate<RequestHandler>> packages = Sets.newHashSet();
    	packages.addAll(getPluginBasePackages());
    	String scanPackageName = getScanPackageName();//ClassUtils.getPackageName(ServiceApplication.class);
    	if(StringUtils.isNotBlank(scanPackageName)){
    		packages.add(RequestHandlerSelectors.basePackage(scanPackageName));
    	}
    	Docket docket = new Docket(DocumentationType.SWAGGER_2)
		    		.ignoredParameterTypes(ApiIgnore.class)
//		    		.pathProvider(pathProvider)
		            .select()
			            .apis(Predicates.or(packages))
			            .paths(PathSelectors.any())
		            .build()
		            .apiInfo(apiInfo());
    	return docket;
    }
    
    protected Docket configDocket(Docket docket){
    	return docket;
    }
    
    protected String getScanPackageName(){
    	return null;
    }
    
    protected String getServiceName(){
    	return "JFish Service";
    }

    @SuppressWarnings("rawtypes")
    protected ApiInfo apiInfo() {
    	String serviceName = getServiceName();
        ApiInfo apiInfo = new ApiInfo(
        	serviceName,
        	serviceName = " REST API",
            "1.0",
            "termsOfServiceUrl",
            new Contact("wayshall", "", "wayshall@qq.com"),
            "API License",
            "API License URL",
            new ArrayList<VendorExtension>());
        return apiInfo;
    }
}

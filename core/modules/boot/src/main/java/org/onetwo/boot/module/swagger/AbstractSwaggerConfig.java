package org.onetwo.boot.module.swagger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.onetwo.boot.core.web.api.WebApiRequestMappingCombiner;

import springfox.documentation.RequestHandler;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

/****
 * @author wayshall
 *
 */
public class AbstractSwaggerConfig {

    protected List<Parameter> createGlobalParameters(){
		List<Parameter> parameters = Lists.newArrayList();
		return parameters;
    }

	@SuppressWarnings("deprecation")
	protected Predicate<RequestHandler> webApi(Collection<Predicate<RequestHandler>> packages){
    	return rh->{
        	return Predicates.or(packages).apply(rh) && 
        								WebApiRequestMappingCombiner.findWebApiAttrs(rh.getHandlerMethod().getMethod(), 
        															rh.declaringClass())
        															.isPresent();
        };
	}

	@SuppressWarnings("deprecation")
	protected Predicate<RequestHandler> notWebApi(Collection<Predicate<RequestHandler>> packages){
    	return rh->{
        	return Predicates.or(packages).apply(rh) && 
										!WebApiRequestMappingCombiner.findWebApiAttrs(rh.getHandlerMethod().getMethod(), 
																	rh.declaringClass())
																	.isPresent();
        };
	}

    protected Docket createDocket(String groupName, String applicationName, Collection<Predicate<RequestHandler>> packages){
    	Docket docket = new Docket(DocumentationType.SWAGGER_2)
								.ignoredParameterTypes(ApiIgnore.class)
								.groupName(groupName)
						//		.pathProvider(pathProvider)
						        .select()
						            .apis(Predicates.or(packages))
						            .paths(PathSelectors.any())
						        .build()
						        .globalOperationParameters(createGlobalParameters())
						        .apiInfo(apiInfo(applicationName));
    	return docket;
    }
    
    @SuppressWarnings("rawtypes")
    protected ApiInfo apiInfo(String applicationName) {
    	String serviceName = applicationName;
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

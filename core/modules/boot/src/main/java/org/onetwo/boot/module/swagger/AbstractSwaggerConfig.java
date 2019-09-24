package org.onetwo.boot.module.swagger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.onetwo.boot.core.web.api.WebApiRequestMappingCombiner;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import springfox.documentation.RequestHandler;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/****
 * @author wayshall
 *
 */
public class AbstractSwaggerConfig {
	@Autowired
    private TypeResolver typeResolver;
	
    protected List<Parameter> createGlobalParameters(){
		List<Parameter> parameters = Lists.newArrayList();
		return parameters;
    }
    protected Map<List<RequestMethod>, List<ResponseMessage>> createGlobalResponseMessages(){
    	return Collections.emptyMap();
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
								.alternateTypeRules(
										AlternateTypeRules.newRule(
												typeResolver.resolve(DeferredResult.class, typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
												typeResolver.resolve(WildcardType.class)
		                                )
								)
						//		.pathProvider(pathProvider)
						        .select()
						            .apis(Predicates.or(packages))
						            .paths(PathSelectors.any())
						        .build()
						        .globalOperationParameters(createGlobalParameters())
						        .apiInfo(apiInfo(applicationName));
    	
    	addGlobalResponseMessages(docket);
    	return docket;
    }
    
    protected Docket addGlobalResponseMessages(Docket docket){
    	Map<List<RequestMethod>, List<ResponseMessage>> globalResponses = this.createGlobalResponseMessages();
    	if(LangUtils.isNotEmpty(globalResponses)){
    		globalResponses.forEach((methods, value)->{
    			methods.forEach(method->{
        			docket.globalResponseMessage(method, value);
    			});
    		});
    	}
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

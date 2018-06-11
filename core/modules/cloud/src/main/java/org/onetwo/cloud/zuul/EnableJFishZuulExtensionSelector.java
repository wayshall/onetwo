package org.onetwo.cloud.zuul;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.boot.core.web.mvc.ErrorHandleConfiguration;
import org.onetwo.boot.core.web.mvc.log.AccessLogConfiguration;
import org.onetwo.cloud.zuul.limiter.LimiterConfiguration;
import org.onetwo.common.spring.context.AbstractImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wayshall
 * <br/>
 */
public class EnableJFishZuulExtensionSelector extends AbstractImportSelector<EnableJFishZuulExtension> {

	
	@Override
	protected List<String> doSelect(AnnotationMetadata metadata, AnnotationAttributes attributes) {
		List<String> classNames = new ArrayList<String>();
		
		classNames.add(ExtZuulContextConfig.class.getName());
		classNames.add(ErrorHandleConfiguration.class.getName());
		classNames.add(AccessLogConfiguration.class.getName());
		classNames.add(LimiterConfiguration.class.getName());
		
		return classNames;
	}


}

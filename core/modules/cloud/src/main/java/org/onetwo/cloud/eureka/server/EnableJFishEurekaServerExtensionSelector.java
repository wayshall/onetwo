package org.onetwo.cloud.eureka.server;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.spring.context.AbstractImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wayshall
 * <br/>
 */
public class EnableJFishEurekaServerExtensionSelector extends AbstractImportSelector<EnableJFishEurekaServerExtension> {

	
	@Override
	protected List<String> doSelect(AnnotationMetadata metadata, AnnotationAttributes attributes) {
		List<String> classNames = new ArrayList<String>();
		
		classNames.add(ExtEurekaServerConfiguration.class.getName());
		
		return classNames;
	}


}

package org.onetwo.cloud;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.boot.core.web.mvc.log.AccessLogConfiguration;
import org.onetwo.cloud.core.BootCloudConfigration;
import org.onetwo.common.spring.context.AbstractImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wayshall
 * <br/>
 */
public class EnableJFishCloudExtensionSelector extends AbstractImportSelector<EnableJFishCloudExtension> {

	
	@Override
	protected List<String> doSelect(AnnotationMetadata metadata, AnnotationAttributes attributes) {
		List<String> classNames = new ArrayList<String>();
		
		classNames.add(BootCloudConfigration.class.getName());
		classNames.add(AccessLogConfiguration.class.getName());
		
		return classNames;
	}


}

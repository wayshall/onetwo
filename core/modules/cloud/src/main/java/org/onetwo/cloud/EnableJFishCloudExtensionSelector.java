package org.onetwo.cloud;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.boot.core.shutdown.GraceKillConfiguration;
import org.onetwo.boot.core.web.mvc.ErrorHandleConfiguration;
import org.onetwo.boot.core.web.mvc.log.AccessLogConfiguration;
import org.onetwo.cloud.core.BootCloudConfigration;
import org.onetwo.cloud.feign.local.LocalFeignConfiguration;
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
		classNames.add(ErrorHandleConfiguration.class.getName());
		classNames.add(AccessLogConfiguration.class.getName());
		classNames.add(GraceKillConfiguration.class.getName());
		
		classNames.add(LocalFeignConfiguration.class.getName());
		
		return classNames;
	}


}

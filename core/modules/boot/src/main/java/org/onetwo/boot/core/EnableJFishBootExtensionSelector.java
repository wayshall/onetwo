package org.onetwo.boot.core;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.boot.core.EnableJFishBootExtension.AppcationType;
import org.onetwo.boot.core.ms.BootMSContextAutoConfig;
import org.onetwo.boot.core.web.BootWebUIContextAutoConfig;
import org.onetwo.boot.module.security.oauth2.OAuth2SsoClientAutoContextConfig;
import org.onetwo.common.spring.context.AbstractImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wayshall
 * <br/>
 */
public class EnableJFishBootExtensionSelector extends AbstractImportSelector<EnableJFishBootExtension>{

	@Override
	protected List<String> doSelect(AnnotationMetadata metadata, AnnotationAttributes attributes) {
		List<String> classNames = new ArrayList<String>();
		AppcationType appcationType = (AppcationType)attributes.get("appcationType");
		if(appcationType==AppcationType.WEB_SERVICE){
			classNames.add(BootMSContextAutoConfig.class.getName());
		}else if(appcationType==AppcationType.WEB_UI){
			classNames.add(BootWebUIContextAutoConfig.class.getName());
		}
		classNames.add(OAuth2SsoClientAutoContextConfig.class.getName());
		return classNames;
	}
	
	

}

package org.onetwo.boot.module.oauth2;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.spring.context.AbstractImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wayshall
 * <br/>
 */
public class EnableJFishOauth2Selector extends AbstractImportSelector<EnableJFishOauth2> {

	@Override
	protected List<String> doSelect(AnnotationMetadata metadata, AnnotationAttributes attributes) {
		List<String> classNames = new ArrayList<String>();
		
		if(attributes.getBoolean("authorizationServer")){
			classNames.add(AuthorizationServerConfiguration.class.getName());
		}
		
		if(attributes.getBoolean("resourceServer")){
			//可通过ResourceServerProps.ENABLED_KEY关掉
			classNames.add(ResourceServerConfiguration.class.getName());
		}
		
		return classNames;
	}

}

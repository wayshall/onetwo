package org.onetwo.boot.module.oauth2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.onetwo.boot.module.oauth2.EnableJFishOauth2.OAuth2Role;
import org.onetwo.boot.module.oauth2.authorize.AuthorizationServerConfiguration;
import org.onetwo.boot.module.oauth2.clientdetails.ClientDetailsResolverConfiguration;
import org.onetwo.boot.module.oauth2.resouce.ResourceServerConfiguration;
import org.onetwo.boot.module.oauth2.result.OAuth2CustomResultConfiguration;
import org.onetwo.boot.module.oauth2.token.Oauth2TokenStoreConfiguration;
import org.onetwo.common.spring.context.AbstractImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wayshall
 * <br/>
 */
public class EnableJFishOauth2Selector extends AbstractImportSelector<EnableJFishOauth2> {

	@Override
	protected Set<String> doSelect(AnnotationMetadata metadata, AnnotationAttributes attributes) {
		Set<String> classNames = new HashSet<String>();
		
		List<OAuth2Role> roles = Arrays.asList((OAuth2Role[])attributes.get("value"));
		
		if(roles.contains(OAuth2Role.AUTHORIZATION_SERVER)){
			classNames.add(AuthorizationServerConfiguration.class.getName());
			classNames.add(Oauth2TokenStoreConfiguration.class.getName());
			classNames.add(OAuth2CustomResultConfiguration.class.getName());
		}
		
		if(roles.contains(OAuth2Role.RESOURCE_SERVER)){
			if(!classNames.contains(Oauth2TokenStoreConfiguration.class.getName())){
				classNames.add(Oauth2TokenStoreConfiguration.class.getName());
			}
			//仍然可通过ResourceServerProps.ENABLED_KEY关掉
			classNames.add(ResourceServerConfiguration.class.getName());
			classNames.add(OAuth2CustomResultConfiguration.class.getName());
		}

		if(roles.contains(OAuth2Role.CLIENT_DETAILS_RESOLVER)){
			classNames.add(Oauth2TokenStoreConfiguration.class.getName());
			classNames.add(ClientDetailsResolverConfiguration.class.getName());
		}
		
		return classNames;
	}

}

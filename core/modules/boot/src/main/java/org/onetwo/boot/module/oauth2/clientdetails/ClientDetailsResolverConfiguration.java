package org.onetwo.boot.module.oauth2.clientdetails;

import org.onetwo.boot.module.oauth2.JFishOauth2Properties;
import org.onetwo.boot.module.oauth2.JFishOauth2Properties.ClientDetailsResolverProps;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@EnableConfigurationProperties(JFishOauth2Properties.class)
@ConditionalOnProperty(name=ClientDetailsResolverProps.ENABLED_KEY, matchIfMissing=true)
public class ClientDetailsResolverConfiguration {
	/*@Autowired
	private TokenStore tokenStore;*/

	/*@Bean
	@ConditionalOnMissingBean(ResourceServerTokenServices.class)
	public DefaultTokenServices oauth2ResourceServerTokenServices() {
		DefaultTokenServices services = new DefaultTokenServices();
		services.setTokenStore(tokenStore);
		return services;
	}*/
	@Bean
	@ConditionalOnMissingBean(ClientDetailsArgumentResolver.class)
	public ClientDetailsArgumentResolver clientDetailsArgumentResolver(ClientDetailsObtainService clientDetailsObtainService) {
		ClientDetailsArgumentResolver resolver = new ClientDetailsArgumentResolver();
		resolver.setClientDetailsObtainService(clientDetailsObtainService);
		return resolver;
	}
	
	@Bean
	@ConditionalOnMissingBean(ClientDetailConverter.class)
	public DefaultClientDetailConverter clientDetailConverter() {
		DefaultClientDetailConverter converter = new DefaultClientDetailConverter();
		return converter;
	}
	
	@Bean
	@ConditionalOnMissingBean(AccessTokkenClientDetailsObtainService.class)
	public ClientDetailsObtainService clientDetailsObtainService(ClientDetailConverter<?> clientDetailConverter){
		AccessTokkenClientDetailsObtainService obtainService = new AccessTokkenClientDetailsObtainService();
		obtainService.setClientDetailConverter(clientDetailConverter);
		return obtainService;
	}
	
	@Bean
	@ConditionalOnMissingBean(ClientDetailsMvcInterceptor.class)
	public ClientDetailsMvcInterceptor clientDetailsMvcInterceptor(){
		return new ClientDetailsMvcInterceptor();
	}
	
	@Bean
	@ConditionalOnMissingBean(Oauth2ClientDetailManager.class)
	public Oauth2ClientDetailManager oauth2ClientDetailManager(){
		return new DefaultOauth2ClientDetailManager();
	}

}

package org.onetwo.boot.dsrouter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
//@Import(DsRouterRegistrar.class)
@EnableConfigurationProperties(DatasourceRouterProperties.class)
@ConditionalOnProperty(prefix="jfish.dsRouter", name="enabled", havingValue="true", matchIfMissing=false)
public class DsRouterConfiguration {
	
	@Autowired
	private DatasourceRouterProperties dsrProperties;

	@Bean
	@Primary
	public DynamicDataSource dynamicDataSource(){
		return new DynamicDataSource();
	}
	
	@Bean
	public HeaderLookupKeyStrategy headerLookupKeyStrategy(){
		HeaderLookupKeyStrategy strategy = new HeaderLookupKeyStrategy();
		strategy.setHeaderName(dsrProperties.getHeaderStrategy().getHeaderName());
		return strategy;
	}

}

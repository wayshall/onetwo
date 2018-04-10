package org.onetwo.boot.module.oauth2.authorize;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.util.Assert;

/**
 * @author wayshall
 * <br/>
 */
abstract public class AbstractOAuth2ClientDetailsService implements ClientDetailsService, InitializingBean {
	
	@Autowired(required=false)
	private DataSource dataSource;
	
	private JdbcClientDetailsService jdbcClientDetailsService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(jdbcClientDetailsService==null){
			Assert.notNull(dataSource, "dataSource can not be null!");
			this.jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
			return ;
		}
		Assert.notNull(jdbcClientDetailsService, "jdbcClientDetailsService can not be null!");
	}

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
		ClientDetails clientDetail = loadClientDetails(authentication);
		return clientDetail;
	}
	
	abstract protected ClientDetails loadClientDetails(UsernamePasswordAuthenticationToken authentication) throws ClientRegistrationException;


	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	public void setJdbcClientDetailsService(JdbcClientDetailsService jdbcClientDetailsService) {
		this.jdbcClientDetailsService = jdbcClientDetailsService;
	}

	public JdbcClientDetailsService getJdbcClientDetailsService() {
		return jdbcClientDetailsService;
	}
	
}

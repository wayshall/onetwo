package org.onetwo.boot.module.oauth2;

import org.onetwo.boot.module.oauth2.util.PasswordEncoders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author wayshall
 * <br/>
 */
@ConditionalOnMissingBean(PasswordEncoder.class)
@Configuration
public class PasswordEncoderConfiguration {
	@Autowired
	private JFishOauth2Properties oauthProperties;
	
	@Bean
	@ConditionalOnProperty(JFishOauth2Properties.CONFIG_PREFIX+".passwordEncoder")
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = PasswordEncoders.newEncoder(oauthProperties.getPasswordEncoder());
		return encoder;
	}

}

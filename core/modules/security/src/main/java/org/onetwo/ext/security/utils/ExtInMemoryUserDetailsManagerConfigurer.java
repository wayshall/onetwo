package org.onetwo.ext.security.utils;

import java.util.ArrayList;
import java.util.Map;

import org.onetwo.ext.security.utils.SecurityConfig.MemoryUser;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.google.common.collect.Maps;

/**
 * @author weishao zeng
 * <br/>
 */
public class ExtInMemoryUserDetailsManagerConfigurer extends UserDetailsManagerConfigurer<AuthenticationManagerBuilder, ExtInMemoryUserDetailsManagerConfigurer> {

	
	public ExtInMemoryUserDetailsManagerConfigurer(Map<String, MemoryUser> memoryUsers) {
		super(new ExtInMemoryUserDetailsManager(memoryUsers));
	}
	
	public static class ExtInMemoryUserDetailsManager extends InMemoryUserDetailsManager {
		private Map<String, MemoryUser> memoryUsers = Maps.newHashMap();

		public ExtInMemoryUserDetailsManager(Map<String, MemoryUser> memoryUsers) {
			super(new ArrayList<UserDetails>());
		}
		
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			UserDetails user = super.loadUserByUsername(username);
			MemoryUser muser = this.memoryUsers.get(user.getUsername());
			if (muser.getUserId()!=null) {
				user = new LoginUserDetails(muser.getUserId(), username, user.getPassword(), user.getAuthorities());
			}
			return user;
		}
	}
}


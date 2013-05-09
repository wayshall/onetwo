package org.onetwo.common.web.s2.security.ext.acl;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.web.s2.security.config.AuthenticConfig;
import org.onetwo.common.web.s2.security.config.ConfigBuilder;
import org.onetwo.common.web.s2.security.config.annotation.Authentic;
import org.onetwo.common.web.s2.security.service.StrutsAuthenticConfigServiceImpl;
import org.onetwo.common.web.utils.UserKeyManager;
import org.springframework.stereotype.Service;

@Service(AclAuthenticConfigServiceImpl.NAME)
public class AclAuthenticConfigServiceImpl extends StrutsAuthenticConfigServiceImpl {

	public static final String NAME = "aclAuthenticConfigService";

	public static class AclConfigBuilder extends ConfigBuilder {

		public AclConfigBuilder(Class<?> clazz, Method method) {
			super(clazz, method);
		}

		protected AuthenticConfig getDefaultAuthenticConfig() {
			return AuthenticConfig.DEFAULT_CONIFG;
		}

		protected AuthenticConfig newAuthenticConfig(String name, Authentic authentic) {
			AclAuthenticConfig config = new AclAuthenticConfig(name, authentic.isOnlyAuthenticator());
			return config;
		}

		public AuthenticConfig buildAuthenConfig() {
			AuthenticConfig config = super.buildAuthenConfig();
			AclAuthentic acl = findAnnotation(getAuthenticClass(), getAuthenticMethod(), AclAuthentic.class);
			if (acl != null) {
				AclAuthenticConfig aclConfig = null;
				if(this.getDefaultAuthenticConfig().equals(config)){
					aclConfig = config.copy(AclAuthenticConfig.class);
				}else
					aclConfig = (AclAuthenticConfig) config;
				return this.buildAclAuthenticConfig(aclConfig, acl);
			}
			return config;
		}
		
		protected AclAuthenticConfig buildAclAuthenticConfig(AclAuthenticConfig config, AclAuthentic acl) {
			config.setResourceEntity(acl.resourceEntity());
			config.setPermissions(acl.permissions());
			config.setOperator(acl.operator());
			config.setResourceId(acl.resourceId());
			config.setEntityId(acl.entityId());
			config.setGetRights(acl.isGetRights());
			config.setIgnore(acl.ignore());

			if (StringUtils.isBlank(config.getAuthenticationName())) {
				String authenticName = UserKeyManager.getCurrentUserKeyManager().getAuthenticationName();
				if (StringUtils.isBlank(authenticName))
					authenticName = AclAuthenticConfig.ACL_AUTHENTICATION_NAME;
				config.setAuthenticationName(authenticName);
			}

			return config;
		}

	}


	protected ConfigBuilder getConfigBuilder(Class<?> clazz, Method method) {
		ConfigBuilder configBuilder = new AclConfigBuilder(clazz, method);
		return configBuilder;
	}

}

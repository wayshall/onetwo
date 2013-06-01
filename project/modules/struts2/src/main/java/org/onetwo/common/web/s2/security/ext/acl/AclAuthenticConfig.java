package org.onetwo.common.web.s2.security.ext.acl;

import org.onetwo.common.web.s2.security.config.AuthenticConfig;

public class AclAuthenticConfig extends AuthenticConfig{

	public static final String ACL_AUTHENTICATION_NAME = "aclAuthentication";
	
	protected String resourceEntity;
	
	protected String operator;
	
	protected String resourceId;
	
	protected String entityId;
	
	protected boolean getRights;

	public AclAuthenticConfig() {
		super(false);
	}
	
	public AclAuthenticConfig(String name, boolean onlyAuthenticator) {
		super(name, onlyAuthenticator);
	}

	public String getResourceEntity() {
		return resourceEntity;
	}

	public void setResourceEntity(String resourceEntity) {
		this.resourceEntity = resourceEntity;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public boolean isGetRights() {
		return getRights;
	}

	public void setGetRights(boolean getRights) {
		this.getRights = getRights;
	}

}

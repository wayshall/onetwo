package org.onetwo.common.web.ejb;

import org.springframework.ejb.access.SimpleRemoteStatelessSessionProxyFactoryBean;

public class SpringAutoLookupEjbJndi extends SimpleRemoteStatelessSessionProxyFactoryBean{

	private String moduleName ;
	
	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	public String getJndiName() {
		String jndiName = super.getJndiName();
		if(!jndiName.startsWith(this.getModuleName())){
			jndiName = this.getModuleName()+"#"+jndiName;
			this.setJndiName(jndiName);
			System.out.println("===auto jndi name: " + jndiName);
		}
		return jndiName;
	}
	
	
}

package org.onetwo.common.ioc.inject.impl;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.ioc.annotation.InjectRemote;
import org.onetwo.common.ioc.inject.InjectMeta;

@SuppressWarnings("unchecked")
public class InjectRemoteParser extends InjectLocalParser {
	
	public InjectRemoteParser(){
		super(InjectRemote.class);
	}
	
	protected Class getAnnotaionBusinessInterface(InjectMeta field){
		InjectRemote ejb = field.getAnnotation(InjectRemote.class);
		return ejb.businessInterface();
	}
	
	protected String getJndiName(InjectMeta field, Class bInterface){
		InjectRemote ejb = field.getAnnotation(InjectRemote.class);
		
		String jndiName = ejb.name();
		if(StringUtils.isBlank(jndiName))
			jndiName = bInterface.getName();
		
		String mappedName = ejb.mappedName();
		if(StringUtils.isNotBlank(mappedName))
			jndiName = ejb.mappedName() + "#" + jndiName;
		
		return jndiName;
	}

}

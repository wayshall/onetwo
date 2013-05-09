package org.onetwo.common.ejb.utils;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.utils.Assert;

public class WLEJBFinder extends EJBFinder {
	
	public static final String MODULE_NAME_KEY = "moduleName";

    public static WLEJBFinder create(String mNameKey){
    	return create("", mNameKey);
    }
    
    public static WLEJBFinder create(String jndi, String mNameKey){
    	if(StringUtils.isBlank(jndi))
    		jndi = "jndi-wl.properties";
    	if(StringUtils.isBlank(mNameKey))
    		mNameKey = MODULE_NAME_KEY;
    	WLEJBFinder ejbfinder = new WLEJBFinder(jndi, mNameKey);
    	ejbfinder.iniitContext();
    	return ejbfinder;
    }

	private String defaultModuleNameKey;
	
	public WLEJBFinder(String jndiConfigFile, String mNameKey){
		super(jndiConfigFile);
		this.defaultModuleNameKey = mNameKey;
	}
	
	public WLEJBFinder(Properties properties, String mNameKey){
		super(properties);
		this.defaultModuleNameKey = mNameKey;
	}

	protected void iniitContext(){
		 super.iniitContext();
	}
	
	public String getDefaultModuleName() {
		String key = this.getDefaultModuleNameKey();
		String mName = this.getJndiConfig().getProperty(key);
		Assert.hasText(mName, "can not find the module name: " + key);
		return mName;
	}
	
	public String getDefaultModuleNameKey() {
		if(StringUtils.isBlank(defaultModuleNameKey))
			this.defaultModuleNameKey = MODULE_NAME_KEY;
		return defaultModuleNameKey;
	}

	public <T> T getEJB(Class<T> clazz, boolean isRemote) {
		return getEJB(clazz.getName(), isRemote);
	}

	public String getTheJndiName(String ejbName, boolean isRemote){
        String moduleName = isRemote?(getDefaultModuleName()+ "#"):"";
		String jndi = moduleName + ejbName;
		return jndi;
	}
}

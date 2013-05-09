package org.onetwo.common.utils.propconf;

public class SystemConfig extends PropConfig{
	
	public static final String SYSTEM_CONFIG = "SystemConfig";
	
	public static SystemConfig instance = new SystemConfig(SYSTEM_CONFIG);
	
	private SystemConfig(String config){
		super(config);
	}
	
	public static SystemConfig getInstance(){
		return instance;
	}

}

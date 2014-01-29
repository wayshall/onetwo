package org.onetwo.common.utils.propconf;

import org.onetwo.common.log.MyLoggerFactory;

public enum Env {
	PRODUCT("生产环境"),
	DEV("开发环境"),
	TEST("测试环境"),
	DEV_LOCAL("本地开发环境"),
	TEST_LOCAL("本地测试环境"),;
	
	private final String label;
	Env(String label){
		this.label = label;
	}
	public String getLabel() {
		return label;
	}
	public String getValue(){
		return toString().toLowerCase();
	}
	
	public static Env of(String envstr){
		Env env = null;
		try {
			String value = envstr.toUpperCase();
			if(value.indexOf('-')!=-1)
				value = value.replace('-', '_');
			env = valueOf(value);
		} catch (Exception e) {
			MyLoggerFactory.getLogger(Env.class).error("no env found: " + envstr, e);
			env = DEV;
		}
		return env;
	}

}

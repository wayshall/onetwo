package org.onetwo.common.propconf;

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
		return deconvert(toString()).toLowerCase();
	}

	private static String convert(String src){
		return src.replace('-', '_');
	}
	private static String deconvert(String src){
		return src.replace('_', '-');
	}
	
	public static Env of(String envstr){
		Env env = null;
		try {
			String value = convert(envstr.toUpperCase());
			env = valueOf(value);
		} catch (Exception e) {
			MyLoggerFactory.getLogger(Env.class).error("no env found: " + envstr, e);
			env = DEV;
		}
		return env;
	}

}

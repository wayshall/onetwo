package org.onetwo.common.spring.cache;

public final class CacheKeys {
	
	private CacheKeys(){
	}
	
	/*********
	 * 静态缓存，不过时失效
	 */
	public static final String ETERNAL_CACHE = "eternalCache";
	

	/**********
	 * 方法缓存，过时会失效
	 */
	public static final String DYNAMIC_DATA_METHOD_CACHE = "dynamicDataMethodCache";

}

package org.onetwo.common.utils.propconf;

/****
 * @see Env
 * @author weishao
 *
 */
public class Environment {

	/******
	 * 定义环境
	 */
	public static final String PRODUCT = "product";
	public static final String DEV = "dev";
	public static final String TEST = "test";
	public static final String DEV_LOCAL = "dev_local";
	public static final String TEST_LOCAL = "test_local";
	
	/*public static class EnvLable {
		public static final Map<String, String> values = new HashMap<String, String>();
		
		public static final KVEntry<String, String> product = KVEntry.create(Environment.PRODUCT, "生产环境", values);
		public static final KVEntry<String, String> test = KVEntry.create(Environment.TEST, "测试环境", values);
		public static final KVEntry<String, String> dev = KVEntry.create(Environment.DEV, "开发环境", values);
		public static final KVEntry<String, String> dev_local = KVEntry.create(Environment.DEV_LOCAL, "本地开发环境", values);
		public static final KVEntry<String, String> test_local = KVEntry.create(Environment.TEST_LOCAL, "本地测试环境", values);
	}*/

}

package org.onetwo.common.data;


/**
 * @author wayshall
 * <br/>
 */
public interface DataResultWrapper {
	
	Object wrapResult(Object responseData);
	
	
	public class NoWrapper implements DataResultWrapper {
		public static final DataResultWrapper INSTANCE = new NoWrapper();
		@Override
		public Object wrapResult(Object responseData) {
			return responseData;
		}
	}

}

package org.onetwo.common.data;


/**
 * @author wayshall
 * <br/>
 */
public interface DataResultWrapper {
	
	Object wrapResult(Object responseData);
	
	
	public class NoWrapper implements DataResultWrapper {
		@Override
		public Object wrapResult(Object responseData) {
			return null;
		}
	}

}

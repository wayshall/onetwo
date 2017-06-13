package org.onetwo.common.data;
/**
 * @author wayshall
 * <br/>
 */
public interface DataResultWrapper {
	
	Result<?, ?> wrapResult(Object data);

}

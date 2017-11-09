package org.onetwo.ext.alimq;

import java.util.Optional;

/**
 * @author wayshall
 * <br/>
 */
@FunctionalInterface
public interface SendMessageErrorHandler<T> {

	/***
	 * 若optional不为空，则不会抛出异常
	 * @author wayshall
	 * @param e
	 * @return
	 */
	Optional<T> onError(Throwable e);
}

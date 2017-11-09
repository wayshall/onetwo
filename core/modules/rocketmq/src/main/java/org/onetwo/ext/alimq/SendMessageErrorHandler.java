package org.onetwo.ext.alimq;

import java.util.Optional;

/**
 * @author wayshall
 * <br/>
 */
@FunctionalInterface
public interface SendMessageErrorHandler<T> {

	Optional<T> onError(Throwable e);
}

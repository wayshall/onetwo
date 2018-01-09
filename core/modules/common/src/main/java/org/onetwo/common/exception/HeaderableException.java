package org.onetwo.common.exception;

import java.util.Map;
import java.util.Optional;

/**
 * @author wayshall
 * <br/>
 */
public interface HeaderableException {
	Optional<Map<String, String>> getHeaders();
}

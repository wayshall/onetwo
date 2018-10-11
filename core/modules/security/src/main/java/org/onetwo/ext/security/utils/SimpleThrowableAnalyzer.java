package org.onetwo.ext.security.utils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.security.web.util.ThrowableCauseExtractor;

/**
 * @author wayshall
 * <br/>
 */
@Slf4j
public class SimpleThrowableAnalyzer extends ThrowableAnalyzer {
	public SimpleThrowableAnalyzer(){
		super();
	}
	/**
	 * @see org.springframework.security.web.util.ThrowableAnalyzer#initExtractorMap()
	 */
	protected void initExtractorMap() {
		registerExtractor(Throwable.class, new ThrowableCauseExtractor() {
			public Throwable extractCause(Throwable throwable) {
				if(log.isErrorEnabled()){
					log.error("security error", throwable);
				}
				return throwable.getCause();
			}
		});
		super.initExtractorMap();
	}

}

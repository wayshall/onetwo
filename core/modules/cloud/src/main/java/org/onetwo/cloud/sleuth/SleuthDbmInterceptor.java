package org.onetwo.cloud.sleuth;

import org.onetwo.dbm.annotation.DbmInterceptorFilter;
import org.onetwo.dbm.annotation.DbmInterceptorFilter.InterceptorType;
import org.onetwo.dbm.jdbc.spi.DbmInterceptor;
import org.onetwo.dbm.jdbc.spi.DbmInterceptorChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;

/**
 * @author wayshall
 * <br/>
 */
@DbmInterceptorFilter(type=InterceptorType.JDBC)
public class SleuthDbmInterceptor implements DbmInterceptor {
	public static final String SPAN_NAME = "inner-dbm-jdbc";
	@Autowired
	private Tracer tracer;

	@Override
	public Object intercept(DbmInterceptorChain chain) {
		final Span span = tracer.createSpan(SPAN_NAME);
		try {
			span.tag(Span.SPAN_LOCAL_COMPONENT_TAG_NAME, SPAN_NAME);
			span.logEvent(Span.CLIENT_SEND);
			return chain.invoke();
		} finally{
//			span.tag(Span.SPAN_PEER_SERVICE_TAG_NAME, SPAN_NAME);
			span.logEvent(Span.CLIENT_RECV);
			tracer.close(span);
		}
	}

}

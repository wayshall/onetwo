package org.onetwo.cloud.sleuth;

import org.onetwo.dbm.annotation.DbmInterceptorFilter;
import org.onetwo.dbm.annotation.DbmInterceptorFilter.InterceptorType;
import org.onetwo.dbm.core.spi.DbmInterceptor;
import org.onetwo.dbm.core.spi.DbmInterceptorChain;

import brave.ScopedSpan;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContext;

/**
 * 
 * @author wayshall
 * <br/>
 */
@DbmInterceptorFilter(type=InterceptorType.JDBC)
public class SleuthDbmInterceptor implements DbmInterceptor {
	public static final String SPAN_NAME = "inner-dbm-jdbc";
	/*@Autowired
	private Tracer tracer;*/
	/***
	 * seee SleuthHystrixConcurrencyStrategy
	 * 
	 */
	private Tracer tracer;
	private String spanName = SPAN_NAME;
	private final TraceContext parent;
	
	public SleuthDbmInterceptor(Tracing tracing) {
		super();
		this.tracer = tracing.tracer();
		this.parent = tracing.currentTraceContext().get();
//		this.spanName = name != null ? name : spanNamer.name(delegate, DEFAULT_SPAN_NAME);
	}



	/****
	 * 升级详情见：https://github.com/spring-cloud/spring-cloud-sleuth/wiki/Spring-Cloud-Sleuth-2.0-Migration-Guide#core
	 */
	@Override
	public Object intercept(DbmInterceptorChain chain) {
		ScopedSpan span = this.tracer.startScopedSpanWithParent(spanName, this.parent);
		try {
			return chain.invoke();
		} catch (Exception | Error e) {
			span.error(e);
			throw e;
		} finally {
			span.finish();
		}
	}
	/*public Object intercept(DbmInterceptorChain chain) {
		final Span span = tracer.nextSpan().name(SPAN_NAME);
		try (SpanInScope ws = tracer.withSpanInScope(span.start())) {
//			span.tag(Span.SPAN_LOCAL_COMPONENT_TAG_NAME, SPAN_NAME);
//			span.logEvent(Span.CLIENT_SEND);
			return chain.invoke();
		} finally{
			span.finish();
		}
	}*/
	
	/*public Object intercept(DbmInterceptorChain chain) {
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
	}*/

}

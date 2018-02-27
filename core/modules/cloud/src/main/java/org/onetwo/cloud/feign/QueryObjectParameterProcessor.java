package org.onetwo.cloud.feign;

import java.lang.annotation.Annotation;

import org.onetwo.cloud.feign.annotation.QueryObject;
import org.springframework.cloud.netflix.feign.AnnotatedParameterProcessor;

/**
 * @author wayshall
 * <br/>
 */
public class QueryObjectParameterProcessor implements AnnotatedParameterProcessor {

    private static final Class<QueryObject> ANNOTATION = QueryObject.class;
    
	@Override
	public Class<? extends Annotation> getAnnotationType() {
		return ANNOTATION;
	}

	@Override
	public boolean processArgument(AnnotatedParameterContext context, Annotation annotation) {
		return false;
	}

}

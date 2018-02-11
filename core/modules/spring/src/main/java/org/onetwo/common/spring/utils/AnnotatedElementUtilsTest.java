package org.onetwo.common.spring.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.onetwo.common.apiclient.annotation.RestApiClient;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author wayshall
 * <br/>
 */
public class AnnotatedElementUtilsTest {
	
	@Test
	public void test(){
		//RestApiClient需要加@Inherited
		AnnotationAttributes annoAttr = AnnotatedElementUtils.getMergedAnnotationAttributes(SubTestClass.class, RestApiClient.class);
		assertThat(annoAttr).isNull();

		//不需要加@Inherited
		RestApiClient anno = AnnotationUtils.findAnnotation(SubTestClass.class, RestApiClient.class);
		assertThat(anno).isNotNull();
	}
	
	@RestApiClient
	public static class ParentTestClass {
		
	}
	public static class SubTestClass extends ParentTestClass {
		
	}

}

package org.onetwo.common.spring.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
		

		anno = AnnotationUtils.findAnnotation(SubTestClass2.class, RestApiClient.class);
		assertThat(anno).isNotNull();
		assertThat(anno.name()).isEqualTo("RestApiClientTestContainerName");
		assertThat(anno.url()).isEqualTo("http://RestApiClientTestContainer");
	}
	
	@RestApiClient
	public static class ParentTestClass {
	}
	
	public static class SubTestClass extends ParentTestClass {
	}
	
	@RestApiClientTestContainer
	public static class SubTestClass2 extends ParentTestClass {
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@RestApiClient(name="RestApiClientTestContainerName", url="http://RestApiClientTestContainer")
	static public @interface RestApiClientTestContainer {
	}

}

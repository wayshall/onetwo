package org.onetwo.boot.core.web.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptorAdapter;

/**
 * @author wayshall
 * <br/>
 */
public class TestInterceptors {

	@Interceptor(ParentMvcInterceptor.class)
	public static class InterceptableParent {
		@Interceptor(ParentMethodMvcInterceptor.class)
		public void test1(){
		}
	}

	public static class InterceptableChild1 extends InterceptableParent {
		public void test1(){
		}
	}

	@Interceptor(Child2MvcInterceptor.class)
	public static class InterceptableChild2 extends InterceptableParent {
		public void test1(){
		}
	}

	public static class InterceptableChild3 extends InterceptableParent {
		@Interceptor(Child3MvcInterceptor.class)
		public void test1(){
		}
	}
	
	public static class InterceptableParentNoAnno {
		public void test1(){
		}
	}
	
	@CombinationMVCInterceptor
	public static class InterceptableCombine1 extends InterceptableParent {
		public void test1(){
		}
	}

	@Interceptor(Child2MvcInterceptor.class)
	public static class InterceptableCombine2 extends InterceptableParent {
		@CombinationMVCInterceptor
		public void test1(){
		}
	}

	public static class ParentMvcInterceptor extends MvcInterceptorAdapter{
	}
	public static class ParentMethodMvcInterceptor extends MvcInterceptorAdapter{
	}
	public static class Child2MvcInterceptor extends MvcInterceptorAdapter{
	}
	public static class Child3MvcInterceptor extends MvcInterceptorAdapter{
	}
	public static class Combine1MvcInterceptor extends MvcInterceptorAdapter{
	}
	public static class Combine2MvcInterceptor extends MvcInterceptorAdapter{
	}
	public static class Combine2MethodMvcInterceptor extends MvcInterceptorAdapter{
	}
	

	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@Inherited
	@Interceptor(Combine1MvcInterceptor.class)
	@Interceptor(Combine2MvcInterceptor.class)
	public static @interface CombinationMVCInterceptor {
		
	}
}

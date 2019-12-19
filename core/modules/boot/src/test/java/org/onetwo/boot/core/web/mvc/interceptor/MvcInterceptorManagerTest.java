package org.onetwo.boot.core.web.mvc.interceptor;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.onetwo.boot.core.jwt.JwtMvcInterceptor;
import org.onetwo.boot.core.jwt.annotation.JwtUserAuth;
import org.onetwo.boot.core.web.api.WebApi;
import org.onetwo.boot.core.web.mvc.annotation.Interceptor;
import org.onetwo.boot.core.web.mvc.annotation.InterceptorDisabled;
import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptorManager.MvcInterceptorMeta;
import org.onetwo.boot.core.web.view.XResponseView;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.annotation.Property;
import org.onetwo.common.utils.LangUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.web.method.HandlerMethod;

/**
 * @author wayshall
 * <br/>
 */
public class MvcInterceptorManagerTest {
	
	MvcInterceptorManager mvcInterceptorMgr = new MvcInterceptorManager();
	
	/***
	 * 方法没有拦截器配置时，查找类上的拦截器配置
	 * @author wayshall
	 */
	@Test
	public void testBaseApiController(){
		BaseApiControllerTest base = new BaseApiControllerTest();
		Method withoutUserAuthMethod = ReflectUtils.findMethod(BaseApiControllerTest.class, "withoutUserAuth");
		HandlerMethod hm = new HandlerMethod(base, withoutUserAuthMethod);
		Collection<AnnotationAttributes> attrs = mvcInterceptorMgr.findInterceptorAnnotationAttrsList(hm);
		assertThat(attrs).size().isEqualTo(1);
		AnnotationAttributes attr = LangUtils.getFirst(attrs);
		MvcInterceptorMeta meta = mvcInterceptorMgr.asMvcInterceptorMeta(attr);
		assertThat(meta.getInterceptorType()).isEqualTo(ClientDetailsMvcInterceptorTest.class);
	}
	
	/***
	 * 方法没有拦截器配置时，查找类和父类上的拦截器配置
	 * @author wayshall
	 */
	@Test
	public void testSubControllerWithoutUserAuthMethod(){
		SubControllerTest sub = new SubControllerTest();
		Method withoutUserAuthMethod = ReflectUtils.findMethod(SubControllerTest.class, "withoutUserAuth");
		HandlerMethod hm = new HandlerMethod(sub, withoutUserAuthMethod);
		List<AnnotationAttributes> attrs = new ArrayList<AnnotationAttributes>(mvcInterceptorMgr.findInterceptorAnnotationAttrsList(hm));
		assertThat(attrs).size().isEqualTo(2);
		//父类的@TenentClientAuthTest
		boolean match = attrs.stream().anyMatch(attr->mvcInterceptorMgr.asMvcInterceptorMeta(attr).getInterceptorType()==ClientDetailsMvcInterceptorTest.class);
		assertThat(match).isTrue();
		//自身的@AnonymousOrUserAuthTest
		match = attrs.stream().anyMatch(attr->mvcInterceptorMgr.asMvcInterceptorMeta(attr).getInterceptorType()==AnonymousOrUserJwtMvcInterceptor.class);
		assertThat(match).isTrue();
	}
	/***
	 * 方法有拦截器时，只查找方法的拦截器
	 * @author wayshall
	 */
	@Test
	public void testSubControllerUserAuth(){
		SubControllerTest sub = new SubControllerTest();
		Method userAuth = ReflectUtils.findMethod(SubControllerTest.class, "userAuth");
		HandlerMethod hm = new HandlerMethod(sub, userAuth);
		List<AnnotationAttributes> attrs = new ArrayList<AnnotationAttributes>(mvcInterceptorMgr.findInterceptorAnnotationAttrsList(hm));
		assertThat(attrs).size().isEqualTo(1);
		AnnotationAttributes attr = attrs.get(0);
		MvcInterceptorMeta meta = mvcInterceptorMgr.asMvcInterceptorMeta(attr);
		assertThat(meta.getInterceptorType()).isEqualTo(JwtMvcInterceptor.class);
	}
	
	/****
	 * 方法有拦截器时，只查找方法的拦截器
	 * @author wayshall
	 */
	@Test
	public void testSubControllerUserAuth2(){
		SubControllerTest sub = new SubControllerTest();
		Method userAuth = ReflectUtils.findMethod(SubControllerTest.class, "userAuth2");
		HandlerMethod hm = new HandlerMethod(sub, userAuth);
		List<AnnotationAttributes> attrs = new ArrayList<AnnotationAttributes>(mvcInterceptorMgr.findInterceptorAnnotationAttrsList(hm));
		
		//返回方法级别的两个拦截器
		assertThat(attrs).size().isEqualTo(2);
		boolean match = attrs.stream().anyMatch(attr->mvcInterceptorMgr.asMvcInterceptorMeta(attr).getInterceptorType()==JwtMvcInterceptor.class);
		assertThat(match).isTrue();

		match = attrs.stream().anyMatch(attr->mvcInterceptorMgr.asMvcInterceptorMeta(attr).getInterceptorType()==ClientDetailsMvcInterceptorTest.class);
		assertThat(match).isTrue();
	}
	
	/***
	 * //检测到有disable拦截器，直接返回空
	 * @author wayshall
	 */
	@Test
	public void testSubControllerUserAuthDisabled(){
		SubControllerTest sub = new SubControllerTest();
		Method userAuthDisabled = ReflectUtils.findMethod(SubControllerTest.class, "userAuthDisabled");
		HandlerMethod hm = new HandlerMethod(sub, userAuthDisabled);
		
		List<AnnotationAttributes> attrs = new ArrayList<AnnotationAttributes>(mvcInterceptorMgr.findInterceptorAnnotationAttrsList(hm));
		assertThat(attrs).isEmpty();
		/*assertThat(attrs).size().isEqualTo(1);
		boolean match = attrs.stream().anyMatch(attr->mvcInterceptorMgr.asMvcInterceptorMeta(attr).getInterceptorType()==DisableMvcInterceptor.class);
		assertThat(match).isTrue();*/
		
	}
	
	/**
	 * 方法级别没有配置，检测到类级别，类级别有@InterceptorDisabled
	 * 所有拦截器失效
	 * @author wayshall
	 */
	@Test
	public void testSub2ControllerUserAuthDisabled(){
		Sub2ControllerTest sub = new Sub2ControllerTest();
		Method userAuthDisabled = ReflectUtils.findMethod(Sub2ControllerTest.class, "userAuth");
		HandlerMethod hm = new HandlerMethod(sub, userAuthDisabled);
		
		List<AnnotationAttributes> attrs = new ArrayList<AnnotationAttributes>(mvcInterceptorMgr.findInterceptorAnnotationAttrsList(hm));
		assertThat(attrs).isEmpty();
	}
	@Test
	public void testSub3ControllerUserAuthDisabled(){
		Sub3ControllerTest sub = new Sub3ControllerTest();
		Method userAuthDisabled = ReflectUtils.findMethod(Sub3ControllerTest.class, "userAuth");
		HandlerMethod hm = new HandlerMethod(sub, userAuthDisabled);
		
		List<AnnotationAttributes> attrs = new ArrayList<AnnotationAttributes>(mvcInterceptorMgr.findInterceptorAnnotationAttrsList(hm));
		assertThat(attrs).isEmpty();
	}
	

	@Test
	public void testSub2ControllerUserAuth2(){
		Sub2ControllerTest sub = new Sub2ControllerTest();
		Method userAuth = ReflectUtils.findMethod(Sub2ControllerTest.class, "userAuth2");
		HandlerMethod hm = new HandlerMethod(sub, userAuth);
		List<AnnotationAttributes> attrs = new ArrayList<AnnotationAttributes>(mvcInterceptorMgr.findInterceptorAnnotationAttrsList(hm));
		
		//返回方法级别的两个拦截器
		assertThat(attrs).size().isEqualTo(2);
		boolean match = attrs.stream().anyMatch(attr->mvcInterceptorMgr.asMvcInterceptorMeta(attr).getInterceptorType()==JwtMvcInterceptor.class);
		assertThat(match).isTrue();

		match = attrs.stream().anyMatch(attr->mvcInterceptorMgr.asMvcInterceptorMeta(attr).getInterceptorType()==ClientDetailsMvcInterceptorTest.class);
		assertThat(match).isTrue();
	}

	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@Inherited
	@Interceptor(value=ClientDetailsMvcInterceptorTest.class)
	static @interface TenentClientAuthTest {
	}
	
	public static class ClientDetailsMvcInterceptorTest extends MvcInterceptorAdapter {
	}
	
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@Inherited
	@Interceptor(value=AnonymousOrUserJwtMvcInterceptor.class,
		properties={
			@Property(name="canBeAnonymous", value="true")
		}
	)
	static @interface AnonymousOrUserAuthTest {
		
		/*public static class AnonymousOrUserAuthInterceptor extends JwtMvcInterceptor {
		}*/
	}
	
	static class AnonymousOrUserJwtMvcInterceptor extends JwtMvcInterceptor {
	}
	
	@XResponseView
	@WebApi
	@TenentClientAuthTest
	static class BaseApiControllerTest  {
		public void withoutUserAuth(){
		}
	}

	@AnonymousOrUserAuthTest
	static class SubControllerTest extends BaseApiControllerTest {
		@JwtUserAuth
		public void userAuth(){
		}
		
		@JwtUserAuth
		@TenentClientAuthTest
		public void userAuth2(){
		}

		@JwtUserAuth
		@InterceptorDisabled
		public void userAuthDisabled(){
		}
		
		public void withoutUserAuth(){
		}
	}


	@AnonymousOrUserAuthTest
	@InterceptorDisabled
	static class Sub2ControllerTest extends SubControllerTest {
		
		/***
		 * 方法级别没有配置，检测到类级别，类级别有@InterceptorDisabled
		 * 所有拦截器失效
		 */
		public void userAuth(){
		}
		
		/***
		 * 生效
		 */
		@JwtUserAuth
		@TenentClientAuthTest
		public void userAuth2(){
		}
	}

	static class Sub3ControllerTest extends Sub2ControllerTest {
		
		/***
		 * 方法级别没有配置，检测到类级别，类级别有@InterceptorDisabled
		 * 所有拦截器失效
		 */
		public void userAuth(){
		}
	}
}

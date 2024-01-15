package org.onetwo.common.spring.utils;

import java.util.Date;
import java.util.HashSet;

import javax.script.ScriptException;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.entity.UserEntity;
import org.onetwo.common.spring.utils.JavaScriptInvoker.ScriptEnginer;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;
import org.openjdk.nashorn.internal.objects.NativeDate;

public class JavaScriptInvokerTest {
	
	private JavaScriptInvoker javaScriptInvoker = new JavaScriptInvoker();
	
	
	@Test
	public void testScript(){
		UserEntity user = new UserEntity();
		user.setId(2L);
		Object res = this.javaScriptInvoker.eval("user.id", "user", user);
		Assert.assertEquals(user.getId(), res);
		res = this.javaScriptInvoker.eval("user.id;true", "user", user);
		Assert.assertEquals(true, res);
	}

	@Test
	public void testReturn(){
		Assertions.assertThatExceptionOfType(RuntimeException.class)
					.isThrownBy(()->{
						ScriptEnginer enginer = this.javaScriptInvoker.createScriptEnginer()
								.evalClassPathFile("org/onetwo/common/spring/utils/test_return.js");
						enginer.invokeFunc("funcAfterReturn", new JsContext());
					})
					.withMessage("eval javascript error:%s", "org/onetwo/common/spring/utils/test_return.js")
					;
	}
	
	@Test
	public void testContext(){
		UserEntity user = new UserEntity();
		user.setId(100L);
		
		ScriptEnginer enginer = this.javaScriptInvoker.createScriptEnginer();
		boolean res = enginer
							.evalClassPathFile("org/onetwo/common/spring/utils/test.js")
//								.compile("var validate = function(context){ if(context.ruleExt.arg1==='testArgs'){ return ture; }return false;}")
							.invokeFunc("validate", user);
//		System.out.println("res:"+res);
		Assert.assertEquals(true, res);

		user.setId(200L);
		res = enginer.invokeFunc("validate2", user);
		Assert.assertEquals(false, res);
		
		try {
			enginer.invokeFunc("throwErrorFunc", new JsContext());
			Assert.fail();
		} catch (Exception e) {
			Assert.assertTrue(BaseException.class.isInstance(e.getCause()));
		}
		
		try {
			enginer.invokeFunc("throwErrorFunc");
			Assert.fail();
		} catch (Exception e) {
			Assert.assertTrue(ScriptException.class.isInstance(e.getCause()));
		}
		
		Object funcObj = enginer.eval("funcObj");
		String methodRes = enginer.invokeMethod(funcObj, "say", "hello world!");
		Assert.assertEquals("say:hello world!", methodRes);
		
		// must load("nashorn:mozilla_compat.js")
		HashSet<String> set = enginer.invokeFunc("newHashSet");
		Assert.assertTrue(set.contains("aa"));
		Assert.assertTrue(set.contains("bb"));
		
		Object date = enginer.invokeFunc("newDate");
		Assert.assertTrue(date instanceof ScriptObjectMirror);
		ScriptObjectMirror som = (ScriptObjectMirror) date;
		NativeDate nativeDate = som.to(NativeDate.class);
		date = enginer.invokeFunc("newJavaDate");
		Assert.assertTrue(date instanceof Date);
		res = enginer.invokeFunc("isSameDay");
		Assert.assertTrue(res);
		
	}
	
	public class JsContext {

		public void error(String msg){
			throw new BaseException(msg);
		}
	}
}

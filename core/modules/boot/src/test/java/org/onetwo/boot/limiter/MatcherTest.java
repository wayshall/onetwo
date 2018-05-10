package org.onetwo.boot.limiter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.onetwo.boot.limiter.InvokeContext.DefaultInvokeContext;

/**
 * @author wayshall
 * <br/>
 */
public class MatcherTest {
	private MatcherRegister matcherRegister = new MatcherRegister();
	
	@Test
	public void testAntpath(){
		String[] patterns = new String[]{"/uaa/**"};
		DefaultInvokeContext context = DefaultInvokeContext.builder().requestPath("/uaa/user/info/1").build();
		boolean match = matcherRegister.getRegistered("antpath").apply(patterns).matches(context);
		assertThat(match).isTrue();

		
		context = DefaultInvokeContext.builder().requestPath("/uaa").build();
		match = matcherRegister.getRegistered("antpath").apply(patterns).matches(context);
		assertThat(match).isTrue();
		
		
		context = DefaultInvokeContext.builder().requestPath("/order/1").build();
		match = matcherRegister.getRegistered("antpath").apply(patterns).matches(context);
		assertThat(match).isFalse();

		patterns = new String[]{"/uaa/**", "/order/**"};
		context = DefaultInvokeContext.builder().requestPath("/order/1").build();
		match = matcherRegister.getRegistered("antpath").apply(patterns).matches(context);
		assertThat(match).isTrue();
		context = DefaultInvokeContext.builder().requestPath("/uaa").build();
		match = matcherRegister.getRegistered("antpath").apply(patterns).matches(context);
		assertThat(match).isTrue();
		
		
	}
	
	@Test
	public void testRegexpath(){
		String[] patterns = new String[]{"^/uaa(/?|/.*)$"};
		DefaultInvokeContext context = DefaultInvokeContext.builder().requestPath("/uaa/user/info/1").build();
		boolean match = matcherRegister.getRegistered("regexpath").apply(patterns).matches(context);
		assertThat(match).isTrue();

		
		context = DefaultInvokeContext.builder().requestPath("/uaa").build();
		match = matcherRegister.getRegistered("regexpath").apply(patterns).matches(context);
		assertThat(match).isTrue();
		
		context = DefaultInvokeContext.builder().requestPath("/order/1").build();
		match = matcherRegister.getRegistered("regexpath").apply(patterns).matches(context);
		assertThat(match).isFalse();
	}
	
	@Test
	public void testContainerAnyOne(){
		String[] patterns = new String[]{"127.0.0.1", "192.168.1.1"};
		DefaultInvokeContext context = DefaultInvokeContext.builder().clientIp("127.0.0.1").build();
		boolean match = matcherRegister.getRegistered("ip").apply(patterns).matches(context);
		assertThat(match).isTrue();

		
		context = DefaultInvokeContext.builder().clientIp("192.168.1.1").build();
		match = matcherRegister.getRegistered("ip").apply(patterns).matches(context);
		assertThat(match).isTrue();
		
		context = DefaultInvokeContext.builder().requestPath("192.168.1.2").build();
		match = matcherRegister.getRegistered("ip").apply(patterns).matches(context);
		assertThat(match).isFalse();
	}

}

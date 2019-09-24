package org.onetwo.common.spring.match;
/**
 * @author weishao zeng
 * <br/>
 */

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;

public class RuleMatcherTest {
	
	@Test
	public void testContainer() {
		RuleMatcher matcher = new RuleMatcher();
		matcher.setValue(Arrays.asList("测试1", "测试2"));
		assertThat(matcher.match("我是测试1句子")).isTrue();
		assertThat(matcher.match("我是测试2句子")).isTrue();
		assertThat(matcher.match("我是测试句子")).isFalse();
		assertThat(matcher.match("我是测句子")).isFalse();
	}
	
	@Test
	public void testRegex() {
		RuleMatcher matcher = new RuleMatcher();
		matcher.setRule(MatchRules.REGEX);
		matcher.setValue(Arrays.asList(".+测试1.+", "^.*测试2.*$"));
		assertThat(matcher.match("我是测试1句子")).isTrue();
		assertThat(matcher.match("我是测试2句子")).isTrue();
		assertThat(matcher.match("测试1句子")).isFalse();
		assertThat(matcher.match("测试2句子")).isTrue();
		assertThat(matcher.match("我是测试句子")).isFalse();
		assertThat(matcher.match("我是测句子")).isFalse();
	}
	
	@Test
	public void testAnt() {
		RuleMatcher matcher = new RuleMatcher();
		matcher.setRule(MatchRules.ANT);
		matcher.setValue(Arrays.asList("测试1", "*测试2*"));
		assertThat(matcher.match("测试1")).isTrue();
		assertThat(matcher.match("我是测试1句子")).isFalse();
		assertThat(matcher.match("我是测试2句子")).isTrue();
		assertThat(matcher.match("测试1句子")).isFalse();
		assertThat(matcher.match("测试2句子")).isTrue();
		assertThat(matcher.match("我是测试句子")).isFalse();
		assertThat(matcher.match("我是测句子")).isFalse();
	}

}

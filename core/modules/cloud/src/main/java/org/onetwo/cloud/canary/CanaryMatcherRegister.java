package org.onetwo.cloud.canary;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.boot.limiter.Matcher;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.MapRegisterManager;
import org.springframework.util.AntPathMatcher;


/**
 * @author wayshall
 * <br/>
 */
public class CanaryMatcherRegister extends MapRegisterManager<String, PatternMatcherCreator> {
	public static final CanaryMatcherRegister INSTANCE = new CanaryMatcherRegister();
	
	/*private Cache<String, Matcher<CanaryContext>> matcherCachers = CacheBuilder.newBuilder()
																					.weakKeys()
																					.expireAfterWrite(10, TimeUnit.MINUTES)
																					.build();*/
	
	public CanaryMatcherRegister() {
		super();
		register("antpath", (patterns)->{
			return new AntpathMatcher(patterns);
		})
		.register("regexpath", (patterns)->{
			return new RegexpathMatcher(patterns);
		})
		.register("ip", (patterns)->{
			return new ContainAnyOneMatcher("clientIp", patterns);
		})
		.register("client-tag", (patterns)->{
			return new ContainAnyOneMatcher("clientTag", patterns);
		})
		/*.register("serviceId", (patterns)->{
			return new ContainAnyOneMatcher("serviceId", patterns);
		})*/
		;
	}
	
	/*public Matcher<CanaryContext> getOrCreateMatcher(String matcherName, String...patterns){
		return matcherCachers.get(matcherName, valueLoader);
	}*/
	
	public Matcher<CanaryContext> createMatcher(String matcherName, String...patterns){
		Assert.hasText(matcherName);
		Assert.notEmpty(patterns);
		Matcher<CanaryContext> matcher = findRegistered(matcherName).orElseThrow(()->new BaseException("matcher not found, name: " + matcherName))
													.apply(patterns);
		return matcher;
	}
	
	

	static abstract public class AbstractMathcer implements Matcher<CanaryContext> {
		final private String[] patterns;
		
		public AbstractMathcer(String[] patterns) {
			super();
			Assert.notEmpty(patterns);
			this.patterns = patterns;
		}
		@Override
		public boolean matches(CanaryContext context) {
			return Stream.of(getPatterns())
							.anyMatch(pattern->doMatches(pattern, context));
//			return antMatcher.match(getPatterns(), context.getRequestPath());
		}
		
		protected boolean doMatches(String pattern, CanaryContext context){
			throw new RuntimeException("operation not implement yet!");
		}
		
		public String[] getPatterns() {
			return patterns;
		}
	}
	
	public static class AntpathMatcher extends AbstractMathcer {
		static final private AntPathMatcher antMatcher = new AntPathMatcher();
		public AntpathMatcher(String... patterns) {
			super(patterns);
		}
		@Override
		protected boolean doMatches(String pattern, CanaryContext context) {
			return antMatcher.match(pattern, context.getRequestPath());
		}
	}
	
	public static class RegexpathMatcher extends AbstractMathcer {
		private List<Pattern> regexPatterns;
		public RegexpathMatcher(String... patterns) {
			super(patterns);
			regexPatterns = Stream.of(getPatterns())
									.map(regex->Pattern.compile(regex))
									.collect(Collectors.toList());
		}
		@Override
		public boolean matches(CanaryContext context) {
			return regexPatterns.stream()
								.anyMatch(pattern->pattern.matcher(context.getRequestPath()).matches());
		}
	}
	public static class ContainAnyOneMatcher extends AbstractMathcer {
		private String property;
		public ContainAnyOneMatcher(String property, String[] patterns) {
			super(patterns);
			this.property = property;
		}
		@Override
		public boolean matches(CanaryContext context) {
			String value = (String)ReflectUtils.getPropertyValue(context, property);
			return ArrayUtils.contains(getPatterns(), value);
		}
	}

}

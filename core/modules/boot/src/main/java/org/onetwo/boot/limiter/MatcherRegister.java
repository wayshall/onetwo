package org.onetwo.boot.limiter;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.MapRegisterManager;
import org.springframework.util.AntPathMatcher;


/**
 * @author wayshall
 * <br/>
 */
public class MatcherRegister extends MapRegisterManager<String, Function<String[], Matcher<InvokeContext>>> {
	public static final MatcherRegister INSTANCE = new MatcherRegister();

	public MatcherRegister() {
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
		.register("serviceId", (patterns)->{
			return new ContainAnyOneMatcher("serviceId", patterns);
		})
		;
	}
	
	public Matcher<InvokeContext> createMatcher(String matcherName, String...patterns){
		Assert.hasText(matcherName);
		Assert.notEmpty(patterns);
		Matcher<InvokeContext> matcher = findRegistered(matcherName).orElseThrow(()->new BaseException("matcher not found, name: " + matcherName))
													.apply(patterns);
		return matcher;
	}

	static abstract public class AbstractMathcer implements Matcher<InvokeContext> {
		final private String[] patterns;
		
		public AbstractMathcer(String[] patterns) {
			super();
			Assert.notEmpty(patterns);
			this.patterns = patterns;
		}
		@Override
		public boolean matches(InvokeContext context) {
			return Stream.of(getPatterns())
							.anyMatch(pattern->doMatches(pattern, context));
//			return antMatcher.match(getPatterns(), context.getRequestPath());
		}
		
		protected boolean doMatches(String pattern, InvokeContext context){
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
		protected boolean doMatches(String pattern, InvokeContext context) {
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
		public boolean matches(InvokeContext context) {
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
		public boolean matches(InvokeContext context) {
			String value = (String)ReflectUtils.getPropertyValue(context, property);
			return ArrayUtils.contains(getPatterns(), value);
		}
	}

}

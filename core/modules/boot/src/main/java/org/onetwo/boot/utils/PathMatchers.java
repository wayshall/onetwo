package org.onetwo.boot.utils;

import java.util.Set;

import org.springframework.util.AntPathMatcher;

import com.google.common.collect.Sets;

final public class PathMatchers {
//	private static final Logger logger = JFishLoggerFactory.getLogger(AntPathMatchers.class);

	public static PathMatcher allPaths(String... paths){
		return new AllPathMatcher(paths);
	}
	public static PathMatcher anyPaths(String... paths){
		return new AnyPathMatcher(paths);
	}

	public static interface PathMatcher {
		public boolean match(String path);
	}
	
	public static class AllPathMatcher implements PathMatcher {
		final protected AntPathMatcher matcher= new AntPathMatcher();
		final protected Set<String> patterns;
		public AllPathMatcher(String... patterns) {
			super();
			this.patterns = Sets.newHashSet(patterns);
		}
		@Override
		public boolean match(String path) {
			return patterns.stream().allMatch(pattern->matcher.match(pattern, path));
		}
	}
	
	public static class AnyPathMatcher extends AllPathMatcher {
		public AnyPathMatcher(String... patterns) {
			super(patterns);
		}

		@Override
		public boolean match(String path) {
			return patterns.stream().anyMatch(pattern->matcher.match(pattern, path));
		}
	}
}

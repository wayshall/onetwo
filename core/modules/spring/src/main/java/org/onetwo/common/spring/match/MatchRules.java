package org.onetwo.common.spring.match;

import java.util.regex.Pattern;

/**
 * @author weishao zeng
 * <br/>
 */
public enum MatchRules implements MatchRule {
	
	CONTAINS {
		public boolean match(String text, String matchValue) {
			return text.contains(matchValue);
		}
	},
	ANT{
		public boolean match(String text, String matchValue) {
			return MatchUtils.ANT_MATCHER.match(matchValue, text);
		}
	},
	REGEX{
		public boolean match(String text, String matchValue) {
			boolean match = Pattern.matches(matchValue, text);
			return match;
		}
	}
}

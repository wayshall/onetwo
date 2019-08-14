package org.onetwo.common.spring.match;

import java.util.List;

import org.onetwo.common.utils.LangUtils;

/**
 * @author weishao zeng
 * <br/>
 */
public class RuleMatcher {
	private List<String> value;
	private MatchRules rule = MatchRules.CONTAINS;
	
	public boolean match(String text) {
		if (LangUtils.isEmpty(value)) {
			return false;
		}
		return value.stream().anyMatch(v -> {
			return rule.match(text, v);
		});
	}

	public void setValue(List<String> value) {
		this.value = value;
	}

	public void setRule(MatchRules rule) {
		this.rule = rule;
	}
	
}

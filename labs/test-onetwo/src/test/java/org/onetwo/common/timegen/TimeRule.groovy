package org.onetwo.common.timegen;


public class TimeRule {
	
	/***
	 * PERIOD_WEEK：每周，比如某年月日到某年月日的每周一到周二
PERIOD_MONTH：每月，比如某年月日到某年月日的每月1号到2号
EXACT_DATE：指定具体起止日期
EXPRESSION：表达式
	 * @author way
	 *
	 */
	public static enum RuleType {
		PERIOD_WEEK,
		PERIOD_MONTH,
		EXACT_DATE,
		EXPRESSION
	}
	
	RuleType ruleType;
	String expression;
	int startIndex;
	int endIndex;
	Date startTime;
	Date endTime;
	
	
}

package org.onetwo.common.date.timegen;

import java.util.Date;

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
		PERIOD_WEEK("每周"),
		PERIOD_MONTH("每月"),
		EXACT_DATE("具体日期"),
		EXPRESSION("表达式");
		

		private final String label;

		private RuleType(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
		
	}
	
	private RuleType ruleType;
	private String expression;
	private int startIndex;
	private int endIndex;
	private Date startTime;
	private Date endTime;
	public RuleType getRuleType() {
		return ruleType;
	}
	public void setRuleType(RuleType ruleType) {
		this.ruleType = ruleType;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
}

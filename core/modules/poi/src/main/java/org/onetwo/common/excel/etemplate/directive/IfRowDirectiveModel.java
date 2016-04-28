package org.onetwo.common.excel.etemplate.directive;

import org.onetwo.common.excel.etemplate.AbstractDirectiveModel;

public class IfRowDirectiveModel extends AbstractDirectiveModel {

	final private String condition;
	public IfRowDirectiveModel(String directive, String condition) {
		super(directive);
		this.condition = condition;
	}
	public String getCondition() {
		return condition;
	}

}

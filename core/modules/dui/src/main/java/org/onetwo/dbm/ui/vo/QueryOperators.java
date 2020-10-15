package org.onetwo.dbm.ui.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

/**
 * @author weishao zeng
 * <br/>
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum QueryOperators {
	EQUAL("等于", "=");
	
	@Getter
	final String label;
	@Getter
	final String operator;
	private QueryOperators(String label, String operator) {
		this.label = label;
		this.operator = operator;
	}
}

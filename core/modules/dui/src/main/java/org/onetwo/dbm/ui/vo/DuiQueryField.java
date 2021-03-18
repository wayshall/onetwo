package org.onetwo.dbm.ui.vo;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
public class DuiQueryField {

	String name;
	QueryOperators operator;
	Object value;
	
}

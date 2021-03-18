package org.onetwo.dbm.ui.vo;
/**
 * @author weishao zeng
 * <br/>
 */

import java.util.List;

import com.google.common.collect.Lists;

import lombok.Data;

@Data
public class SearchableFieldVO {
	
	String label;
	String name;
	
	List<QueryOperators> operators = Lists.newArrayList();
	
	public SearchableFieldVO addOperator(QueryOperators op) {
		operators.add(op);
		return this;
	}

}

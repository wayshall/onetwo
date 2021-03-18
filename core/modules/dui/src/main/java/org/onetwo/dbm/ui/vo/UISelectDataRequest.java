package org.onetwo.dbm.ui.vo;

import javax.validation.constraints.NotNull;

import org.onetwo.common.utils.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UISelectDataRequest {
	public static final String INIT_QUERY = "__init__";
	
	@NotNull
	String entity;
	@NotNull
	String field; 
	String query;
	String selectedValue;
	
	public boolean isInitQuery() {
		return isRequestInitQuery(query);
	}
	
	public static boolean isRequestInitQuery(String query) {
		return StringUtils.isBlank(query) || INIT_QUERY.equals(query);
	}
	
}

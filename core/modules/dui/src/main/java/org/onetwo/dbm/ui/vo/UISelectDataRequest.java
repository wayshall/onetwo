package org.onetwo.dbm.ui.vo;

import javax.validation.constraints.NotNull;

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
	@NotNull
	String entity;
	@NotNull
	String field; 
	String query; 
	
}

package org.onetwo.dbm.ui.meta;

import org.onetwo.dbm.ui.annotation.DUITreeGrid.TreeStyles;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
public class DUITreeGridMeta {

	DUIFieldMeta parentField;
	String rootId;
	
	TreeStyles style;
	
	Class<?> cascadeEntity;
	DUIEntityMeta cascadeEntityMeta;
	
	public boolean isCascadeOnRightStyle() {
		return style!=null && style==TreeStyles.CASCADE_ON_RIGHT;
	}
	
	public boolean hasCascadeEntity() {
		return cascadeEntity!=null;
	}
	
}

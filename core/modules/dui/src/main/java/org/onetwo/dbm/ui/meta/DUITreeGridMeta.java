package org.onetwo.dbm.ui.meta;

import org.onetwo.common.utils.StringUtils;
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

	DUIEntityMeta ownerEntityMeta;
	
	Class<?> cascadeEntity;
	/***
	 * 树表格级连的实体
	 */
	DUIEntityMeta cascadeEntityMeta;
	
	String cascadeField;
	
	public DUITreeGridMeta(DUIEntityMeta ownerEntityMeta) {
		super();
		this.ownerEntityMeta = ownerEntityMeta;
	}
	
	public String getCascadeFieldBarName() {
		return StringUtils.convertWithSeperator(cascadeField, "-");
	}
	
	public boolean isCascadeOnRightStyle() {
		return style!=null && style==TreeStyles.CASCADE_ON_RIGHT;
	}
	
	public boolean hasCascadeEntity() {
		return cascadeEntity!=null;
	}
	
	public void setCascadeEntityMeta(DUIEntityMeta cascadeEntityMeta) {
		this.cascadeEntityMeta = cascadeEntityMeta;
		this.cascadeEntityMeta.setTreeParent(ownerEntityMeta);
	}
	
}

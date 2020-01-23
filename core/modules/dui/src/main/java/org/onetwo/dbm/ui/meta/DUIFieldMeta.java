package org.onetwo.dbm.ui.meta;

import org.onetwo.common.db.generator.meta.ColumnMeta;
import org.onetwo.dbm.mapping.DbmMappedField;
import org.onetwo.dbm.ui.annotation.DUIInput.InputTypes;
import org.onetwo.dbm.ui.annotation.DUIInput.NullDUIJsonValueWriter;
import org.onetwo.dbm.ui.annotation.DUISelect.NoEnums;
import org.onetwo.dbm.ui.annotation.DUISelect.NoProvider;
import org.onetwo.dbm.ui.core.UISelectDataProvider;
import org.onetwo.dbm.ui.spi.DUIJsonValueWriter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
@AllArgsConstructor
@Builder
public class DUIFieldMeta {
	
	DUIEntityMeta classMeta;
	DbmMappedField dbmField;
	ColumnMeta column;
	
	String name;
	String label;
	/***
	 * 列表时显示的字段
	 * 如果枚举时应显示对应的label
	 */
	String listField;
	boolean listable;
	boolean insertable;
	boolean updatable;
	boolean searchable;
	int order;
	
	DUISelectMeta select;
	DUIInputMeta input;
	
	public String getFormDisabledValue() {
		if (insertable && updatable) {
			return "false";
		} else if (insertable) {
			return "statusMode!=='Add'";
		} else if (updatable) {
			return "statusMode!=='Edit'";
		} else {
			return "true";
		}
	}

	@Data
	public class DUIInputMeta {
		InputTypes type;
		
		Class<? extends DUIJsonValueWriter> valueWriter;
		
		public String getTypeName() {
			return type.name().toLowerCase();
		}
		
		public boolean hasValueWriter() {
			return valueWriter!=null && valueWriter!=NullDUIJsonValueWriter.class;
		}
	}

	@Data
	public class DUISelectMeta {
		Class<? extends Enum<?>> dataEnumClass;
		Class<? extends UISelectDataProvider> dataProvider;
		
		Class<?> cascadeEntity;
		String[] cascadeQueryFields;
		
		String labelField = "label";
		String valueField = "value";
		
		public boolean useEnumData() {
			return dataEnumClass!=null && dataEnumClass!=NoEnums.class;
		}
		
		public boolean useDataProvider() {
			return dataProvider!=null && dataProvider!=NoProvider.class;
		}
		
		public DUIFieldMeta getField() {
			return DUIFieldMeta.this;
		}
	}

}

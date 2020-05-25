package org.onetwo.dbm.ui.meta;

import org.onetwo.common.db.generator.meta.ColumnMeta;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.dbm.mapping.DbmMappedField;
import org.onetwo.dbm.ui.annotation.DUIInput.InputTypes;
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
	
	boolean notnull;
	/***
	 * 列表时显示的字段
	 * 如果枚举时应显示对应的label
	 */
	String listField;
//	Class<? extends DUIJsonValueWriter<?>> listValueWriter;
	
	boolean listable;
	boolean insertable;
	boolean updatable;
	boolean searchable;
	int order;
	
	String defaultValue;
	
	DUISelectMeta select;
	DUIInputMeta input;
	
	
	public boolean isIdField() {
		return this.column!=null && this.column.isPrimaryKey();
	}
	
	/***
	 * 横杠分割的名称
	 * @author weishao zeng
	 * @return
	 */
	public String getHorizontalBarName(){
		return StringUtils.convertWithSeperator(getName(), "-");
	}
	
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
		
		Class<? extends DUIJsonValueWriter<?>> valueWriter;
		
		public String getTypeName() {
			return type.name();
		}
		
		public boolean hasValueWriter() {
			return DUIEntityMeta.hasValueWriter(valueWriter);
		}
		
		public boolean isFileType() {
			return type!=null && type==InputTypes.FILE;
		}
		
		public boolean isDateType() {
			return type!=null && (type==InputTypes.DATE || type==InputTypes.DATE_TIME || type==InputTypes.TIME);
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
		
		boolean treeSelect;
		
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

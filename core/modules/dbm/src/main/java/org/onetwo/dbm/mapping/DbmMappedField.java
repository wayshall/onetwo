package org.onetwo.dbm.mapping;

import java.util.List;

import org.onetwo.common.utils.JFishProperty;
import org.onetwo.dbm.dialet.AbstractDBDialect.StrategyType;
import org.onetwo.dbm.event.DbmEntityFieldListener;
import org.onetwo.dbm.event.DbmEventAction;
import org.onetwo.dbm.mapping.version.VersionableType;

public interface DbmMappedField {

	public void setValue(Object entity, Object value);

	public Object getValue(Object entity);

//	public void setValueFromJdbc(Object entity, Object value);

//	public Object getValueForJdbc(Object entity);
	
	public List<DbmEntityFieldListener> getFieldListeners();

	public boolean isIdentify();

	public void setIdentify(boolean identify);

	public BaseColumnInfo getColumn();

	public void setColumn(BaseColumnInfo column);

	public String getName();

	public JFishMappedEntry getEntry();

	public String getLabel();

	public void setLabel(String label);

	/********
	 * 自动生成值是否需要在插入之前fetch数据，一般就是oracle序列
	 * @return
	 */
	public boolean isGeneratedValueFetchBeforeInsert();

	public boolean isGeneratedValue();

	public boolean isSeqStrategy();

	public boolean isIncreaseIdStrategy();

	public StrategyType getStrategyType();

	public void setStrategyType(StrategyType strategyType);

	public JFishProperty getPropertyInfo();
	
	/***
	 * 获取实际映射到类型
	 * @return
	 */
	public Class<?> getColumnType();

	public void freezing();

	public void checkFreezing(String name);

	public boolean isFreezing();

	public DbmMappedFieldType getMappedFieldType();

	public void setMappedFieldType(DbmMappedFieldType mappedFieldType);

	public boolean isJoinTableField();

//	public DataHolder<String, Object> getDataHolder();
	
//	public SqlParameterValue getValueForJdbcAndFireDbmEventAction(Object entity, JFishEventAction eventAction);
	/***
	 * 
	 * @param fieldValue
	 * @param eventAction
	 * @return newFieldValue
	 */
	public Object fireDbmEntityFieldEvents(Object fieldValue, DbmEventAction eventAction);

	public boolean isVersionControll();
	public <T> VersionableType<T> getVersionableType();
	public void setVersionableType(VersionableType<?> versionableType);
	
	public boolean isEnumerated();
	public DbmEnumType getEnumType();
}
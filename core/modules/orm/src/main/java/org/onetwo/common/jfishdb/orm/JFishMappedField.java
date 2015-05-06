package org.onetwo.common.jfishdb.orm;

import java.util.List;

import org.onetwo.common.jfishdb.dialet.AbstractDBDialect.StrategyType;
import org.onetwo.common.jfishdb.event.JFishEntityFieldListener;
import org.onetwo.common.jfishdb.event.JFishEventAction;
import org.onetwo.common.jfishdb.orm.version.VersionableType;
import org.onetwo.common.utils.JFishProperty;

public interface JFishMappedField {

	public void setValue(Object entity, Object value);

	public Object getValue(Object entity);

	public void setColumnValue(Object entity, Object value);

	public Object getColumnValue(Object entity);
	
	public List<JFishEntityFieldListener> getFieldListeners();

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
	
	public Class<?> getColumnType();

	public void freezing();

	public void checkFreezing(String name);

	public boolean isFreezing();

	public JFishMappedFieldType getMappedFieldType();

	public void setMappedFieldType(JFishMappedFieldType mappedFieldType);

	public boolean isJoinTableField();

	public DataHolder<String, Object> getDataHolder();
	
	public Object getColumnValueWithJFishEventAction(Object entity, JFishEventAction eventAction);

	public boolean isVersionControll();
	public <T> VersionableType<T> getVersionableType();
	public void setVersionableType(VersionableType<?> versionableType);
}
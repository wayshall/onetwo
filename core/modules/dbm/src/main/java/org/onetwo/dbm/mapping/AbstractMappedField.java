package org.onetwo.dbm.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.onetwo.common.utils.JFishProperty;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.dbm.annotation.DbmFieldListeners;
import org.onetwo.dbm.dialet.AbstractDBDialect.StrategyType;
import org.onetwo.dbm.event.DbmEntityFieldListener;
import org.onetwo.dbm.event.JFishEventAction;
import org.onetwo.dbm.exception.DbmException;
import org.onetwo.dbm.mapping.version.VersionableType;
import org.onetwo.dbm.utils.DbmUtils;


@SuppressWarnings("unchecked")
abstract public class AbstractMappedField implements DbmMappedField{
	
	private final JFishMappedEntry entry;
	
	private String name;
	private String label;
	private boolean identify;
//	private PropertyDescriptor property;
	private BaseColumnInfo column;

	private DbmMappedFieldType mappedFieldType = DbmMappedFieldType.FIELD;
	private StrategyType strategyType;
	private final JFishProperty propertyInfo;
	
//	private DataHolder<String, Object> dataHolder = new DataHolder<String, Object>();
	
	private boolean freezing;
	
	private List<DbmEntityFieldListener> fieldListeners = Collections.EMPTY_LIST;
	
	private VersionableType<? extends Object> versionableType;
	
	public AbstractMappedField(JFishMappedEntry entry, JFishProperty propertyInfo) {
		super();
		this.entry = entry;
		this.propertyInfo = propertyInfo;
		this.name = propertyInfo.getName();
		
		DbmFieldListeners listenersAnntation = propertyInfo.getAnnotation(DbmFieldListeners.class);
		if(listenersAnntation!=null){
			fieldListeners = DbmUtils.initDbmEntityFieldListeners(listenersAnntation);
		}else{
			if(!entry.getFieldListeners().isEmpty()){
				this.fieldListeners = new ArrayList<DbmEntityFieldListener>(entry.getFieldListeners());
			}
		}
	}

	public boolean isEnumerated(){
		return Enum.class.isAssignableFrom(propertyInfo.getType());
	}
//	abstract public Class getMappedType();
	
	/*abstract public Method getReadMethod();
	
	abstract protected Method getWriteMethod();*/
	
	@Override
	public void setValue(Object entity, Object value){
		propertyInfo.setValue(entity, value);
	}
	
	
	@Override
	public Object getValue(Object entity){
		Object value = propertyInfo.getValue(entity);
		if(isEnumerated()){
			Object actualValue = null;
			DbmEnumType etype = getEnumType();
			Enum<?> enumValue = (Enum<?>) value;
			if(etype==DbmEnumType.ORDINAL){
				actualValue = enumValue.ordinal();
			}else if(etype==DbmEnumType.STRING){
				actualValue = enumValue.name();
			}else{
				throw new DbmException("error enum type: " + etype);
			}
			return actualValue;
		}
		return value;
	}
	
	/*@Override
	public void setValueFromJdbc(Object entity, Object value){
//		Object newValue = convertPropertyValue(value);
		setValue(entity, value);
	}*/
	

	/****
	 * 获取sql参数值
	 */
	/*@Override
	public Object getValueForJdbc(Object entity){
		return getValue(entity);
	}*/
	
	/*@Override
	public SqlParameterValue getValueForJdbcAndFireDbmEventAction(Object entity, JFishEventAction eventAction){
//		Object oldValue = getColumnValue(entity);
		Object fieldValue = getValue(entity);
//		Object newValue = null;
		boolean doListener = false;
		if(JFishEventAction.insert==eventAction){
			if(!getFieldListeners().isEmpty()){
				for(DbmEntityFieldListener fl : getFieldListeners()){
					fieldValue = fl.beforeFieldInsert(propertyInfo, fieldValue);
					doListener = true;
				}
			}
		}else if(JFishEventAction.update==eventAction){
			if(!getFieldListeners().isEmpty()){
				for(DbmEntityFieldListener fl : getFieldListeners()){
					fieldValue = fl.beforeFieldUpdate(propertyInfo, fieldValue);
					doListener = true;
				}
			}
		}
		if(doListener){
//			setValueFromJdbc(entity, newValue);
			setValue(entity, fieldValue);
		}
//		return getValueForJdbc(entity);
		return convertSqlParameterValue(fieldValue);
	}*/
	
	public Object fireDbmEntityFieldEvents(Object fieldValue, JFishEventAction eventAction){
//		boolean doListener = false;
		if(JFishEventAction.insert==eventAction){
			if(!getFieldListeners().isEmpty()){
				for(DbmEntityFieldListener fl : getFieldListeners()){
					fieldValue = fl.beforeFieldInsert(propertyInfo, fieldValue);
//					doListener = true;
				}
			}
		}else if(JFishEventAction.update==eventAction){
			if(!getFieldListeners().isEmpty()){
				for(DbmEntityFieldListener fl : getFieldListeners()){
					fieldValue = fl.beforeFieldUpdate(propertyInfo, fieldValue);
//					doListener = true;
				}
			}
		}
		return fieldValue;
	}
	
	public Class<?> getColumnType(){
		Class<?> actualType = propertyInfo.getType();
		if(Enum.class.isAssignableFrom(actualType)){
			Enumerated enumerated = propertyInfo.getAnnotation(Enumerated.class);
			if(enumerated!=null){
				EnumType etype = enumerated.value();
				if(etype==EnumType.ORDINAL){
					actualType = int.class;
				}else if(etype==EnumType.STRING){
					actualType = String.class;
				}else{
					throw new DbmException("error enum type: " + etype);
				}
			}else{
				actualType = String.class;
			}
		}
		return actualType;
	}
	
	@Override
	public boolean isIdentify() {
		return identify;
	}


	@Override
	public void setIdentify(boolean identify) {
		this.checkFreezing("setIdentify");
		this.identify = identify;
	}

	@Override
	public BaseColumnInfo getColumn() {
		return column;
	}

	@Override
	public void setColumn(BaseColumnInfo column) {
		this.checkFreezing("setColumn");
		this.column = column;
	}

	@Override
	public String getName() {
		return name;
	}

	void setName(String name) {
		this.checkFreezing("setName");
		this.name = name;
	}


	@Override
	public JFishMappedEntry getEntry() {
		return entry;
	}

	@Override
	public String getLabel() {
		if(StringUtils.isBlank(label)){
			return getName();
		}
		return label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public boolean isGeneratedValueFetchBeforeInsert() {
		return isSeqStrategy();
	}

	@Override
	public boolean isGeneratedValue() {
		return this.strategyType!=null;
	}

	@Override
	public boolean isSeqStrategy() {
		return this.strategyType==StrategyType.SEQ;
	}

	@Override
	public boolean isIncreaseIdStrategy() {
		return this.strategyType==StrategyType.INCREASE_ID;
	}

	@Override
	public StrategyType getStrategyType() {
		return strategyType;
	}

	@Override
	public void setStrategyType(StrategyType strategyType) {
		this.strategyType = strategyType;
	}

	@Override
	public JFishProperty getPropertyInfo() {
		return propertyInfo;
	}

	@Override
	public void freezing() {
		freezing = true;
	}

	@Override
	public void checkFreezing(String name) {
		if(isFreezing()){
			throw new UnsupportedOperationException("the field["+getName()+"] is freezing now, don not supported this operation : " + name);
		}
	}

	@Override
	public boolean isFreezing() {
		return freezing;
	}

	@Override
	public DbmMappedFieldType getMappedFieldType() {
		return mappedFieldType;
	}

	@Override
	public final void setMappedFieldType(DbmMappedFieldType mappedFieldType) {
		this.mappedFieldType = mappedFieldType;
	}
	
	@Override
	public boolean isJoinTableField(){
		return false;
	}

	/*@Override
	public DataHolder<String, Object> getDataHolder() {
		return dataHolder;
	}*/
	
	public String toString(){
		return LangUtils.append(getName());
	}

	public List<DbmEntityFieldListener> getFieldListeners() {
		return fieldListeners;
	}

	public boolean isVersionControll() {
		return versionableType!=null;
	}

	public VersionableType<? extends Object> getVersionableType() {
		return versionableType;
	}

	public void setVersionableType(VersionableType<? extends Object> versionableType) {
		this.versionableType = versionableType;
	}

	public DbmEnumType getEnumType() {
		throw new UnsupportedOperationException();
	}
}

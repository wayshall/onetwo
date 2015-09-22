package org.onetwo.common.jfishdbm.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.onetwo.common.jfishdbm.annotation.DbmFieldListeners;
import org.onetwo.common.jfishdbm.dialet.AbstractDBDialect.StrategyType;
import org.onetwo.common.jfishdbm.event.DbmEntityFieldListener;
import org.onetwo.common.jfishdbm.event.JFishEventAction;
import org.onetwo.common.jfishdbm.mapping.version.VersionableType;
import org.onetwo.common.jfishdbm.utils.DbmUtils;
import org.onetwo.common.utils.JFishProperty;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.jdbc.core.SqlParameterValue;


@SuppressWarnings("unchecked")
abstract public class AbstractMappedField implements JFishMappedField{
	
	private final JFishMappedEntry entry;
	
	private String name;
	private String label;
	private boolean identify;
//	private PropertyDescriptor property;
	private BaseColumnInfo column;

	private JFishMappedFieldType mappedFieldType = JFishMappedFieldType.FIELD;
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
			fieldListeners = DbmUtils.initJFishEntityFieldListeners(listenersAnntation);
		}else{
			if(!entry.getFieldListeners().isEmpty()){
				this.fieldListeners = new ArrayList<DbmEntityFieldListener>(entry.getFieldListeners());
			}
		}
	}

//	abstract public Class getMappedType();
	
	/*abstract public Method getReadMethod();
	
	abstract protected Method getWriteMethod();*/
	
	@Override
	public void setValue(Object entity, Object value){
		propertyInfo.setValue(entity, value);
	}
	
	/****
	 * 根据实体的属性做一定的类型转换
	 * @param value
	 * @return
	 */
	protected Object convertPropertyValue(Object value){
		return DbmUtils.convertPropertyValue(propertyInfo, value);
	}
	/***
	 * 转成spring jdbc sql parameter 参数
	 * @param value
	 * @return
	 */
	protected SqlParameterValue convertSqlParameterValue(Object value){
		return DbmUtils.convertSqlParameterValue(propertyInfo, value);
	}
	
	@Override
	public Object getValue(Object entity){
		return propertyInfo.getValue(entity);
	}
	
	@Override
	public void setValueFromJdbc(Object entity, Object value){
		Object newValue = convertPropertyValue(value);
		propertyInfo.setValue(entity, newValue);
	}
	

	/****
	 * 获取sql参数值
	 */
	@Override
	public SqlParameterValue getValueForJdbc(Object entity){
		SqlParameterValue sqlValue = convertSqlParameterValue(propertyInfo.getValue(entity));
		return sqlValue;
	}
	
	@Override
	public SqlParameterValue getValueForJdbcAndFireDbmEventAction(Object entity, JFishEventAction eventAction){
//		Object oldValue = getColumnValue(entity);
		Object oldValue = getValue(entity);
		Object newValue = oldValue;
		boolean doListener = false;
		if(JFishEventAction.insert==eventAction){
			if(!getFieldListeners().isEmpty()){
				for(DbmEntityFieldListener fl : getFieldListeners()){
					newValue = fl.beforeFieldInsert(getName(), oldValue);
					doListener = true;
				}
			}
		}else if(JFishEventAction.update==eventAction){
			if(!getFieldListeners().isEmpty()){
				for(DbmEntityFieldListener fl : getFieldListeners()){
					newValue = fl.beforeFieldUpdate(getName(), oldValue);
					doListener = true;
				}
			}
		}
		if(doListener){
//			setValueFromJdbc(entity, newValue);
			setValue(entity, newValue);
		}
//		return getValueForJdbc(entity);
		return convertSqlParameterValue(newValue);
	}
	
	public Class<?> getColumnType(){
		return propertyInfo.getType();
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
	public JFishMappedFieldType getMappedFieldType() {
		return mappedFieldType;
	}

	@Override
	public final void setMappedFieldType(JFishMappedFieldType mappedFieldType) {
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

}

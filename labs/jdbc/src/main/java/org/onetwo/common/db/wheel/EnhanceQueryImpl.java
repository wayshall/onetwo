package org.onetwo.common.db.wheel;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.db.sql.AnotherQuery;
import org.onetwo.common.db.sql.AnotherQueryImpl;
import org.onetwo.common.db.sql.Condition;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.StringUtils;

@SuppressWarnings("unchecked")
public class EnhanceQueryImpl extends AnotherQueryImpl implements EnhanceQuery {

	public static class EntityValue implements ValueAdaptor{
		private Object entity;
		private String prefix;
		public EntityValue(Object entity, String prefix){
			this.entity = entity;
			this.prefix = prefix;
		}
		protected Object getEntity(){return entity;}
		protected String getNoPrefixName(String name){
			if(StringUtils.isBlank(prefix))
				return name;
			if(name.startsWith(this.prefix)){
				return name.substring(this.prefix.length());
			}else{
				return name;
			}
		}
		public Object getValue(Condition cond, DBConnection dbcon){
			Object value = MyUtils.getValue(getEntity(), getNoPrefixName(cond.getVarname()));
			return value;
		}
	}

	public static class SelfValueAdaptor implements ValueAdaptor{
		private Object value;
		public SelfValueAdaptor(Object value){
			this.value = value;
		}
		public Object getValue(Condition cond, DBConnection dbcon){
			return value;
		}
	}
	
	protected EnhanceQueryImpl(String sql) {
		super(sql);
	}
	protected SqlOperation sqlOperation = SqlOperation.query;
	

	public AnotherQuery registerListener(SqlOperation operation, EventListener listener){
		this.eventListeners.increasePut(operation, listener);
		return this;
	}
	
	public List<EventListener> getEventListers(SqlOperation operation){
		return this.eventListeners.getValueList(sqlOperation);
	}
	
	public void onBefore(SqlOperation operation, DBConnection dbcon){
		for(EventListener l : this.getEventListers(operation)){
			l.onBefore(dbcon);
		}
	}
	
	public void onAfter(SqlOperation operation, DBConnection dbcon, Object result){
		for(EventListener l : this.getEventListers(operation)){
			l.onAfter(dbcon, result);
		}
	}
	
	public void addBath(){
		Assert.notEmpty(this.conditions);
		
		List batchValues = new ArrayList();
		for(Condition cond : this.getActualConditions()){
			batchValues.add(cond.getValue());
			cond.clearValue();
		}
		this.values.add(batchValues);
		if(!isBatch()){
			this.sqlOperation = SqlOperation.batch;
		}
	}
	
	public boolean isBatch(){
		return SqlOperation.batch==this.sqlOperation;
	}

	public SqlOperation getSqlOperation() {
		return sqlOperation;
	}

	public void setSqlOperation(SqlOperation sqlType) {
		this.sqlOperation = sqlType;
	}
	public List getValues() {
		if(isBatch()){
			return values;
		}else{
			values = this.getConditionValues();
			return values;
		}
	}

	public AnotherQuery setParameters(Object bean) {
		if(!hasConditions())
			return this;
		ValueAdaptor ev = null;
		if(ValueAdaptor.class.isAssignableFrom(bean.getClass())){
			ev = (ValueAdaptor) bean;
		}else{
			ev = new EntityValue(bean, "");
		}
		for(Condition cond : this.conditions){
			cond.setValue(ev);
		}
		setNotompiled();
		return this;
	}
}

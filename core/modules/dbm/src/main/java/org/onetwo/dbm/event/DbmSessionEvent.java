package org.onetwo.dbm.event;

import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.LangUtils;

public class DbmSessionEvent implements DbmEvent<DbmSessionEventSource> {
//	private static final String RELATED_FIELD_PROPERTY_SYMBOL = ".";

	private Object object;
	private Class<?> entityClass;
	private DbmEventAction action;
	private DbmSessionEventSource eventSource;
	private int updateCount;

	private boolean joined;
//	private String[] relatedFields;
//	private boolean relatedReferenceOnly;//是否只保存引用，只对jointable有用……
	
	public DbmSessionEvent(Object object, DbmEventAction action, DbmSessionEventSource eventSource) {
		super();
		this.object = object;
		if(object!=null){
			this.entityClass = ReflectUtils.getObjectClass(LangUtils.getFirst(object));
		}
		this.action = action;
		this.eventSource = eventSource;
	}

	public Object getObject() {
		return object;
	}

	@Override
	public DbmEventAction getAction() {
		return action;
	}

	@Override
	public DbmSessionEventSource getEventSource() {
		return eventSource;
	}

	public void setAction(DbmEventAction action) {
		this.action = action;
	}

	public int getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(int updateCount) {
		this.updateCount = updateCount;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public boolean isJoined() {
		return joined;
	}

	public void setJoined(boolean joined) {
		this.joined = joined;
	}

	/*public String[] getRelatedFieldsBy(JFishMappedField field) {
		if(LangUtils.isEmpty(relatedFields))
			return null;
		List<String> relatedFieldList = new ArrayList<String>();
		String nfield = null;
		String prefix = field.getName() + RELATED_FIELD_PROPERTY_SYMBOL;
		for(String rfield : relatedFields){
			if(!rfield.startsWith(prefix))
				continue;
			nfield = rfield.substring(prefix.length());
			relatedFieldList.add(nfield);
		}
		return relatedFieldList.toArray(new String[relatedFieldList.size()]);
	}

	public void setRelatedFields(String... relatedFields) {
		this.relatedFields = relatedFields;
	}*/

}

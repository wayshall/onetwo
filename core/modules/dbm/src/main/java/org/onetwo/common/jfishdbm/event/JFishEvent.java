package org.onetwo.common.jfishdbm.event;

import org.onetwo.common.utils.ReflectUtils;

public class JFishEvent {
//	private static final String RELATED_FIELD_PROPERTY_SYMBOL = ".";

	private Object object;
	private Class<?> entityClass;
	private JFishEventAction action;
	private JFishEventSource eventSource;
	private int updateCount;

	private boolean joined;
//	private String[] relatedFields;
//	private boolean relatedReferenceOnly;//是否只保存引用，只对jointable有用……
	
	public JFishEvent(Object object, JFishEventAction action, JFishEventSource eventSource) {
		super();
		this.object = object;
		if(object!=null){
			this.entityClass = ReflectUtils.getObjectClass(object);
		}
		this.action = action;
		this.eventSource = eventSource;
	}

	public Object getObject() {
		return object;
	}

	public JFishEventAction getAction() {
		return action;
	}

	public JFishEventSource getEventSource() {
		return eventSource;
	}

	public void setAction(JFishEventAction action) {
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

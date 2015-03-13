package org.onetwo.common.log;

import java.io.Serializable;
import java.util.Collection;

import org.onetwo.common.utils.LangUtils;

public class DataChangedContext implements Serializable {

	private final Collection<EntityState> changedList = LangUtils.newArrayList();

	public void saveData(Object entity, Serializable id, Object[] state, String[] propertyNames, Object[] types){
		changedList.add(new EntityState(entity.getClass().getName(), id, propertyNames, state, null, types, DataOperateType.INSERT));
	}
	public void updateData(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Object[] types){
		changedList.add(new EntityState(entity.getClass().getName(), id, propertyNames, currentState, previousState, types, DataOperateType.UPDATE));
	}
	public void deleteData(Object entity, Serializable id, Object[] state, String[] propertyNames, Object[] types){
		changedList.add(new EntityState(entity.getClass().getName(), id, propertyNames, state, null, types, DataOperateType.DELETE));
	}
	
	public Collection<EntityState> getChangedList() {
		return changedList;
	}



	public static class EntityState{
		private final String entity;
		private final Object id;
//		private final Set<PropertyState> fields;
		
		private final Object[] currentState;
		private final Object[] previousState;
		private final String[] propertyNames;
		private final Object[] types;
		
		private final DataOperateType operationType;
		
		private EntityState(String entity, Object id, String[] propertyNames, Object[] currentState, Object[] previousState, Object[] types, DataOperateType operationType) {
			super();
			this.entity = entity;
			this.id = id;
			/*this.fields = new HashSet<DataChangedContext.PropertyState>(propertyNames.length);
			for (int i = 0; i < propertyNames.length; i++) {
				fields.add(new PropertyState(this, propertyNames[i], currentState[i], LangUtils.isEmpty(currentState)?null:previousState[i]));
			}*/
			this.propertyNames = propertyNames;
			this.currentState = currentState;
			this.previousState = previousState;
			this.types = types;
			this.operationType = operationType;
		}

		public String getEntity() {
			return entity;
		}

		public Object getId() {
			return id;
		}

		public Object[] getCurrentState() {
			return currentState;
		}

		public Object[] getPreviousState() {
			return previousState;
		}

		public String[] getPropertyNames() {
			return propertyNames;
		}

		public Object[] getTypes() {
			return types;
		}

		public DataOperateType getOperationType() {
			return operationType;
		}
		
		protected boolean logThisType(Object type){
			return true;
		}

	}
	
	public enum DataOperateType {
		INSERT,
		UPDATE,
		DELETE
	}
	
}

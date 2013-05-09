package org.onetwo.common.db.wheel;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.ExtQuery;
import org.onetwo.common.db.wheel.AbstractEntityOperation.EntityOperationType;
import org.onetwo.common.utils.MyUtils;


@SuppressWarnings({"unchecked", "rawtypes"})
public class JDao extends JDaoSupport {
	
	static JDao create(Wheel wheel){
		return new JDao(wheel);
	}
	
	private JDao(Wheel wheel){
		super(wheel);
	}

	public void save(Object object){
		executeOperation(EntityOperationType.insert, object, object);
	}

	public void saveOnly(Object object){
		InsertEntityOperation operation = (InsertEntityOperation)operationFactory.create(this, EntityOperationType.insert, object);
		operation.setInsertOnly(true);
		executeOperation(operation, object);
	}
	
	public void update(Object object){
		executeOperation(EntityOperationType.update, object, object);
	}
	
	public void updateIgnoreNull(Object object){
		AbstractEntityOperation operation = operationFactory.create(this, EntityOperationType.update, object);
		executeOperation(operation, object, true);
	}

	public <T> T find(Object id, Class<T> entityClass){
		EntityOperation operation = executeOperation(EntityOperationType.query, entityClass, id);
		return (T)operation.getSingleResult();
	}

	public <T> T find(Object id, Map entityMeta){
		EntityOperation operation = executeOperation(EntityOperationType.query, entityMeta, id);
		return (T)operation.getSingleResult();
	}

	public <T> List<T> findByExample(T object){
		AbstractEntityOperation operation = this.operationFactory.create(this, EntityOperationType.query, object);
		executeOperation(operation, object, true);
		return (List<T>)operation.getResult();
	}
	
	public <T> T delete(Object id, Class<T> entityClass){
		EntityOperation operation = executeOperation(EntityOperationType.delete, entityClass, id);
		return (T)operation.getResult();
	}
	
	public <T> T delete(Object id, Map entityMeta){
		EntityOperation operation = executeOperation(EntityOperationType.delete, entityMeta, id);
		return (T)operation.getSingleResult();
	}
	
	public void deleteOnly(Object id, Class<?> entityClass){
		DeleteEntityOperation operation = (DeleteEntityOperation)operationFactory.create(this, EntityOperationType.delete, entityClass);
		operation.setDeleteOnly(true);
		executeOperation(operation, id);
	}
	
	public <T> List<T> findByProperties(Class entityClass, Object... properties) {
		return this.findByProperties(entityClass, MyUtils.convertParamMap(properties));
	}

	public <T> List<T> findByProperties(Class entityClass, Map<Object, Object> properties) {
		ExtQuery extQuery = this.createExtQuery(entityClass, properties);
		extQuery.build();
		return super.queryList(extQuery);
	}

	public <T> List<T> findByProperties(Map<String, Object> entityClass, Map<Object, Object> properties) {
		ExtQuery extQuery = this.createExtQuery(entityClass, properties);
		extQuery.build();
		return super.queryList(extQuery);
	}

}

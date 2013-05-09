package org.onetwo.common.db.wheel;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.ExtQuery;
import org.onetwo.common.db.wheel.AbstractEntityOperation.EntityOperationType;
import org.onetwo.common.db.wheel.Wheel.WheelSQLSymbolManagerImpl;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.Assert;

@SuppressWarnings("unchecked")
public class JDaoSupport {
	
	private Wheel wheel;
	private ConnectionCreator creator;
	private ConnectionExecutor executor;
	
	EntityOperationFactory operationFactory;
	

	/*JDaoSupport(ConnectionCreator creator, ConnectionExecutor executor, EntityOperationFactory operationFactory) {
		super();
		this.creator = creator;
		this.executor = executor;
		this.operationFactory = operationFactory;
	}*/
	

	JDaoSupport(Wheel wheel) {
		this.wheel = wheel;
		this.creator = wheel.getConnectionCreator();
		this.executor = wheel.getConnectionExecutor();
		this.operationFactory = wheel.getEntityOperationFactory();
	}

	public Wheel getWheel() {
		return wheel;
	}


	public void execute(final SqlExeContext... ectxs){
//		final List results = new ArrayList(ectxs.length);
		this.executor.execute(creator, new ConnectionCallback(){

			@Override
			public Object doInConnection(DBConnection dbcon) {
				for(SqlExeContext ectx : ectxs){
					dbcon.execute(ectx);
				}
				return null;
			}
			
		});
	}
	
	public <T> List<T> queryList(final ExtQuery query){
//		final List results = new ArrayList(ectxs.length);
		List<T> result = (List<T>)this.executor.execute(creator, new ConnectionCallback(){

			@Override
			public Object doInConnection(DBConnection dbcon) {
				return dbcon.queryList(query);
			}
			
		});
		return result;
	}
	

	EntityOperation executeOperation(EntityOperationType type, Object entityClass, Object object){
		return executeOperation(type, entityClass, object, false);
	}

	EntityOperation executeOperation(EntityOperationType type, Object entityClass, Object object, boolean dynamic){
		Assert.notNull(object);
		String pname = "JDaoSupport.executeOperation(EntityOperationType type, Object entityClass, Object object, boolean dynamic)";
		UtilTimerStack.push(pname);
		
		EntityOperation operation = operationFactory.create(this, type, entityClass);
		executeOperation(operation, object, dynamic);
		
		UtilTimerStack.pop(pname);
		return operation;
	}

	void executeOperation(EntityOperation operation, Object operationObject){
		executeOperation(operation, operationObject, false);
	}

	void executeOperation(EntityOperation operation, Object operationObject, boolean dynamic){
		Assert.notNull(operation);
		operation.setOperationObject(operationObject);
		if(dynamic)
			operation.dynamicBuild();
		else
			operation.build();
		operation.execute();
	}
	
	EntityOperation executeByTableInfo(Object operationObject, TableInfo tableInfo){
		EntityOperation operation = operationFactory.create(this, EntityOperationType.query, tableInfo);
		executeOperation(operation, operationObject);
		return operation;
	}
	
	<T> T findByTableInfo(Object id, TableInfo tableInfo){
		EntityOperation operation = operationFactory.create(this, EntityOperationType.query, tableInfo);
		executeOperation(operation, id);
		return (T)operation.getResult();
	}

	protected ExtQuery createExtQuery(Class entityClass, Map<Object, Object> properties){
		ExtQuery q = getSQLSymbolManager().createQueryByObject(entityClass, properties);
		return q;
	}
	
	protected ExtQuery createExtQuery(Map entityClass, Map<Object, Object> properties){
		ExtQuery q = getSQLSymbolManager().createQueryByObject(entityClass, properties);
		return q;
	}
	
	protected WheelSQLSymbolManagerImpl getSQLSymbolManager(){
		return wheel.getSqlSymbolManager();
	}
	
}

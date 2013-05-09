package org.onetwo.common.db.wheel;

import org.onetwo.common.db.wheel.AbstractEntityOperation.EntityOperationType;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.LangUtils;

public class DefaultEntityOperationFactory implements EntityOperationFactory {

	private EntityBuilderFactory entityBuilderFactory;
	
	public DefaultEntityOperationFactory(EntityBuilderFactory entityBuilderFactory){
		this.entityBuilderFactory = entityBuilderFactory;
	}
	

	public AbstractEntityOperation create(JDaoSupport dao, EntityOperationType operation, Object entityClassAble){
		return create(dao, operation, entityBuilderFactory.create(entityClassAble));
	}
	
	public AbstractEntityOperation create(JDaoSupport dao, EntityOperationType operation, TableInfo tableInfo){
		return create(dao, operation, entityBuilderFactory.create(tableInfo));
	}
	
	public AbstractEntityOperation create(JDaoSupport dao, EntityOperationType operation, EntityBuilder builder){
		AbstractEntityOperation ecb = null;
		String pname = "DefaultEntityOperationFactory.create";
		UtilTimerStack.push(pname);
		if(EntityOperationType.insert==operation){
			ecb = createInsertEntityOperation(dao, builder);
		}else if(EntityOperationType.update==operation){
			ecb = createUpdateEntityOperation(dao, builder);
		}else if(EntityOperationType.delete==operation){
			ecb = createDeleteEntityOperation(dao, builder);
		}else if(EntityOperationType.query==operation){
			ecb = createQueryEntityOperation(dao, builder);
		}else{
			LangUtils.throwBaseException("unsupport entity operation : " + operation);
		}
		ecb.setOperationType(operation);
		UtilTimerStack.pop(pname);
		return ecb;
	}
	
	protected AbstractEntityOperation createInsertEntityOperation(JDaoSupport dao, EntityBuilder builder){
		return new InsertEntityOperation(dao, builder);
	}
	
	protected AbstractEntityOperation createUpdateEntityOperation(JDaoSupport dao, EntityBuilder builder){
		return new UpdateEntityOperation(dao, builder);
	}
	
	protected AbstractEntityOperation createDeleteEntityOperation(JDaoSupport dao, EntityBuilder builder){
		return new DeleteEntityOperation(dao, builder);
	}
	
	protected AbstractEntityOperation createQueryEntityOperation(JDaoSupport dao, EntityBuilder builder){
		return new QueryEntityOperation(dao, builder);
	}

}

package org.onetwo.common.db.wheel;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.list.L;

@SuppressWarnings("unchecked")
abstract public class AbstractEntityOperation implements EntityOperation {

	public static enum EntityOperationType {
		insert, update, delete, query
	}

	protected EntityBuilder entityBuilder;
	protected List<EntityExeContext> entityExeContexts = new ArrayList<EntityExeContext>(3);
	protected Object result;
	protected Object operationObject;
	
	protected JDaoSupport dao;
	
//	protected boolean staticBuild = true;
	private EntityOperationType operationType;

	// private EntityOperation opertion = EntityOperation.insert;

	protected AbstractEntityOperation(JDaoSupport dao, EntityBuilder entityBuilder) {
		this.dao = dao;
		this.entityBuilder = entityBuilder;
	}

	/* (non-Javadoc)
	 * @see org.onetwo.common.db.wheel.EntityOperation#getOperationObject()
	 */
	public Object getOperationObject() {
		return operationObject;
	}

	/* (non-Javadoc)
	 * @see org.onetwo.common.db.wheel.EntityOperation#setOperationObject(java.lang.Object)
	 */
	public void setOperationObject(Object operationObject) {
		this.operationObject = operationObject;
	}
	
	protected EntityExeContext createEntityExeContext(EntityBuilder entityBuilder, EnhanceQuery q){
		return new EntityExeContext(entityBuilder, q);
	}
	
	protected void checkOperationObject(Object operationObject){
		Assert.notNull(operationObject);
	}

	public EntityOperation build() {
		String pname = "AbstractEntityOperation.build";
		UtilTimerStack.push(pname);
		
		this.checkOperationObject(operationObject);

//		checkStaticBuild();
		EntityExeContext ectx = buildEntityExeContext();
		this.entityExeContexts.add(ectx);

		if(LangUtils.isMultiple(this.operationObject)){
			List list = L.tolist(this.operationObject, true);
			for(Object obj : list){
				ectx.addBatch(obj);
			}
		}else{
			ectx.setBy(operationObject);
		}

		UtilTimerStack.pop(pname);
		return this;
	}

	@SuppressWarnings("rawtypes")
	public EntityOperation dynamicBuild() {
		String pname = "AbstractEntityOperation.dynamicBuild";
		UtilTimerStack.push(pname);
		
		this.checkOperationObject(operationObject);
		
//		checkStaticBuild();
		if(LangUtils.isMultiple(this.operationObject)){
			List list = L.tolist(this.operationObject, true);
			for(Object obj : list){
				addDynamicBuild(obj);
			}
		}else{
			addDynamicBuild(operationObject);
		}
		
		UtilTimerStack.pop(pname);
		return this;
	}
	
	protected EntityOperation addDynamicBuild(Object object) {
		Assert.notNull(object);
		
		EntityExeContext ectx = dynamicBuildEntityExeContext(object);
		ectx.setBy(object);
		this.entityExeContexts.add(ectx);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.common.db.wheel.EntityOperation#execute()
	 */
	public void execute(){
		String pname = "AbstractEntityOperation.execute";
		UtilTimerStack.push(pname);
		
		beforeExecute();
		this.dao.execute(entityExeContexts.toArray(new EntityExeContext[this.entityExeContexts.size()]));
		afterExeucte();
		
		UtilTimerStack.pop(pname);
	}

	protected void beforeExecute(){
	}
	protected void afterExeucte(){
		this.result = this.getLastedExeContext().getResult();
	}
	
	/*protected void checkStaticBuild(){
		if(staticBuilded)
			LangUtils.throwBaseException("had build :  " + this.entityBuilder.getEntityClass());
	}*/
	
//	abstract protected EntityExeContext buildEntityExeContext();
//	abstract protected EntityExeContext dynamicBuildEntityExeContext(Object object);
	
	abstract protected EnhanceQuery createSqlParser();

	abstract protected EnhanceQuery dynamicCreateSqlParser(Object object);
	

	protected EntityExeContext buildEntityExeContext() {
		EnhanceQuery q = this.createSqlParser();
		return this.createEntityExeContext(entityBuilder, q);
	}

	protected EntityExeContext dynamicBuildEntityExeContext(Object object) {
		EnhanceQuery q = this.dynamicCreateSqlParser(object);
		return this.createEntityExeContext(entityBuilder, q);
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.common.db.wheel.EntityOperation#getResult()
	 */
	public Object getResult() {
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.common.db.wheel.EntityOperation#getSingleResult()
	 */
	public Object getSingleResult() {
		return LangUtils.getFirst(result);
	}
	
	protected EntityExeContext getLastedExeContext(){
		if(this.entityExeContexts==null || this.entityExeContexts.isEmpty())
			LangUtils.throwBaseException("the entity ExeContexts is empty, check it.");
		return this.entityExeContexts.get(this.entityExeContexts.size()-1);
	}


	/* (non-Javadoc)
	 * @see org.onetwo.common.db.wheel.EntityOperation#getOperationType()
	 */
	public EntityOperationType getOperationType() {
		return operationType;
	}

	void setOperationType(EntityOperationType operationType) {
		this.operationType = operationType;
	}

	
}

package org.onetwo.common.ejb.jpa;

import java.io.Serializable;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.EntityManagerProvider;
import org.onetwo.common.db.IResourceEntity;
import org.onetwo.common.db.IWithResourceEntity;
import org.onetwo.common.db.IdEntity;
import org.onetwo.common.db.event.DbEventListeners;
import org.onetwo.common.db.event.RemoveEvent;
import org.onetwo.common.db.event.RemoveEventListener;
import org.onetwo.common.db.event.SaveOrUpdateEvent;
import org.onetwo.common.db.event.SaveOrUpdateEventListener;
import org.onetwo.common.db.sqlext.SQLSymbolManagerFactory;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.StringUtils;


@SuppressWarnings({"rawtypes"})
abstract public class AbstractBaseEntityManager extends AbstractEntityManager {
	
	
	public AbstractBaseEntityManager(){
		super();
	}
	
	public void init(){
		super.init();
		SQLSymbolManagerFactory.getInstance().register(EntityManagerProvider.JPA, JPASQLSymbolManager.create());
	}
	
	@Override
	protected DbEventListeners newDbEventListeners() {
		return new JPAEventListeners();
	}

	@SuppressWarnings("unchecked")
	abstract public EntityManager getEntityManager();
	
	/***
	 * 根据id查找实体，如果没有，抛出异常
	 */
	public <T> T load(Class<T> entityClass, Serializable id){
		this.checkEntityIdValid(id);
		T entity = (T)getEntityManager().find(entityClass, id);
		if(entity==null)
			throw new ServiceException("entity["+entityClass.getName()+"] is not exist. id:["+id+"]");
		return entity;
	}
	
	/***
	 * 根据id查找实体，如果没有，返回null
	 */
	public <T> T findById(Class<T> entityClass, Serializable id){
		if(!MyUtils.checkIdValid(id))
			return null;
		T entity = (T) getEntityManager().find(entityClass, id);
		return entity;
	}

	@SuppressWarnings("unchecked")
	public <T> T save(T entity){
		this.prepareSave(entity);
		SaveOrUpdateEvent event = new SaveOrUpdateEvent(entity, this);
		this.fireSaveOrUpdate(event);
		this.afterSave(event.getObject());
		return (T)event.getObject();
	}
	
	protected void fireSaveOrUpdate(SaveOrUpdateEvent event){
		SaveOrUpdateEventListener[] eventListeners = getDbEventListeners().getSaveOrUpdateEventListeners();
		if(eventListeners==null)
			return ;
		for(SaveOrUpdateEventListener lst : eventListeners){
			lst.onSaveOrUpdate(event);
		}
	}

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void persist(Object entity){
		this.getEntityManager().persist(entity);
	}
	
	protected void prepareSave(Object entity){
	}
	
	protected void afterSave(Object entity){
		if(entity instanceof IWithResourceEntity){
			this.processIWithResourceEntity((IWithResourceEntity) entity);
		}
	}
	
	protected void processIWithResourceEntity(IWithResourceEntity withRes){
//		getEntityManager().flush();
		
		if(withRes == null || withRes.getResourceIds() == null) {
			return ;
		}
		
		for(Long resId : withRes.getResourceIds()){
			if(resId==null){
				logger.error(withRes.getClass()+" resId is null. ");
				continue;
			}
			IResourceEntity resEntity = (IResourceEntity)this.findById(withRes.getResourceClass(), resId);
			if(resEntity==null){
				logger.error(withRes.getClass()+" resEntity is notfoud: " + resId);
				continue;
			}
			if(resId.equals(resEntity.getEntityId()))
				continue;
			
			resEntity.setEntityId((Long)((IdEntity)withRes).getId());
			resEntity.setEntityName(withRes.getClass().getName());
			this.save(resEntity);
		}
	}
	
	public void remove(Object entity){
		if(entity==null)
			return ;
//		getEntityManager().remove(getEntityManager().merge(entity));
		fireRemove(new RemoveEvent(entity, this));
	}
	
	protected void fireRemove(RemoveEvent event){
		RemoveEventListener[] eventListeners = getDbEventListeners().getRemoveEventListeners();
		if(eventListeners==null)
			return ;
		for(RemoveEventListener lst : eventListeners){
			lst.onRemove(event);
		}
	}
	
	public <T> T removeById(Class<T> entityClass, Serializable id){
		T entity = findById(entityClass, id);
		remove(entity);
		return entity;
	}
	
	protected long countHqlResult(final String hql, final Object... values) {
		String countHql = MyUtils.getCountSql(hql);
		Long count = findUnique(countHql, values);
		return count;
	}
	
	public void flush(){
		getEntityManager().flush();
	}
	
	public void clear(){
		getEntityManager().clear();
	}
	
	public <T> T merge(T entity){
		return getEntityManager().merge(entity);
	}
	
	public DataQuery createSQLQuery(String sqlString, Class entityClass){
		return createSQLQuery(sqlString, entityClass, null);
	}

	public DataQuery createMappingSQLQuery(String sqlString, String resultSetMapping){
		return createSQLQuery(sqlString, null, resultSetMapping);
	}
	
	public DataQuery createSQLQuery(String sqlString, Class entityClass, String resultSetMapping){
		Query query = null;
		if(entityClass!=null)
			query = this.getEntityManager().createNativeQuery(sqlString, entityClass);
		else if(StringUtils.isNotBlank(resultSetMapping))
			query = this.getEntityManager().createNativeQuery(sqlString, resultSetMapping);
		else
			query = this.getEntityManager().createNativeQuery(sqlString);
		DataQuery dquery = new JpaQueryImpl(query);
		return dquery;
	}
	
	public DataQuery createNamedQuery(String name){
		Query query = this.getEntityManager().createNamedQuery(name);
		DataQuery dquery = new JpaQueryImpl(query);
		return dquery;
	}
	
	public DataQuery createQuery(String ejbqlString){
		DataQuery dquery = new JpaQueryImpl(getEntityManager().createQuery(ejbqlString));
		return dquery;
	}
}

package org.onetwo.common.hibernate;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.onetwo.common.base.HibernateSequenceNameManager;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.EntityManagerProvider;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.MyUtils;

@SuppressWarnings("unchecked")
public class HibernateEntityManagerImpl extends AbstractEntityManager {

	protected Logger logger = Logger.getLogger(this.getClass());
	
	private SessionFactory sessionFactory; 
	
	private SequenceNameManager sequenceNameManager = new HibernateSequenceNameManager();

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	

	public DataQuery createSQLQuery(String sqlString, Class<?> entityClass){
		SQLQuery query = getSession().createSQLQuery(sqlString);
		if(entityClass!=null)
			query.addEntity(entityClass);
		DataQuery dquery = new HibernateQueryImpl(query);
		return dquery;
	}
	
	public DataQuery createNamedQuery(String name){
		Query query = getSession().getNamedQuery(name);
		DataQuery dquery = new HibernateQueryImpl(query);
		return dquery;
	}
	
	public DataQuery createQuery(String ejbqlString){
		DataQuery dquery = new HibernateQueryImpl(getSession().createQuery(ejbqlString));
		return dquery;
	}


	/***
	 * 根据id查找实体，如果没有，返回null
	 */
	public <T> T findById(Class<T> entityClass, Serializable id){
		if(!MyUtils.checkIdValid(id))
			return null;
		T entity = (T) getSession().get(entityClass, id);
		return entity;
	}

	@Override
	public void flush() {
		getSession().flush();
	}

	@Override
	public <T> T load(Class<T> entityClass, Serializable id) {
		this.checkEntityIdValid(id);
		T entity = (T)getSession().get(entityClass, id);
		if(entity==null)
			throw new ServiceException("entity["+entityClass.getName()+"] is not exist. id:["+id+"]");
		return entity;
	}

	@Override
	public void persist(Object entity) {
		getSession().persist(entity);
	}
	@Override
	public void update(Object entity) {
		getSession().update(entity);
	}

	@Override
	public void remove(Object entity) {
		if(entity==null)
			return ;
		getSession().delete(entity);
	}

	@Override
	public <T> T removeById(Class<T> entityClass, Serializable id) {
		T entity = findById(entityClass, id);
		if(entity!=null)
			remove(entity);
		return entity;
	}

	@Override
	public <T> T save(T entity) {
		try {
			getSession().saveOrUpdate(entity);
//			getSession().merge(entity);
		} catch (SQLGrammarException e) {
			if("42000".equals(e.getSQLState())){
				this.createSequence(entity.getClass());
			}
			getSession().saveOrUpdate(entity);
		}
		return entity;
	}
	
	public Session getSession(){
		return this.sessionFactory.getCurrentSession();
	}
	
	public EntityManagerProvider getEntityManagerProvider(){
		return EntityManagerProvider.Hibernate;
	}

	@Override
	public SequenceNameManager getSequenceNameManager() {
		return sequenceNameManager;
	}

	public void setSequenceNameManager(SequenceNameManager sequenceNameManager) {
		this.sequenceNameManager = sequenceNameManager;
	}

	@Override
	public DataQuery createMappingSQLQuery(String sqlString, String resultSetMapping) {
		throw new UnsupportedOperationException("unsupported createMappingSQLQuery");
	}

	@Override
	public void clear() {
		getSession().clear();
	}

	@Override
	public <T> T merge(T entity) {
		return (T)getSession().merge(entity);
	}
	
}

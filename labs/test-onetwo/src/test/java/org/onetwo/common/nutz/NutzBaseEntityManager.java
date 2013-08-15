package org.onetwo.common.nutz;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.EntityManagerProvider;
import org.onetwo.common.db.ILogicDeleteEntity;
import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.db.QueryBuilder;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.utils.Page;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public class NutzBaseEntityManager implements BaseEntityManager {
	
	private NutzBaseDao nutzDao;
	
	@Resource
	private DataSource dataSource;
	
	public NutzBaseEntityManager(){
	}
	
	@PostConstruct
	public void init(){
		nutzDao = new NutzBaseDao(dataSource);
	}
	
	public NutzBaseDao getNutzDao() {
		return nutzDao;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	@Override
	public <T> T load(Class<T> entityClass, Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T findById(Class<T> entityClass, Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Object entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T save(T entity) {
		return this.nutzDao.insert(entity);
	}

	@Override
	public void persist(Object entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Object entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeList(List entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T removeById(Class<T> entityClass, Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> findAll(Class<T> entityClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Number countRecord(Class entityClass, Map<Object, Object> properties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Number countRecord(Class entityClass, Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T findUnique(String sql, Object... values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T findUnique(Class<T> entityClass, Object... properties) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public <T> T findUnique(Class<T> entityClass, Map<Object, Object> properties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> findByProperties(Class<T> entityClass,
			Object... properties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> findByProperties(Class<T> entityClass,
			Map<Object, Object> properties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void findPage(Class entityClass, Page page, Object... properties) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void findPage(Class entityClass, Page page, Map<Object, Object> properties) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(ILogicDeleteEntity entity) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public <T extends ILogicDeleteEntity> T deleteById(Class<T> entityClass, Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLSymbolManager getSQLSymbolManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> findList(JFishQueryValue queryValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T findUnique(JFishQueryValue queryValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void findPage(Page<T> page, JFishQueryValue squery) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T merge(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataQuery createSQLQuery(String sqlString, Class entityClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataQuery createMappingSQLQuery(String sqlString, String resultSetMapping) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataQuery createQuery(String ejbqlString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataQuery createNamedQuery(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataQuery createQuery(String sql, Map<String, Object> values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getSequences(String sequenceName, boolean createIfNotExist) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getSequences(Class entityClass, boolean createIfNotExist) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityManagerProvider getEntityManagerProvider() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public <T> T getRawManagerObject() {
		// TODO Auto-generated method stub
		return null;
	}


}

package org.onetwo.common.fish.test;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.EntityManagerProvider;
import org.onetwo.common.db.ILogicDeleteEntity;
import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.db.ParamValues.PlaceHolder;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.fish.JFishEntityManager;
import org.onetwo.common.fish.JFishQuery;
import org.onetwo.common.fish.JFishQueryBuilder;
import org.onetwo.common.fish.spring.JFishDaoImplementor;
import org.onetwo.common.utils.Page;
import org.springframework.jdbc.core.RowMapper;

public class NullJFishEntityManagerImpl implements JFishEntityManager {

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
	public <T> T save(T entity) {
		// TODO Auto-generated method stub
		return null;
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
	public <T> List<T> findByProperties(Class<T> entityClass, Object... properties) {
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
	public void findPage(Class entityClass, Page page,
			Map<Object, Object> properties) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(ILogicDeleteEntity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T extends ILogicDeleteEntity> T deleteById(Class<T> entityClass,
			Serializable id) {
		// TODO Auto-generated method stub
		return null;
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
	public DataQuery createMappingSQLQuery(String sqlString,
			String resultSetMapping) {
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
	public JFishQuery createJFishQueryByQName(String queryName, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JFishQuery createJFishQueryByQName(String queryName,
			PlaceHolder type, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JFishQuery createCountJFishQueryByQName(String queryName,
			Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> findListByQName(String queryName, Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T findUniqueByQName(String queryName, Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Page<T> findPageByQName(String queryName, Page<T> page,
			Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Page<T> findPageByQName(String queryName,
			RowMapper<T> rowMapper, Page<T> page, Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int removeAll(Class<?> entityClass) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public JFishDaoImplementor getJfishDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T saveWith(T entity, String... relatedFields) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> int saveRef(T entity, String... relatedFields) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> int dropRef(T entity, String... relatedFields) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> int clearRef(T entity, String... relatedFields) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public JFishQueryBuilder createQueryBuilder(Class<?> entityClass) {
		// TODO Auto-generated method stub
		return null;
	}

}

package org.onetwo.plugins.dq;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.BaseEntityManagerAdapter;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.EntityManagerProvider;
import org.onetwo.common.db.FileNamedQueryFactory;
import org.onetwo.common.db.FileNamedSqlGenerator;
import org.onetwo.common.db.ILogicDeleteEntity;
import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.db.ParamValues.PlaceHolder;
import org.onetwo.common.db.QueryBuilder;
import org.onetwo.common.db.QueryProvider;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;
import org.onetwo.common.utils.propconf.NamespacePropertiesManager;

@SuppressWarnings("unchecked")
public class NullBaseEntityManager extends BaseEntityManagerAdapter implements BaseEntityManager {

	private FileNamedQueryFactory<? extends NamespaceProperty> fileNamedQueryFactory = new FileNamedQueryFactory(){

		@Override
		public NamespacePropertiesManager getNamespacePropertiesManager() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object findOne(String queryName, Object... params) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void initQeuryFactory(QueryProvider em) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public DataQuery createQuery(String queryName, Object... args) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public DataQuery createQuery(String queryName, PlaceHolder type,
				Object... args) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public DataQuery createCountQuery(String queryName, Object... args) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List findList(String queryName, Object... params) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object findUnique(String queryName, Object... params) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Page findPage(String queryName, Page page, Object... params) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public FileNamedSqlGenerator createFileNamedSqlGenerator(
				String queryName, Map params) {
			// TODO Auto-generated method stub
			return null;
		}

		
		
		
	};
	
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
	public void update(Object entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Object entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void removes(Collection<T> entities) {
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
	public <T> void findPage(Class<T>  entityClass, Page<T>  page,
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
	public DataQuery createSQLQuery(String sqlString, Class<?> entityClass) {
		// TODO Auto-generated method stub
		return new DataQuery(){

			@Override
			public int executeUpdate() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public <T> List<T> getResultList() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <T> T getSingleResult() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DataQuery setFirstResult(int startPosition) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DataQuery setMaxResults(int maxResult) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DataQuery setParameter(int position, Object value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DataQuery setParameter(String name, Object value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DataQuery setParameters(Map<String, Object> params) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DataQuery setParameters(List<Object> params) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DataQuery setParameters(Object[] params) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DataQuery setPageParameter(Page page) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DataQuery setLimited(Integer first, Integer size) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <T> T getRawQuery(Class<T> clazz) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DataQuery setQueryConfig(Map<String, Object> configs) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isCacheable() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setCacheable(boolean cacheable) {
				// TODO Auto-generated method stub
				
			}
			
		};
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
	public <T> T getRawManagerObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getRawManagerObject(Class<T> rawClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileNamedQueryFactory getFileNamedQueryFactory() {
		return this.fileNamedQueryFactory;
	}

	public void setFileNamedQueryFactory(
			FileNamedQueryFactory<? extends NamespaceProperty> fileNamedQueryFactory) {
		this.fileNamedQueryFactory = fileNamedQueryFactory;
	}

	@Override
	public <T> List<T> findByProperties(QueryBuilder squery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void findPage(Page page, QueryBuilder query) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> Collection<T> removeByIds(Class<T> entityClass, Serializable[] id) {
		// TODO Auto-generated method stub
		return null;
	}

}

package org.onetwo.common.base;

import java.io.Serializable;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.IdEntity;
import org.onetwo.common.jdbc.JFishJdbcOperations;
import org.onetwo.common.jdbc.JdbcDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*****
 * 一些底层数据访问的实现
 * 
 * @author weishao
 *
 * @param <T>
 * @param <PK>
 */
@SuppressWarnings("rawtypes")
abstract public class AbstractDao<T extends IdEntity, PK extends Serializable> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

//	protected SessionFactory sessionFactory;

	protected Class<T> entityClass;
	
//	protected SQLSymbolManager hqlSymbolManager;


	public AbstractDao(){
//		hqlSymbolManager = SQLSymbolManagerFactory.getHibernate();
	}

//	abstract public SequenceNameManager getSequenceNameManager();
	abstract public BaseEntityManager getBaseEntityManager();
	abstract public JdbcDao getJdbcDao();

	protected JFishJdbcOperations getJdbcTemplate() {
		return getJdbcDao().getJdbcTemplate();
	}
	/**
	 * 取得当前Session.
	 */
	/*public Session getSession() {
		return sessionFactory.getCurrentSession();
	}*/

	/**
	 * 获取当前VO所对应的的序列
	 * 
	 * @return
	 * @throws Exception
	 */
	public Long createSequences() {
		// 获取当前VO所在HIBERNATE的 表名字
		/*String sql = getSequenceNameManager().getSequenceSql(entityClass);
		Long id = null;
		try {
			id = getJdbcDao().queryForLong(sql);
//			logger.info("createSequences id : "+id);
		} catch (DataAccessException e) {
			SQLException se = (SQLException) e.getCause();
			if ("42000".equals(se.getSQLState()) && se.getErrorCode() == 2289) {
				try {
					getJdbcTemplate().execute(getSequenceNameManager().getCreateSequence(entityClass));
					id = getJdbcTemplate().queryForLong(getSequenceNameManager().getSequenceSql(entityClass));
				} catch (Exception ne) {
					ne.printStackTrace();
					throw e;
				}
				if (id == null)
					throw e;
			}
		}
		return id;*/
		return this.getBaseEntityManager().getSequences(entityClass, true);
	}

}

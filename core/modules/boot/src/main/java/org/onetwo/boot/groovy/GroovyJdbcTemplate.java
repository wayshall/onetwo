package org.onetwo.boot.groovy;

import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.CUtils;
import org.onetwo.dbm.core.spi.DbmSessionFactory;
import org.onetwo.dbm.exception.DbmException;
import org.onetwo.dbm.jdbc.DbmNamedJdbcTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author weishao zeng
 * <br/>
 */
@Transactional
@GroovyBindingBean(alias = "jdbc")
public class GroovyJdbcTemplate implements InitializingBean {
//	static private final Object[] EMPTY_OBJECT = new Object[] {};
	
	@Autowired(required = false)
	private DbmSessionFactory dbmSessionFactory;
	private DbmNamedJdbcTemplate dbmNamedJdbcTemplate;

	@Override
	public void afterPropertiesSet() throws Exception {
		dbmNamedJdbcTemplate = dbmSessionFactory.getServiceRegistry().getDbmJdbcOperations().getDbmNamedJdbcOperations();
	}

	public List<?> list(String sql, Class<?> resultType, Object...params) {
		this.check();
		return dbmNamedJdbcTemplate.queryForList(sql, CUtils.asMap(params), resultType);
	}
	
	/***
	 * 适配没有参数的方法
	 * @author weishao zeng
	 * @param sql
	 * @param resultType
	 * @return
	 */
//	public List<?> list(String sql, Class<?> resultType) {
//		return list(sql, resultType, EMPTY_OBJECT);
//	}
	
	
	public Object one(String sql, Class<?> resultType, Object...params) {
		this.check();
		return dbmNamedJdbcTemplate.queryForObject(sql, CUtils.asMap(params), resultType);
	}
	
//	public Object one(String sql, Class<?> resultType) {
//		return one(sql, resultType, EMPTY_OBJECT);
//	}
	
	
//	public List<?> listForMap(String sql) {
//		return listForMap(sql, EMPTY_OBJECT);
//	}
	
	public List<?> listForMap(String sql, Object...params) {
		this.check();
		return dbmNamedJdbcTemplate.queryForList(sql, CUtils.asMap(params));
	}
	
	public Map<String, Object> oneForMap(String sql, Object...params) {
		this.check();
		return dbmNamedJdbcTemplate.queryForMap(sql, CUtils.asMap(params));
	}

//	public Map<String, Object> oneForMap(String sql) {
//		return oneForMap(sql, EMPTY_OBJECT);
//	}

	private void check() {
		if (dbmNamedJdbcTemplate==null) {
			throw new DbmException("DbmNamedJdbcTemplate not found!");
		}
	}

}

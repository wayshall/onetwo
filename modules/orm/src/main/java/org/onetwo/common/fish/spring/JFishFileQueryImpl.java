package org.onetwo.common.fish.spring;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.db.sql.DynamicQuery;
import org.onetwo.common.db.sql.DynamicQueryFactory;
import org.onetwo.common.db.sql.QueryOrderByable;
import org.onetwo.common.fish.JFishQuery;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.CUtils;
import org.springframework.jdbc.core.RowMapper;

public class JFishFileQueryImpl implements JFishQuery, QueryOrderByable {

	private DynamicQuery query;
//	private JFishNamedFileQueryInfo info;
	private JFishDaoImplementor jfishFishDao;
	private boolean needParseSql;
	private JFishQuery jfishQuery;
	private RowMapper<?> rowMapper;
	
	private boolean countQuery;


	public JFishFileQueryImpl(JFishDaoImplementor jfishFishDao, JFishNamedFileQueryInfo info, boolean count) {
		this(jfishFishDao, info, count, info.isNeedParseSql());
	}
	public JFishFileQueryImpl(JFishDaoImplementor jfishFishDao, JFishNamedFileQueryInfo info, boolean count, boolean needParseSql) {
		Assert.notNull(jfishFishDao);
		this.jfishFishDao = jfishFishDao;
		this.countQuery = count;
		this.needParseSql = needParseSql;
		
		String sql = count?info.getCountSql():info.getSql();
		Class<?> mappedClass = countQuery?Long.class:info.getMappedEntityClass();
		
		if(needParseSql){
			this.query = DynamicQueryFactory.createJFishDynamicQuery(sql, mappedClass);
			if(info.isIgnoreNull())
				this.query.ignoreIfNull();
		}else{
			this.jfishQuery = this.jfishFishDao.createJFishQuery(sql, mappedClass);
		}
	}

	public boolean isNeedParseSql() {
		return needParseSql;
	}

	public JFishQuery setParameter(Integer index, Object value) {
		if(isNeedParseSql())
			query.setParameter(index, value);
		else
			jfishQuery.setParameter(index, value);
		return this;
	}

	public JFishQuery setParameter(String name, Object value) {
		JNamedQueryKey key = JNamedQueryKey.ofKey(name);
		if(key!=null){
			this.processQueryKey(key, value);
		}else{
			if(isNeedParseSql()){
				query.setParameter(name, value);
			}else{
				jfishQuery.setParameter(name, value);
			}
		}
		return this;
	}

	public <T> T getSingleResult() {
		if(isNeedParseSql()){
			this.jfishQuery = jfishFishDao.createJFishQuery(query);
		}
//		this.jfishQuery.setRowMapper(rowMapper);
		return this.jfishQuery.getSingleResult();
	}

	public int executeUpdate() {
		if(isNeedParseSql()){
			this.jfishQuery = jfishFishDao.createJFishQuery(query);
		}
		return this.jfishQuery.executeUpdate();
	}
	
	public <T> List<T> getResultList() {
		if(isNeedParseSql()){
			this.jfishQuery = jfishFishDao.createJFishQuery(query);
		}
		this.jfishQuery.setRowMapper(rowMapper);
		return jfishQuery.getResultList();
	}

	public JFishQuery setFirstResult(int firstResult) {
		if(isNeedParseSql()){
			query.setFirstRecord(firstResult);
		}else{
			jfishQuery.setFirstResult(firstResult);
		}
		return this;
	}

	public JFishQuery setMaxResults(int maxResults) {
		if(isNeedParseSql()){
			query.setMaxRecord(maxResults);
		}else{
			jfishQuery.setMaxResults(maxResults);
		}
		return this;
	}

	public JFishQuery setResultClass(Class<?> resultClass) {
		if(isNeedParseSql()){
			query.setEntityClass(resultClass);
		}else{
			jfishQuery.setResultClass(resultClass);
		}
		return this;
	}

	public JFishQuery setParameters(Map<String, Object> params) {
		/*if(isNeedParseSql()){
			query.setParameters(params);
		}else{
			jfishQuery.setParameters(params);
		}*/
		for(Entry<String, Object> entry : params.entrySet()){
			setParameter(entry.getKey(), entry.getValue());
		}
		return this;
	}

	public void setQueryAttributes(Map<Object, Object> params) {
		Object key;
		for(Entry<Object, Object> entry : params.entrySet()){
			key = entry.getKey();
			if(String.class.isInstance(key)){
				setParameter(key.toString(), entry.getValue());
			}else if(Integer.class.isInstance(key)){
				setParameter((Integer)key, entry.getValue());
			}else if(JNamedQueryKey.class.isInstance(key)){
				this.processQueryKey((JNamedQueryKey)key, entry.getValue());
			}
		}
	}

	private void processQueryKey(JNamedQueryKey qkey, Object value){
		switch (qkey) {
			case ResultClass:
				if(!countQuery)
					setResultClass((Class<?>)value);
				break;
			case ASC:
				String[] ascFields = CUtils.asStringArray(value);
				asc(ascFields);
				break;
			case DESC:
				String[] desFields = CUtils.asStringArray(value);
				desc(desFields);
				break;
			default:
				break;
		}
		
//		if(JNamedQueryKey.ResultClass.equals(qkey) && !countQuery){//count qyery 忽略
//			setResultClass((Class<?>)value);
//		}
	}
	
	public JFishQuery setParameters(List<?> params) {
		if(isNeedParseSql()){
			query.setParameters(params);
		}else{
			jfishQuery.setParameters(params);
		}
		return this;
	}

	@Override
	public void setRowMapper(RowMapper<?> rowMapper) {
		this.rowMapper = rowMapper;
	}

	@Override
	public void asc(String... fields) {
		if(isNeedParseSql() && QueryOrderByable.class.isAssignableFrom(query.getClass())){
			((QueryOrderByable)query).asc(fields);
		}else{
			throw new UnsupportedOperationException("the query can't supported orderby, you need set ignore.null to true.");
		}
	}

	@Override
	public void desc(String... fields) {
		if(isNeedParseSql() && QueryOrderByable.class.isAssignableFrom(query.getClass())){
			((QueryOrderByable)query).desc(fields);
		}else{
			throw new UnsupportedOperationException("the query can't supported orderby, you need set ignore.null to true.");
		}

	}

}

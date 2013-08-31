package org.onetwo.common.hibernate.sql;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.sql.DynamicQuery;
import org.onetwo.common.db.sql.DynamicQueryFactory;
import org.onetwo.common.db.sql.QueryOrderByable;
import org.onetwo.common.spring.sql.JNamedQueryKey;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.Page;

public class HibernateFileQueryImpl implements DataQuery, QueryOrderByable {

	private DynamicQuery query;
//	private JFishNamedFileQueryInfo info;
	private BaseEntityManager baseEntityManager;
	private boolean needParseSql;
	private boolean hqlQeury;
	private DataQuery dataQuery;
	
	private boolean countQuery;


	protected int firstRecord = -1;
	protected int maxRecords;

	public HibernateFileQueryImpl(BaseEntityManager baseEntityManager, HibernateNamedInfo info, boolean count) {
		this(baseEntityManager, info, count, info.isNeedParseSql());
	}
	public HibernateFileQueryImpl(BaseEntityManager baseEntityManager, HibernateNamedInfo info, boolean count, boolean needParseSql) {
		Assert.notNull(baseEntityManager);
		this.baseEntityManager = baseEntityManager;
		this.countQuery = count;
		this.needParseSql = needParseSql;
		this.hqlQeury = info.isHql();
		
		String sql = count?info.getCountSql():info.getSql();
		Class<?> mappedClass = countQuery?Long.class:info.getMappedEntityClass();
		
		if(needParseSql){
			this.query = DynamicQueryFactory.createJFishDynamicQuery(sql, mappedClass);
			if(info.isIgnoreNull())
				this.query.ignoreIfNull();
		}else{
			this.dataQuery = createDataQuery(sql, mappedClass);
		}
	}
	
	private DataQuery createDataQuery(DynamicQuery query){
		DataQuery dataQuery = null;
		if(hqlQeury){
			dataQuery = this.baseEntityManager.createQuery(query.getTransitionSql());
		}else{
			dataQuery = this.baseEntityManager.createSQLQuery(query.getTransitionSql(), query.getEntityClass());
		}
		return dataQuery;
	}
	
	private DataQuery createDataQuery(String sql, Class<?> mappedClass){
		DataQuery dataQuery = null;
		if(hqlQeury){
			dataQuery = this.baseEntityManager.createQuery(sql);
		}else{
			dataQuery = this.baseEntityManager.createSQLQuery(sql, mappedClass);
		}
		return dataQuery;
	}
	
	private void createDataQueryIfNecessarry(){
		if(isNeedParseSql()){
			this.dataQuery = createDataQuery(query);
		}
		if(this.firstRecord>0)
			this.dataQuery.setFirstResult(firstRecord);
		if(this.maxRecords>0)
			this.dataQuery.setMaxResults(maxRecords);
	}

	public boolean isNeedParseSql() {
		return needParseSql;
	}

	public DataQuery setParameter(int index, Object value) {
		if(isNeedParseSql())
			query.setParameter(index, value);
		else
			dataQuery.setParameter(index, value);
		return this;
	}

	public DataQuery setParameter(String name, Object value) {
		JNamedQueryKey key = JNamedQueryKey.ofKey(name);
		if(key!=null){
			this.processQueryKey(key, value);
		}else{
			if(isNeedParseSql()){
				query.setParameter(name, value);
			}else{
				dataQuery.setParameter(name, value);
			}
		}
		return this;
	}

	public <T> T getSingleResult() {
		this.createDataQueryIfNecessarry();
//		this.jfishQuery.setRowMapper(rowMapper);
		return this.dataQuery.getSingleResult();
	}

	public int executeUpdate() {
		this.createDataQueryIfNecessarry();
		return this.dataQuery.executeUpdate();
	}
	
	public <T> List<T> getResultList() {
		this.createDataQueryIfNecessarry();
		return dataQuery.getResultList();
	}

	public DataQuery setFirstResult(int firstResult) {
		if(isNeedParseSql()){
			query.setFirstRecord(firstResult);
		}else{
			dataQuery.setFirstResult(firstResult);
		}
		return this;
	}

	public DataQuery setMaxResults(int maxResults) {
		if(isNeedParseSql()){
			query.setMaxRecord(maxResults);
		}else{
			dataQuery.setMaxResults(maxResults);
		}
		return this;
	}

	public DataQuery setResultClass(Class<?> resultClass) {
		if(isNeedParseSql()){
			query.setEntityClass(resultClass);
		}else{
//			dataQuery.setResultClass(resultClass);
		}
		return this;
	}

	public DataQuery setParameters(Map<String, Object> params) {
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
	
	public DataQuery setParameters(List<Object> params) {
		if(isNeedParseSql()){
			query.setParameters(params);
		}else{
			dataQuery.setParameters(params);
		}
		return this;
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
	

	@Override
	public DataQuery setParameters(Object[] params) {
		if(ArrayUtils.hasNotElement(params))
			return this;
		int position = 1;
		for(Object value : params){
			query.setParameter(position++, value);
		}
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public DataQuery setPageParameter(final Page page) {
		return setLimited(page.getFirst()-1, page.getPageSize());
	}
	
	public DataQuery setLimited(final Integer first, final Integer max) {
		this.firstRecord = first;
		this.maxRecords = max;
		return this;
	}
	
	@Override
	public <T> T getRawQuery() {
		this.createDataQueryIfNecessarry();
		return dataQuery.getRawQuery();
	}
	@Override
	public DataQuery setQueryConfig(Map<String, Object> configs) {
		return null;
	}

}

package org.onetwo.plugins.dq;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;

public class DataQueryHolderForTest implements DataQuery {
	
	private String sql;
	private Map<Object, Object> params = LangUtils.newHashMap();
	private Map<String, ?>[] batchValues;
	
	public DataQueryHolderForTest() {
		super();
	}
	
	
	
	public Map<String, ?>[] getBatchValues() {
		return batchValues;
	}



	public void setBatchValues(Map<String, ?>[] batchValues) {
		this.batchValues = batchValues;
	}



	public void setSql(String sql) {
		this.sql = sql;
	}



	public Map<Object, Object> getParams() {
		return params;
	}

	public void setParams(Map<Object, Object> params) {
		this.params = params;
	}



	public String getSql() {
		return sql;
	}



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
		return this;
	}

	@Override
	public DataQuery setMaxResults(int maxResult) {
		return this;
	}

	@Override
	public DataQuery setParameter(int position, Object value) {
		this.setParameter(position, value);
		return null;
	}

	@Override
	public DataQuery setParameter(String name, Object value) {
		this.params.put(name, value);
		return this;
	}

	@Override
	public DataQuery setParameters(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public DataQuery setParameters(List<Object> params) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public DataQuery setParameters(Object[] params) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public DataQuery setPageParameter(Page page) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public DataQuery setLimited(Integer first, Integer size) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public <T> T getRawQuery(Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataQuery setQueryConfig(Map<String, Object> configs) {
		// TODO Auto-generated method stub
		return this;
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
	
	

}

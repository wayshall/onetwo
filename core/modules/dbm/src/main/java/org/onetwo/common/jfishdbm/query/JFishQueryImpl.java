package org.onetwo.common.jfishdbm.query;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.db.sqlext.ParamValues.PlaceHolder;
import org.onetwo.common.jfishdbm.dialet.DBDialect;
import org.onetwo.common.jfishdbm.support.JFishDaoImplementor;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.Assert;
import org.slf4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class JFishQueryImpl implements JFishQuery {
	private static final int INVALID_VALUE = -1;
	private static final int INVALID_VALUE_MAX_RESULTS = 0;

	private static final String FIRST_RESULT_NAME = "JFishQueryFirstResult";
	private static final String MAX_RESULT_NAME = "JFishQueryMaxResult";
	
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private JFishDaoImplementor JFishDaoImplementor;
	private DBDialect dbDialect;
	private String sqlString;
//	private Map<String, Object> parameters = new LinkedHashMap<String, Object>();
	private JFishQueryValue parameters = null;
	
	private Class<?> resultClass;
	
	private int firstResult = 0;
	private int maxResults = INVALID_VALUE_MAX_RESULTS;
	
	private RowMapper<?> rowMapper;
//	private QType qtype;

	public JFishQueryImpl(JFishDaoImplementor jFishDao, String sqlString, Class<?> resultClass) {
		super();
		this.JFishDaoImplementor = jFishDao;
		this.dbDialect = this.JFishDaoImplementor.getDialect();
		this.sqlString = sqlString;
		this.resultClass = resultClass;
		this.parameters = JFishQueryValue.create(PlaceHolder.NAMED, null);
	}
	
	/**********
	 * field = :1 and field = :2
	 */
	@Override
	public JFishQuery setParameter(Integer index, Object value){
//		this.checkQueryType(PlaceHolder.POSITION);
//		createParameterIfNull();
		this.parameters.setValue(index, value);
		return this;
	}
	
	/*********
	 * field = :name1 and field = :name2
	 */
	@Override
	public JFishQuery setParameter(String name, Object value){
//		createParameterIfNull();
		this.parameters.setValue(name, value);
		return this;
	}
	
	public JFishQuery setParameters(Map<String, Object> params){
//		createParameterIfNull();
		this.parameters.setValue(params);
		return this;
	}
	
	public JFishQuery setParameters(List<?> params){
		Assert.notNull(params);
		for(int index=0; index<params.size(); index++){
			setParameter(index, params.get(index));
		}
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.common.fish.JFishQuery#getSingleResult()
	 */
	@Override
	public <T> T getSingleResult(){
		String fname = this.getClass().getSimpleName()+".getSingleResult";
		UtilTimerStack.push(fname);
		
		String sql = getSqlString();
		JFishQueryValue params = this.getActualParameters(sql);
		T result = null;
		
		if(rowMapper!=null){
			result = (T)this.JFishDaoImplementor.findUnique(params, rowMapper);
		}else{
			result = this.JFishDaoImplementor.findUnique(params);
		}
		
		UtilTimerStack.pop(fname);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.onetwo.common.fish.JFishQuery#getResultList()
	 */
	@Override
	public <T> List<T> getResultList(){
		String fname = this.getClass().getSimpleName()+".getResultList";
		UtilTimerStack.push(fname);
		
		List<T> result = null;
		String sql = getSqlString();
		JFishQueryValue params = this.getActualParameters(sql);
		
		if(rowMapper!=null){
			result = (List<T>)this.JFishDaoImplementor.findList(params, rowMapper);
		}else{
			result = this.JFishDaoImplementor.findList(params);
		}
		
		UtilTimerStack.pop(fname);
		return result;
	}
	

	public String getSqlString() {
		String sql = sqlString;
		if(isLimitedQuery()){
			if(this.parameters.isNamed())
				sql = dbDialect.getLimitStringWithNamed(sqlString, FIRST_RESULT_NAME, MAX_RESULT_NAME);
			else
				sql = dbDialect.getLimitString(sqlString);
		}
		if(UtilTimerStack.isActive()){
			this.logger.info("sql:"+sql);
		}
		return sql;
	}

	public JFishDaoImplementor getJFishDaoImplementor() {
		return JFishDaoImplementor;
	}

	public JFishQueryValue getActualParameters(String sql) {
		this.parameters.setResultClass(resultClass);
		this.parameters.setSql(sql);
		if(!isLimitedQuery()){
			return this.parameters;
		}
		/*if(!params.containsKey(FIRST_RESULT_NAME))
			params.put(FIRST_RESULT_NAME, this.firstResult);
		if(!params.containsKey(MAX_RESULT_NAME))
			params.put(MAX_RESULT_NAME, dbDialect.getMaxResults(this.firstResult, maxResults));*/
		
		this.dbDialect.addLimitedValue(parameters, FIRST_RESULT_NAME, firstResult, MAX_RESULT_NAME, maxResults);
		
		if(UtilTimerStack.isActive()){
			this.logger.info("params"+parameters.getValues());
		}
		
		return this.parameters;
	}

	public boolean isLimitedQuery(){
		return this.getFirstResult()>INVALID_VALUE && this.getMaxResults()>INVALID_VALUE_MAX_RESULTS;
	}

	/* (non-Javadoc)
	 * @see org.onetwo.common.fish.JFishQuery#getFirstResult()
	 */
	public int getFirstResult() {
		return firstResult;
	}

	/* (non-Javadoc)
	 * @see org.onetwo.common.fish.JFishQuery#setFirstResult(int)
	 */
	@Override
	public JFishQuery setFirstResult(int firstResult) {
		this.firstResult = firstResult;
		return this;
	}

	/* (non-Javadoc)
	 * @see org.onetwo.common.fish.JFishQuery#getMaxResult()
	 */
	public int getMaxResults() {
		return maxResults;
	}

	/* (non-Javadoc)
	 * @see org.onetwo.common.fish.JFishQuery#setMaxResult(int)
	 */
	@Override
	public JFishQuery setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	public JFishQuery setResultClass(Class<?> resultClass) {
		this.resultClass = resultClass;
		return this;
	}

	public int executeUpdate(){
		int result = 0;
		String sql = getSqlString();
		JFishQueryValue params = getActualParameters(sql);
		result = JFishDaoImplementor.executeUpdate(params);
		return result;
	}

	protected void setSqlString(String sqlString) {
		this.sqlString = sqlString;
	}

	public void setRowMapper(RowMapper<?> rowMapper) {
		this.rowMapper = rowMapper;
	}

	@Override
	public void setQueryAttributes(Map<Object, Object> params) {
		Object key;
		for(Entry<Object, Object> entry : params.entrySet()){
			key = entry.getKey();
			if(String.class.isInstance(key)){
				setParameter(key.toString(), entry.getValue());
			}else if(Integer.class.isInstance(key)){
				setParameter((Integer)key, entry.getValue());
			}
		}
	}
}

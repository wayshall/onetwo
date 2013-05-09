package org.onetwo.common.db.wheel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.onetwo.common.db.ExtQuery;
import org.onetwo.common.db.sql.Condition;
import org.onetwo.common.db.wheel.EnhanceQuery.SqlOperation;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;

@SuppressWarnings("unchecked")
public class DBConnection implements WheelAware {

	public static DBConnection Create(Connection conn, boolean autoClose){
		DBConnection dbcon = new DBConnection(conn, autoClose);
		return dbcon;
	}
	
	public static DBConnection Create(Connection conn){
		DBConnection dbcon = new DBConnection(conn);
		return dbcon;
	}
	
	public static DBConnection Create(DBConnection conn){
		DBConnection dbcon = new DBConnection(conn.connection);
		dbcon.wheel = conn.wheel;
		return dbcon;
	}
	
	private Connection connection;
//	private PreparedStatement preparedStatement;
	private Statement statement;
//	private boolean autoCommit = true;
	private boolean autoClose;
	private Wheel wheel;

	private DBConnection(Connection connection, boolean autoClose){
		this.connection = connection;
		this.autoClose = autoClose;
	}
	private DBConnection(Connection connection){
		this.connection = connection;
	}
	
	public Connection getConnection(){
		return this.connection;
	}
	
	public boolean isAutoCommit() {
		try {
			return this.connection.getAutoCommit();
		} catch (SQLException e) {
			LangUtils.throwBaseException(e);
		}
		return false;
	}

	public void setAutoCommit(boolean autoCommit) {
		try {
			this.connection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			LangUtils.throwBaseException(e);
		}
	}

	public boolean isAutoClose() {
		return autoClose;
	}

	public void setAutoClose(boolean autoClose) {
		this.autoClose = autoClose;
	}

	public ResultSet query(String sql, Object...objects){
		return query(sql, MyUtils.convertParamMap(objects));
	}
	
	protected ResultSet query(String sql, Map params){
		return (ResultSet) execute(sql, params, false);
	}
	
	public Object unique(String sql, Map params){
		Map row = one(sql, params);
		Collection rs = row.values();
		if(rs==null || rs.isEmpty())
			return null;
		return rs.iterator().next();
	}
	
	public <T> T value(String sql, Object...objects){
		Map map = one(sql, MyUtils.convertParamMap(objects));
		return (T)map.values().iterator().next();
	}
	
	public Map one(String sql, Object...objects){
		return one(sql, MyUtils.convertParamMap(objects));
	}
	
	public Map one(String sql, Map params){
		List<Map> datas = queryForList(sql, params);
		if(datas==null || datas.isEmpty())
			return null;
		Map row = datas.get(0);
		return row;
	}
	
	public List<Map> queryForList(String sql, Map params){
		try {
			List<Map> datas = (List<Map>) execute(sql, params, false);
			return datas;
		}finally{
			autoHandleClose();
		}
	}

	public <T> T one(EnhanceQuery q, Class<?> entityClass){
		List datalist = queryForList(q, entityClass);
		if(datalist.isEmpty())
			return null;
		return (T) datalist.get(0);
	}
	
	public List queryForList(EnhanceQuery q, Class<?> entityClass){
		BeanDataRowMapper mapper = getBeanDataRowMapper(entityClass);
		ResultSetLooperHandler handler = new ResultSetLooperHandler(mapper);
		return queryForList(q, handler);
	}
	
	public List queryForList(EnhanceQuery q, DataRowMapper rowMapper){
		ResultSetLooperHandler handler = new ResultSetLooperHandler(rowMapper);
		return queryForList(q, handler);
	}
	
	public List queryForList(EnhanceQuery q, ResultSetHandler handler){
		ResultSet rs = null;
		List datalist = null;
		try {
			rs = (ResultSet) execute(q, false);
			datalist = (List)handler.handleData(rs);
		}catch(Exception e){
			LangUtils.throwBaseException("query list error : " + e.getMessage(), e);
		}finally{
			DBUtils.closeResultSet(rs);
			autoHandleClose();
		}
		return LangUtils.emptyIfNull(datalist);
	}
	
	protected void autoHandleClose(){
		closeStatement();
		if(isAutoClose())
			close();
	}
	
	public int update(String sql, Map params){
		return (Integer) execute(sql, params, isAutoClose());
	}
	
	public void batch(String sql){
		setAutoCommit(false);
		createPreparedStatement(sql);
	}
	
	public DBConnection addBatch(){
		try {
			this.getPreparedStatement().addBatch();
		} catch (Exception e) {
			LangUtils.throwBaseException("add batch error : " + e.getMessage(), e);
		}
		return this;
	}
	
	public PreparedStatement getPreparedStatement(){
		return (PreparedStatement) statement;
	}
	
	public DBConnection setParameter(List values){
		return setParameter(values, false);
	}
	
	public DBConnection setParameter(List values, boolean addbatch){
		setPstmParameter(null, defaultPreparedStatementSetter(), values);
		if(addbatch)
			addBatch();
		return this;
	}

	/*public Object execute(EntityValueSetter setter){
		Object result = execute(setter.getSqlParser(), isAutoClose());
		return result;
	}*/

	public Object execute(String sql, Map params, boolean autoClose){
		EnhanceQuery q = EnhanceQueryFacotry.create(sql);
		if(params!=null && !params.isEmpty())
			q.setParameters(params);
		return execute(q, autoClose);
	}

	public Object execute(EnhanceQuery q){
		return execute(q, isAutoClose());
	}
	
	protected BeanDataRowMapper getBeanDataRowMapper(Class clazz){
		BeanDataRowMapper bdm = new BeanDataRowMapper(clazz);
		return wheel.awareWheel(bdm);
		/*if(wheel!=null)
			return wheel.awareWheel(bdm);
		else
			return bdm;*/
	}
	
	protected ResultSetLooperHandler getResultSetLooperHandler(DataRowMapper mapper){
		return new ResultSetLooperHandler(mapper);
	}
	
	protected ResultSetHandler getDefaultResultSetHandler(){
		BeanDataRowMapper mapper = getBeanDataRowMapper(HashMap.class);
		ResultSetLooperHandler handler = new ResultSetLooperHandler(mapper);
		return handler;
	}
	
	public List<Map> queryWithoutParams(String sql, boolean autoClose){
		BeanDataRowMapper beanMapper = getBeanDataRowMapper(HashMap.class);
		return (List<Map>)queryWithoutParams(sql, getResultSetLooperHandler(beanMapper), autoClose);
	}
	
	public Object queryWithoutParams(String sql, ResultSetHandler rsHandler, boolean autoClose){
		Object result = null;
		ResultSet res = null;
		try {
			this.createStatement();
			res = statement.executeQuery(sql);
			result = rsHandler.handleData(res);
		} catch (SQLException e) {
			LangUtils.throwBaseException("execute query error: " + sql, e);
		} finally{
			DBUtils.closeResultSet(res);
			if(autoClose)
				close();
		}
		return result;
	}
	
	public int updateWithoutParams(String sql, boolean autoClose){
		int result = -1;
		try {
			this.createStatement();
			result = statement.executeUpdate(sql);
		} catch (SQLException e) {
			LangUtils.throwBaseException("execute query error: " + sql, e);
		} finally{
			if(autoClose)
				close();
		}
		return result;
	}
	
	protected void setPstmParameter(DBConnection dbcon, PreparedStatementSetter setter, List values) {
		PreparedStatement preStatement = (PreparedStatement) statement;
		if(dbcon==null)
			dbcon = Create(this);
		int index = 0;
		for(Object value : values){
			try {
				if(value instanceof ConnectionCallback){
					ConnectionCallback ccb = (ConnectionCallback)value;
					value = ccb.doInConnection(dbcon);
				}
				if(setter==null)
					DBUtils.setPstmParameter(getPreparedStatement(), index+1, value);
				else
					setter.setParameter(preStatement, index+1, value);
			} catch (Exception e) {
				LangUtils.throwBaseException("set statement parameter error : " + e.getMessage(), e);
			}
		}
	}
	
	protected void setParameterByConditions(DBConnection dbcon, PreparedStatementSetter setter, List<Condition> conditions, List values) {
		PreparedStatement preStatement = (PreparedStatement) statement;
		Object value;
		if(dbcon==null)
			dbcon = Create(this);
		for(Condition cond : conditions){
			if(values!=null){
				value = values.get(cond.getIndex());
			}else{
				value = cond.getValue();
			}
			try {
				if(value instanceof ConnectionCallback){
					ConnectionCallback ccb = (ConnectionCallback)value;
					value = ccb.doInConnection(dbcon);
				}else if(value instanceof ValueAdaptor){
					ValueAdaptor ev = (ValueAdaptor)value;
					value = ev.getValue(cond, dbcon);
				}
				if(setter==null){
					setter = defaultPreparedStatementSetter();
//					DBUtils.setPstmParameter(getPreparedStatement(), cond.getIndex()+1, value);
				}
				setter.setParameter(preStatement, cond.getIndex()+1, value);
			} catch (Exception e) {
				LangUtils.throwBaseException("set statement parameter for ["+cond.getName()+"] error : " + e.getMessage(), e);
			}
		}
	}
	

	public void execute(SqlExeContext context){
		String pname = "DBConnection.execute(SqlExeContext context)";
		UtilTimerStack.push(pname);
		
		context.build();
		Object result = execute(context.getSqlParser(), context.getStatementSetter(), context.getResultSetHandler(), isAutoClose());
		context.setResult(result);
		
		UtilTimerStack.pop(pname);
	}
	
	public <T> List<T> queryList(ExtQuery extQuery){
		if(!extQuery.hasBuilt())
			extQuery.build();
		List<T> result = (List<T>)this.executeRawSQL(extQuery.getSql(), (List<?>)extQuery.getParamsValue().getValues());
		return result;
	}
	
	public Object execute(EnhanceQuery q, boolean autoClose){
		return execute(q, defaultPreparedStatementSetter(), null, autoClose);
	}
	
	protected PreparedStatementSetter defaultPreparedStatementSetter(){
		if(wheel==null){
			return PreparedStatementSetter.dbhandler;
		}
		return wheel.getPreparedStatementSetter();
	}
	

	public Object executeRawSQL(String sql, List<?> paramValues){
		return executeRawSQL(sql, paramValues, getDefaultResultSetHandler());
	}
	
	public Object executeRawSQL(String sql, List<?> paramValues, ResultSetHandler handler){
		createPreparedStatement(sql);
		PreparedStatement preparedStatement = getPreparedStatement();
		if(LangUtils.isNotEmpty(paramValues)){
			PreparedStatementSetter setter = defaultPreparedStatementSetter();
			int index = 1;
			for(Object value : paramValues){
				try {
					setter.setParameter(preparedStatement, index, value);
				} catch (SQLException e) {
					LangUtils.throwBaseException("set parameter error : sql[" + sql+"], index["+index+"]", e);
				}
				index++;
			}
		}
		Object result = null;
		try {
			result = this.executePreparedQuery(handler);
		} catch (Exception e) {
			handleException("execute sql error : " + sql, e);
		}finally{
			if(isAutoClose())
				close();
		}
		return result;
	}
	
	protected void handleException(String msg, Exception e){
		throw new ServiceException(msg, e);
	}
	
	public Object execute(EnhanceQuery q, PreparedStatementSetter setter, ResultSetHandler handler, boolean autoClose){
		if(!q.hasCompiled())
			q.compile();
		String sql = q.getTransitionSql();
		
		if(JDBC.inst().isDebug()){
			LangUtils.debug("dbconecton.execute sql : ${0}", sql);
		}
		
		createPreparedStatement(sql);
		PreparedStatement preparedStatement = getPreparedStatement();
		DBConnection newDbcon = DBConnection.Create(this);
		
		if(q.getSqlOperation()==SqlOperation.batch){
			int count = 0;
			List<List> batchValues = q.getValues();
			for(List values : batchValues){
				this.setParameterByConditions(newDbcon, setter, q.getActualConditions(), values);
				this.addBatch();
				count++;
				if(JDBC.inst().config().isBatch() && count%JDBC.inst().config().batchSize()==0 && count!=batchValues.size()){
					try {
						preparedStatement.executeBatch();
					} catch (SQLException e) {
						LangUtils.throwBaseException("exectute batch error", e);
					}
				}
			}
		}else{
			this.setParameterByConditions(newDbcon, setter, q.getActualConditions(), q.getValues());
		}

		Object result = null;
		try {
			q.onBefore(q.getSqlOperation(), newDbcon);
			if(q.getSqlOperation()==SqlOperation.query){
				result = this.executePreparedQuery(handler);
			}else if(q.getSqlOperation()==SqlOperation.batch){
				result = preparedStatement.executeBatch();
			}else{
				result = preparedStatement.executeUpdate();
			}
			q.onAfter(q.getSqlOperation(), newDbcon, result);
		} catch (SQLException e) {
			handleException("execute sql error : " + sql, e);
		} finally{
			if(autoClose)
				close();
		}
		return result;
	}
	
	public Object executePreparedQuery(ResultSetHandler handler) throws SQLException{
		Object result = null;
		ResultSet res = null;
		try {
			res = getPreparedStatement().executeQuery();
			if(handler!=null)
				result = handler.handleData(res);
			else{
				result = getDefaultResultSetHandler().handleData(res);
			}
		} finally{
			DBUtils.closeResultSet(res);
		}
		return result;
	}

	public DBConnection createStatement(){
		try {
			this.statement = connection.createStatement();
		} catch (SQLException e) {
			throw new ServiceException("createPreparedStatement error.", e);
		}
		return this;
	}

	public DBConnection createPreparedStatement(String sql){
		try {
//			connection.setAutoCommit(autoCommit);
			this.statement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			throw new ServiceException("createPreparedStatement error : " + sql, e);
		}
		return this;
	}
	
	public void close(){
		closeStatement();
		DBUtils.closeCon(connection);
		this.connection = null;
	}
	
	public void commit(){
		try {
			connection.commit();
		} catch (SQLException e) {
			LangUtils.throwBaseException("commit error.", e);
		}
	}
	
	public void closeStatement(){
		/*if(!isAutoCommit())
			commit();*/
		DBUtils.closePreparedStatement(this.statement);
	}

	@Override
	public void setWheel(Wheel wheel) {
		this.wheel = wheel;
	}
	
}

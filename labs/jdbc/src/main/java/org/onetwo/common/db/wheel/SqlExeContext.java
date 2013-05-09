package org.onetwo.common.db.wheel;


public class SqlExeContext {

	private EnhanceQuery sqlParser;
	protected Object result;

	private ResultSetHandler resultSetHandler;
	private PreparedStatementSetter statementSetter;

	public SqlExeContext(EnhanceQuery sqlParser) {
		super();
		this.sqlParser = sqlParser;
	}
	
	protected void initBeforeBuild(){};
	
	public void build(){
		this.initBeforeBuild();
		this.sqlParser.compile();
	}
	
	public SqlExeContext setBy(Object entity){
		this.sqlParser.setParameters(entity);
		return this;
	}
	
	public SqlExeContext addBatch(Object entity){
		setBy(entity);
		this.sqlParser.addBath();
		return this;
	}

	public EnhanceQuery getSqlParser() {
		return sqlParser;
	}

	public void setSqlParser(EnhanceQuery sqlParser) {
		this.sqlParser = sqlParser;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public ResultSetHandler getResultSetHandler() {
		return resultSetHandler;
	}

	public PreparedStatementSetter getStatementSetter() {
		return statementSetter;
	}

	public void setStatementSetter(PreparedStatementSetter statementSetter) {
		this.statementSetter = statementSetter;
	}

}

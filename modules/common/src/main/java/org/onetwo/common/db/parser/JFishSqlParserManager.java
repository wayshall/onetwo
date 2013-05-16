package org.onetwo.common.db.parser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JFishSqlParserManager {
	
	private final static JFishSqlParserManager instance = new JFishSqlParserManager();
	
	public static JFishSqlParserManager getInstance() {
		return instance;
	}
	
	private Map<String, SqlStatment> sqlCaches = new ConcurrentHashMap<String, SqlStatment>();
	private boolean debug;
	
	private JFishSqlParserManager(){
		debug = false;
	}
	
	public SqlStatment getSqlStatment(String sql){
//		int hc = sql.hashCode();
		SqlStatment statments = this.sqlCaches.get(sql);
		if(statments!=null){
//			this.printSqlStaments(statments);
			return statments;
		}
		JFishSqlParser sqlParser = new JFishSqlParser(sql);
		sqlParser.setDebug(debug);
		statments = sqlParser.parse();
		if(debug){
			this.printSqlStaments(statments);
		}
		this.sqlCaches.put(sql, statments);
		
		return statments;
	}
	
	protected void printSqlStaments(SqlStatment statments){
		if(!debug)
			return ;
		for(SqlObject sqlObj : statments.getSqlObjects()){
			System.out.println(sqlObj);
		}
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public String toString(){
		return sqlCaches.toString();
	}

}

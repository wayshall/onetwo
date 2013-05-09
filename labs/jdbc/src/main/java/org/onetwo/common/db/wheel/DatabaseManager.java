package org.onetwo.common.db.wheel;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.SimpleExpression;


public interface DatabaseManager {

	@SuppressWarnings("serial")
	public static class DBKeys {
		public static final String mysql = "mysql";
		public static final String oracle = "oracle";
		public static final String type = "db.type";
		public static final String jdbc_driver = "jdbc.driverClass";
		public static final String mysql_driver = "com.mysql.jdbc.Driver";
		public static final String oralce_driver = "oracle.jdbc.driver.OracleDriver";
		
		public static final String host = "db.host";
		public static final String port = "db.port";
		public static final String dbname = "db.name";

		public static final String db_username = "jdbc.username";
		public static final String db_password = "jdbc.password";
		
		public static final Map<String, String> dbtypes = new HashMap<String, String>(){
			{
				put(oracle, "jdbc:oracle:thin:@${db.host}:${db.port}/${db.name}");
				put(mysql, "jdbc:mysql://${db.host}:${db.port}/${db.name}?useUnicode=true&amp;characterEncoding=UTF-8");
			}
		};
		
		@SuppressWarnings("unchecked")
		public static String parse(String type, Map properties){
			if(!dbtypes.containsKey(type))
				throw new ServiceException("unsupported database : " + type);
			String str = dbtypes.get(type);
			str = SimpleExpression.DOLOR_INSTANCE.parse(str, properties);
			return str;
		}
	}
	
	public boolean isInit();
	
	public void initDatabaseManager();
	
	public void setDbconfigPath(String dbconfig);
	
	public void setDbconfig(Properties dbconfig);
	
	public Connection getConnection();
	
	public void close(Connection con);

	public boolean isConnectSuccess();
	
	public boolean isMySQL();
	
	public boolean isOracle();
	
	public void setUser(String user);
	
	public String getUser();
	
	public void setPassword(String password);
	
	public String getPassword();
	
	public void setDataBaseHost(String dbhost);
	
	public void setDataBasePort(String dbport);
	
	public void setDataBaseName(String dbname);
	
	public String getDataBaseType();
	
	public void setDataBaseType(String type);
	
	public String getDatabaseName();
}
 
package org.onetwo.common.db.wheel;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.PropConfig;
import org.onetwo.common.utils.propconf.PropUtils;

@SuppressWarnings("serial")
public class DefaultDatabaseManage implements DatabaseManager {

	static {
		try {
			ReflectUtils.loadClass(DBKeys.mysql_driver);
			ReflectUtils.loadClass(DBKeys.oralce_driver);
		} catch (Exception e) {
		}
	}

	private Properties dbconfig;

	private DataSource datasource;

	private Connection connection;

	private String jdbcurl;

	private boolean init;
	
	public DefaultDatabaseManage() {
	}

	/***************************************************************************
	 * public void init(){ ComboPooledDataSource ds = new
	 * ComboPooledDataSource(); try {
	 * ds.setDriverClass(this.dbconfig.getProperty("jdbc.driverClass"));
	 * ds.setJdbcUrl(this.dbconfig.getProperty("jdbc.url"));
	 * ds.setUser(this.dbconfig.getProperty("jdbc.username"));
	 * ds.setPassword(this.dbconfig.getProperty("jdbc.password")); } catch
	 * (PropertyVetoException e) { throw new RuntimeException(e); }
	 * this.datasource = ds; }
	 */

	public Connection getConnection2() {
		try {
			this.connection = this.datasource.getConnection();
		} catch (Exception e) {
			throw new RuntimeException("get connection error!", e);
		}
		return this.connection;
	}

	public void initDatabaseManager() {
		try {
			String cls = this.getJdbcDriver();
			if (StringUtils.isNotBlank(cls))
				Class.forName(cls);

			jdbcurl = dbconfig.getProperty("jdbc.url");
			if (StringUtils.isBlank(jdbcurl)) {
				jdbcurl = DBKeys.parse(this.getDataBaseType(), getDbconfig());
			}
			this.init = true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isInit() {
		return init;
	}

	public void setNotInit() {
		this.init = false;
		close(connection);
	}

	public void close(Connection con) {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		if (!isInit())
			initDatabaseManager();

		try {
			if (connection == null || connection.isClosed()) {
				this.connection = DriverManager.getConnection(jdbcurl, getUser(), getPassword());
			}
		} catch (Exception e) {
			throw new RuntimeException("get connection error!", e);
		}
		return this.connection;
	}

	public String getDatabaseName(){
		String dbname = null;
		Connection conn = null;
		try {
			conn = getConnection();
			if(isMySQL())
				dbname = conn.getCatalog();
			else{
				DBConnection dbcon = DBConnection.Create(conn);
				dbname = (String)dbcon.unique("select distinct TABLESPACE_NAME from tabs", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DBUtils.closeCon(conn);
		}
		
		return dbname;
	}

	public DatabaseMetaData getMetaData() {
		return DBUtils.getMetaData(getConnection());
	}
	
	public void setDbconfigPath(String dbconfig) {
		PropConfig config = PropUtils.loadPropConfig(dbconfig);
		setDbconfig((Properties)config.getConfig());
	}

	public void setDbconfig(Properties dbconfig) {
		this.dbconfig = dbconfig;
		setNotInit();
	}

	public Properties getDbconfig() {
		if (dbconfig == null)
			dbconfig = new Properties();
		return dbconfig;
	}

	@Override
	public boolean isConnectSuccess() {
		Connection con = getConnection();
		try{
			if(con!=null)
				return true;
			else
				return false;
		}finally{
			DBUtils.closeCon(con);
		}
	}

	public void setUser(String user) {
		getDbconfig().setProperty(DBKeys.db_username, user);
		setNotInit();
	}

	public String getUser() {
		return getDbconfig().getProperty(DBKeys.db_username);
	}

	public void setPassword(String password) {
		getDbconfig().setProperty(DBKeys.db_password, password);
	}

	public String getPassword() {
		return getDbconfig().getProperty(DBKeys.db_password);
	}

	public void setDataBaseHost(String dbhost) {
		getDbconfig().setProperty(DBKeys.host, dbhost);
		setNotInit();
	}

	public void setDataBasePort(String dbport) {
		getDbconfig().setProperty(DBKeys.port, dbport);
		setNotInit();
	}

	public void setDataBaseName(String dbname) {
		getDbconfig().setProperty(DBKeys.dbname, dbname);
	}

	public String getDataBaseType() {
		return getDbconfig().getProperty(DBKeys.type, DBKeys.mysql);
	}

	public String getJdbcDriver() {
		return getDbconfig().getProperty(DBKeys.jdbc_driver, DBKeys.mysql_driver);
	}
	
	public boolean isMySQL(){
		return DBKeys.mysql.equals(getDataBaseType());
	}
	
	public boolean isOracle(){
		String type = getDataBaseType();
		if(StringUtils.isNotBlank(type))
			return DBKeys.oracle.equals(type);
		type = getJdbcDriver();
		return DBKeys.oralce_driver.equals(type);
	}

	public void setDataBaseType(String type) {
		if (!DBKeys.dbtypes.containsKey(type))
			throw new ServiceException("unsupported database : " + type);
		getDbconfig().setProperty(DBKeys.type, type);
		if (DBKeys.mysql.equals(type.toLowerCase())) {
			setDataBasePort("3306");
		} else if (DBKeys.oracle.equals(type.toLowerCase())) {
			setDataBasePort("1521");
		}
		setNotInit();
	}

	public static void main(String[] args) {
		DefaultDatabaseManage dm = new DefaultDatabaseManage();
		dm.setDataBaseHost("localhost");
		dm.setDataBaseType("mysql");
		dm.setUser("root");
		dm.setPassword("712");
		dm.initDatabaseManager();
		boolean rs = dm.isConnectSuccess();
		System.out.println("connect mysql: " + rs);
		
		DefaultDatabaseManage dm2 = new DefaultDatabaseManage();
		dm2.setDataBaseHost("orcl.ciipp.com");
		dm2.setDataBaseType("oracle");
		dm2.setDataBasePort("1521");
		dm2.setDataBaseName("orcl.ciipp.com");
		dm2.setUser("yoyob2b");
		dm2.setPassword("yoyob2b123");
		dm2.initDatabaseManager();
		boolean rs2 = dm2.isConnectSuccess();
		System.out.println("connect oracle["+""+"] : " + rs2);
	}
}

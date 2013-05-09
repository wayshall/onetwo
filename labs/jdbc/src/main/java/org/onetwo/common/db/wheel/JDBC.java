package org.onetwo.common.db.wheel;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.db.wheel.access.AccessWheel;
import org.onetwo.common.db.wheel.mysql.MySQLWheel;
import org.onetwo.common.db.wheel.oracle.OracleWheel;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;

public class JDBC {

	private static class DataSourceConnectionCreator implements ConnectionCreator, WheelAware {
		private DataSource dataSource;
		private Wheel wheel;
		
		DataSourceConnectionCreator(DataSource ds){
			this.dataSource = ds;
		}
		
		@Override
		public DBConnection getConnection() {
			try {
				Connection conn = dataSource.getConnection();
				DBConnection dbcon = DBConnection.Create(conn);
				if(wheel!=null)
					wheel.awareWheel(dbcon);
				return dbcon;
			} catch (SQLException e) {
				LangUtils.throwBaseException(e);
			}
			return null;
		}

		@Override
		public void setWheel(Wheel wheel) {
			this.wheel = wheel;
		}
		
	}
	private static class DatabaseManagerConnectionCreator implements ConnectionCreator, WheelAware {
		private DatabaseManager dm;
		private Wheel wheel;
		
		public DatabaseManagerConnectionCreator(DatabaseManager dm){
			this.dm = dm;
		}
		
		@Override
		public DBConnection getConnection() {
			Connection conn = dm.getConnection();
			LangUtils.println(true, "getConnection : " + conn);
			DBConnection dbcon = DBConnection.Create(conn);
			if(wheel!=null)
				wheel.awareWheel(dbcon);
			return dbcon;
		}

		@Override
		public void setWheel(Wheel wheel) {
			this.wheel = wheel;
		}
		
	}
	
	private static final JDBC instance = new JDBC();
	
	private static final Map<String, Class<? extends Wheel>> WHEELS;
	
	static {
		 Map<String, Class<? extends Wheel>> temp = new HashMap<String, Class<? extends Wheel>>();
		 temp.put("default", Wheel.class);
		 temp.put("mysql", MySQLWheel.class);
		 temp.put("mysql5", MySQLWheel.class);
		 temp.put("oracle", OracleWheel.class);
		 temp.put("access", AccessWheel.class);
		 WHEELS = Collections.unmodifiableMap(temp);
	}
	

	/*public synchronized static void init(Wheel wheel){
		instance.setWheel(wheel);
		instance.initJDBC();
	}*/
	
	public static Config conf(){
		return instance.config();
	}
	
	public synchronized static void init(DatabaseManager dm){
		init(new DatabaseManagerConnectionCreator(dm));
	}
	

	public synchronized static void init(DataSource ds){
		init(new DataSourceConnectionCreator(ds));
	}

	public synchronized static void init(ConnectionCreator creator){
		Wheel wheel = createWheel(creator);
		instance.setWheel(wheel);
//		instance.initJDBC();
		instance.jdao = createJDao(wheel);
		instance.setInitialized(true);
	}


	public static JDao createJDao(DatabaseManager dm){
		return createJDao(new DatabaseManagerConnectionCreator(dm));
	}


	public static JDao createJDao(DataSource ds){
		return createJDao(new DataSourceConnectionCreator(ds));
	}

	public static JDao createJDao(ConnectionCreator creator){
		Wheel wheel = createWheel(creator);
		return createJDao(wheel);
	}


	public static Wheel createWheel(ConnectionCreator creator){
		DBConnection dbcon = creator.getConnection();
		Wheel wheel = null;
		try {
			DatabaseMetaData meta = dbcon.getConnection().getMetaData();
			String dbName = meta.getDatabaseProductName();
			String version = meta.getDatabaseProductVersion();
			LangUtils.println("dbName:${0}, version:${1}", dbName, version);

			String key = dbName.toLowerCase();
			if(StringUtils.isNotBlank(version)){
				int first = version.indexOf('.');
				key = dbName+version.substring(0, first);
				wheel = getWheel(key);
				if(wheel==null)
					wheel = getWheel(dbName);
			}else{
				wheel = getWheel(key);
			}
			if(wheel==null){
				System.err.println("can not find the wheel for database["+dbName+"], use the default wheel.");
				wheel = getWheel(null);
			}
			wheel.init(creator, null);
		} catch (Exception e) {
			LangUtils.throwBaseException("initialize error : " + e.getMessage() , e);
		} finally{
			dbcon.close();
		}
		return wheel;
	}
	
	public static JDao createJDao(Wheel wheel){
		Assert.notNull(wheel);
		if(!wheel.hasInitialize()){
			LangUtils.throwBaseException("the wheel has not initialze : " + wheel);
		}
		JDao jdao = JDao.create(wheel);
		return jdao;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Wheel getWheel(String db){
		if(StringUtils.isBlank(db)){
			db = "default";
		}
		db = db.toLowerCase();
		Class cls = WHEELS.get(db);
		if(cls==null){
			for(Entry<String, Class<? extends Wheel>> entry : WHEELS.entrySet()){
				if(db.indexOf(entry.getKey())!=-1){
					cls = entry.getValue();
					break;
				}
			}
		}
		if(cls==null)
			LangUtils.throwBaseException("can not find wheel for database: " + db);
		return ReflectUtils.newInstance(cls);
	}

	public static JDBC inst() {
		if(!instance.isInitialized())
			LangUtils.throwBaseException("not init!");
		return instance;
	}

	public static class Config {
		private int batchSize = 1000;
		private boolean hasTransaction = true;
		private boolean debug = true;

		public int batchSize() {
			return batchSize;
		}
		public Config batchSize(int size) {
			batchSize=size;
			return this;
		}
		public boolean isBatch() {
			return batchSize>0;
		}
		public boolean hasTransaction() {
			return hasTransaction;
		}
		public Config hasTransaction(boolean hasTransaction) {
			this.hasTransaction = hasTransaction;
			return this;
		}
		public boolean isDebug() {
			return debug;
		}
		public Config debug(boolean debug) {
			this.debug = debug;
			return this;
		}
		public boolean isProduct() {
			return !isDebug();
		}
	}

	private boolean initialized;
	
	private Wheel wheel;
	private JDao jdao;
	private Config config;
	
//	private Map<String, JDao> jdaos;
	

	private JDBC() {
	}
	
	/*public void initConfig(Config config){
		this.config = config;
	}*/
	
	/********
	 * must call
	 */
	/*private void initJDBC(){
		Assert.notNull(wheel);
		if(!wheel.hasInitialize()){
			LangUtils.throwBaseException("the wheel has not initialze : " + wheel);
		}
		this.jdao = JDao.create(wheel);
		this.setInitialized(true);
	}*/

	public Wheel wheel() {
		return wheel;
	}

	public void setWheel(Wheel wheel) {
		this.wheel = wheel;
	}

	public JDao jdao() {
		return jdao;
	}

	public boolean isDebug() {
		return config().isDebug();
	}

	public boolean isProduct() {
		return config().isDebug();
	}
	
	public Config config(){
		if(config==null)
			config = wheel.config();
		return config;
	}


	public boolean isInitialized() {
		return initialized;
	}


	protected void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
	
}

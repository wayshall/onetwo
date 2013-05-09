package org.onetwo.common.db.wheel;

import java.util.Map;

import org.onetwo.common.db.ExtQuery;
import org.onetwo.common.db.ExtQueryImpl;
import org.onetwo.common.db.ParamValues.PlaceHolder;
import org.onetwo.common.db.sqlext.DefaultSQLDialetImpl;
import org.onetwo.common.db.sqlext.DefaultSQLSymbolManagerImpl;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.db.wheel.JDBC.Config;
import org.onetwo.common.utils.ReflectUtils;

@SuppressWarnings("rawtypes")
public class Wheel {
	
	public static class WheelSQLSymbolManagerImpl extends DefaultSQLSymbolManagerImpl implements WheelAware {
		private Wheel wheel;
		
		public WheelSQLSymbolManagerImpl() {
			super(new DefaultSQLDialetImpl(), PlaceHolder.POSITION);
		}

		@Override
		public ExtQuery createQuery(Class<?> entityClass, String alias, Map<Object, Object> properties) {
			ExtQuery q = new WheelExtQueryImpl(wheel, entityClass, alias, properties, this);
			return q;
		}

		public ExtQuery createQueryByObject(Object entity, Map<Object, Object> properties) {
			ExtQuery q = new WheelExtQueryImpl(wheel, entity, "ent", properties, this);
			return q;
		}

		@Override
		public void setWheel(Wheel wheel) {
			this.wheel = wheel;
		}
		
	}
	
	public static class WheelExtQueryImpl extends ExtQueryImpl implements WheelAware {

		private Wheel wheel;
		private TableInfo tableInfo;
		
		public WheelExtQueryImpl(Wheel wheel, Object entityClass, String alias, Map params, SQLSymbolManager symbolManager) {
			super((entityClass instanceof Class)?(Class)entityClass:entityClass.getClass(), alias, params, symbolManager);
			this.wheel = wheel;
			tableInfo = wheel.getTableInfoBuilder().buildTableInfo(entityClass);
		}

		@Override
		public void setWheel(Wheel wheel) {
			this.wheel = wheel;
		}

		protected String getSelectFromName(Class<?> entityClass){
			return tableInfo.getName();
		}
		
		protected String getDefaultSelectFields(Class<?> entityClass, String alias){
			return "*";
		}

		public Wheel getWheel() {
			return wheel;
		}
		
	}
	
	/*public static Wheel New(ConnectionCreator creator){
		return new Wheel().init(creator);
	}*/
	
	private ConnectionCreator creator;
	private TableInfoBuilder tableInfoBuilder;
	private SQLBuilderFactory sqlBuilderFactory;
	private EntityBuilderFactory entityBuilderFactory;
	
	private EntityOperationFactory entityOperationFactory;
	private ConnectionExecutor executor;
	
	private boolean initialize;

	private Map<Class, DBValueHanlder> dbValueHandlers;
	private PreparedStatementSetter preparedStatementSetter;
	
	private Map<Integer, Class> sqlToJavaMapper;
	private Map<Class, Integer> javaToSqlMapper;
	

	private WheelSQLSymbolManagerImpl sqlSymbolManager;
	

	private Config config;

	public Wheel(){
	}
	
	public Wheel init(ConnectionCreator creator, Config config){
		this.creator = awareWheel(creator);
		this.executor = createConnectionExecutor();
		this.tableInfoBuilder = this.createTableInfoBuilder();
		this.sqlBuilderFactory = this.createSQLBuilderFactory();
		
		this.entityBuilderFactory = createEntityBuilderFactory(tableInfoBuilder, sqlBuilderFactory);
		this.entityOperationFactory = createEntityOperationFactory(entityBuilderFactory);
		
		this.dbValueHandlers = createDBValueHandlers();
		this.preparedStatementSetter = createPreparedStatementSetter();
		this.sqlToJavaMapper = mapSqlTypeToJavaType();
		this.javaToSqlMapper = mapJavaTypeToSqlType();
		
		this.sqlSymbolManager = createSQLSymbolManager();

		if(config==null)
			this.config = new Config();
		
		initialize = true;
		return this;
	}

	public Config config(){
		if(config==null)
			config = new Config();
		return config;
	}
	
	protected WheelSQLSymbolManagerImpl createSQLSymbolManager(){
		WheelSQLSymbolManagerImpl sqlSymbolManager = new WheelSQLSymbolManagerImpl();
		awareWheel(sqlSymbolManager);
		return sqlSymbolManager;
	}
	
	protected ConnectionExecutor createConnectionExecutor(){
		ConnectionExecutor executor = new DefaultConnectionExecutor(config().hasTransaction());
		awareWheel(executor);
		return executor;
	}

	protected Map<Class, DBValueHanlder> createDBValueHandlers(){
		return DBUtils.DBVALUE_HANDLERS;
	}

	protected Map<Integer, Class> mapSqlTypeToJavaType(){
		return SqlTypeFactory.SQL_TYPE_TO_JAVA_TYPE;
	}

	protected Map<Class, Integer> mapJavaTypeToSqlType(){
		return SqlTypeFactory.BASIC_TYPES;
	}
	
	protected TableInfoBuilder createTableInfoBuilder(){
		return new EntityTableInfoBuilder(config.isProduct());
	}
	
	protected SQLBuilderFactory createSQLBuilderFactory(){
		return new DefaultSQLBuilderFactory();
	}
	
	protected EntityBuilderFactory createEntityBuilderFactory(TableInfoBuilder tableInfoBuilder, SQLBuilderFactory sqlBuilderFactory){
		return awareWheel(new DefaultEntityBuilderFactory(tableInfoBuilder, sqlBuilderFactory));
	}
	
	protected EntityOperationFactory createEntityOperationFactory(EntityBuilderFactory entityBuilderFactory){
		return new DefaultEntityOperationFactory(entityBuilderFactory);
	}
	
	public ConnectionCreator getConnectionCreator(){
		return creator;
	}
	
	public void setConnectionCreator(ConnectionCreator connectionCreator){
		this.creator = connectionCreator;
	}
	
	public ConnectionExecutor getConnectionExecutor(){
		return executor;
	}
	
	public void setConnectionExecutor(ConnectionExecutor connectionExecutor){
		this.executor = connectionExecutor;
	}
	
	public EntityOperationFactory getEntityOperationFactory(){
		return entityOperationFactory;
	}
	
	public void setEntityCallbackFactory(EntityOperationFactory entityCallbackFactory){
		this.entityOperationFactory = entityCallbackFactory;
	}
	
	public PreparedStatementSetter createPreparedStatementSetter(){
		return awareWheel(new DBValueHandlerSetter());
	}
	
	public PreparedStatementSetter getPreparedStatementSetter(){
		return this.preparedStatementSetter;
	}

	public boolean hasInitialize() {
		return initialize;
	}
	
	public DBValueHanlder getValueHandler(Class<?> clazz){
		if(!dbValueHandlers.containsKey(clazz)){
			clazz = Object.class;
		}
		return dbValueHandlers.get(clazz);
	}
	
	public int getSqlType(Class<?> cls){
		Integer type = this.javaToSqlMapper.get(cls);
		if(type==null){
			type = new Integer(DBUtils.TYPE_UNKNOW);
		}
		return type.intValue();
	}
	
	public Class getJavaType(int sqlType){
		Class clz = this.sqlToJavaMapper.get(sqlType);
		return clz;
	}
	
	public <T> T createComponent(Class<T> clazz){
		T bean = ReflectUtils.newInstance(clazz);
		awareWheel(bean);
		return bean;
	}
	
	public <T> T awareWheel(T bean){
		if(bean instanceof WheelAware){
			((WheelAware)bean).setWheel(this);
		}
		return bean;
	}

	public WheelSQLSymbolManagerImpl getSqlSymbolManager() {
		return sqlSymbolManager;
	}

	public TableInfoBuilder getTableInfoBuilder() {
		return tableInfoBuilder;
	}
	

	/*public DBConnection Create(DataSource ds){
		try {
			DBConnection dbcon = new DBConnection(ds.getConnection());
			awareWheel(dbcon);
			return dbcon;
		} catch (Exception e) {
			LangUtils.throwBaseException("create connection error.", e);
		}
		return null;
	}*/
	
}

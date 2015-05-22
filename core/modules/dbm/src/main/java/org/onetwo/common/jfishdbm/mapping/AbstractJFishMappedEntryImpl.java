package org.onetwo.common.jfishdbm.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.IBaseEntity;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.jfishdbm.annotation.JFishEntityListeners;
import org.onetwo.common.jfishdbm.annotation.JFishFieldListeners;
import org.onetwo.common.jfishdbm.event.JFishEntityFieldListener;
import org.onetwo.common.jfishdbm.event.JFishEntityListener;
import org.onetwo.common.jfishdbm.event.JFishEventAction;
import org.onetwo.common.jfishdbm.exception.JFishOrmException;
import org.onetwo.common.jfishdbm.mapping.SQLBuilderFactory.SqlBuilderType;
import org.onetwo.common.jfishdbm.utils.JFishdbUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.AnnotationInfo;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.slf4j.Logger;

@SuppressWarnings({"unchecked", "rawtypes"})
abstract public class AbstractJFishMappedEntryImpl implements JFishMappedEntry {
	public static final Comparator SORT_BY_LENGTH = new Comparator<AbstractMappedField>() {

		@Override
		public int compare(AbstractMappedField o1, AbstractMappedField o2) {
			return (o1.getName().length()-o2.getName().length());
		}
		
	};
	
	private Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private Class entityClass;
	private String entityName;
	private boolean dynamic;
	private TableInfo tableInfo;
//	private boolean queryableOnly;
	private AnnotationInfo annotationInfo;

	private Map<String, AbstractMappedField> mappedFields = new LinkedHashMap<String, AbstractMappedField>();
	private Map<String, AbstractMappedField> mappedColumns = new HashMap<String, AbstractMappedField>();
	private JFishMappedField identifyField;
	private JFishMappedField versionField;
	
	private SQLBuilderFactory sqlBuilderFactory;
	
//	private DataHolder<String, Object> dataHolder = new DataHolder<String, Object>();

	private boolean freezing;
	
	private List<JFishEntityListener> entityListeners = Collections.EMPTY_LIST;
	private List<JFishEntityFieldListener> fieldListeners;
	
	public AbstractJFishMappedEntryImpl(AnnotationInfo annotationInfo) {
		this(annotationInfo, null);
	}
	
	public AbstractJFishMappedEntryImpl(AnnotationInfo annotationInfo, TableInfo tableInfo) {
		this.entityClass = annotationInfo.getSourceClass();
		this.annotationInfo = annotationInfo;
		this.entityName = this.entityClass.getName();
		this.tableInfo = tableInfo;
		if(Map.class.isAssignableFrom(entityClass)){
			this.setDynamic(true);
		}
		JFishEntityListeners listenersAnntation = annotationInfo.getAnnotation(JFishEntityListeners.class);
		if(listenersAnntation!=null){
			Class<? extends JFishEntityListener>[] listenerClasses = listenersAnntation.value();
			entityListeners = LangUtils.newArrayList(listenerClasses.length);
			for(Class<? extends JFishEntityListener> lcs : listenerClasses){
				if(lcs==null)
					continue;
				JFishEntityListener listener = ReflectUtils.newInstance(lcs);
				entityListeners.add(listener);
			}
		}

		JFishFieldListeners fieldListenersAnntation = annotationInfo.getAnnotation(JFishFieldListeners.class);
		this.fieldListeners = JFishdbUtils.initJFishEntityFieldListeners(fieldListenersAnntation);
	}

	public Collection<AbstractMappedField> getFields(){
		return this.mappedFields.values();
	}
	
	public Collection<AbstractMappedField> getFields(JFishMappedFieldType... types){
		List<AbstractMappedField> flist = new ArrayList<AbstractMappedField>(mappedFields.values().size());
		if(LangUtils.isEmpty(types)){
			Collections.sort(flist, SORT_BY_LENGTH);
			return flist;
		}
		for(AbstractMappedField field : mappedFields.values()){
			if(ArrayUtils.contains(types, field.getMappedFieldType())){
				flist.add(field);
			}
		}
		Collections.sort(flist, SORT_BY_LENGTH);
		return flist;
	}
	
	public boolean isInstance(Object entity){
		return entityClass.isInstance(entity);
	}
	
	public boolean hasIdentifyValue(Object entity){
		return getId(entity)!=null;
	}
	
	void setTableInfo(TableInfo tableInfo) {
		this.tableInfo = tableInfo;
	}

	@Override
	public void setId(Object entity, Object value){
		getIdentifyField().setValue(entity, value);
	}
	
	@Override
	public Object getId(Object entity){
		if(getIdentifyField()==null)
			return null;
		return getIdentifyField().getValue(entity);
	}
	
	@Override
	public void setFieldValue(Object entity, String fieldName, Object value){
		getField(fieldName).setValue(entity, value);
	}
	
	@Override
	public JFishMappedField getField(String fieldName){
		JFishMappedField field = mappedFields.get(fieldName);
		if(field==null)
			throw new JFishOrmException(getEntityName()+" no field mapped : " + fieldName);
		return field;
	}
	
	@Override
	public boolean contains(String field){
		return this.mappedFields.containsKey(field);
	}
	
	@Override
	public boolean containsColumn(String col){
		Assert.hasText(col, "column name can not be empty");
		return this.mappedColumns.containsKey(col.toLowerCase());
	}
	
	@Override
	public String getColumnName(String field){
		return getField(field).getColumn().getName();
	}
	
	@Override
	public JFishMappedField getFieldByColumnName(String columnName){
		columnName = columnName.toLowerCase();
		JFishMappedField field = mappedColumns.get(columnName);
		if(field==null)
			throw new JFishOrmException(getEntityName() + " no column mapped : " + columnName);
		return field;
	}
	
	@Override
	public Object getFieldValue(Object entity, String fieldName){
		return getField(fieldName).getValue(entity);
	}
	
	protected void checkEntry(){
		if(getMappedType()!=MappedType.ENTITY)
			return ;
		if(getIdentifyField()==null){
			throw new BaseException("the entity must has a identify : " + getEntityClass());
		}
	}

	/*@Override
	public void build(TableInfo tableInfo) {
		this.checkEntry();
//		buildTableInfo();
		this.tableInfo = tableInfo;
		buildStaticSQL(tableInfo);
		
		for(AbstractMappedField field : this.mappedFields.values()){
			this.mappedColumns.put(field.getColumn().getName().toLowerCase(), field);
		}

		this.mappedFields = Collections.unmodifiableMap(this.mappedFields);
		this.mappedColumns = Collections.unmodifiableMap(this.mappedColumns);
	}*/

	/**********
	 * 此方法会延迟调用，会设置各种属性和manager的事件回调后，才会调用，
	 * 所以，如果没有实现扫描和构建所有实体，而在运行时才build，就要注意多线程的问题
	 */
	@Override
	public void buildEntry() {
		this.checkEntry();
		
		for(AbstractMappedField field : this.mappedFields.values()){
			if(field.getColumn()!=null)
				this.mappedColumns.put(field.getColumn().getName().toLowerCase(), field);
		}

		this.mappedFields = Collections.unmodifiableMap(this.mappedFields);
		this.mappedColumns = Collections.unmodifiableMap(this.mappedColumns);
		
//		buildTableInfo();
		buildStaticSQL(tableInfo);
		
//		freezing();
	}

	protected void buildStaticSQL(TableInfo taboleInfo){
	}
	
	public EntrySQLBuilder createSQLBuilder(SqlBuilderType type){
//		SQLBuilder sqlb = SQLBuilder.createNamed(tableInfo.getName(), tableInfo.getAlias(), type);
		EntrySQLBuilder sqlb = sqlBuilderFactory.createQMark(this, tableInfo.getAlias(), type);
		return sqlb;
	}
	
	public JdbcStatementContextBuilder createJdbcStatementContextBuilder(SqlBuilderType type){
		EntrySQLBuilder sb = sqlBuilderFactory.createQMark(this, this.getTableInfo().getAlias(), type);
		JdbcStatementContextBuilder sqlb = JdbcStatementContextBuilder.create(null, this, sb);
		return sqlb;
	}
	
	@Override
	public JFishMappedEntry addMappedField(AbstractMappedField field){
		Assert.notNull(field);
		
		this.mappedFields.put(field.getName(), field);
		
		if(field.isIdentify()){
			this.identifyField = field;
		}else if(field.isVersionControll()){
			if(versionField!=null)
				throw new JFishOrmException("a version field has already exist : " + versionField.getName());
			this.versionField = field;
		}

		/*if(field.isJoinTable()){
			addJoinableField((JoinableMappedField)field);
		}*/
		/*if(field.getMappedFieldType()==JFishMappedFieldType.TO_ONE){
			Assert.isInstanceOf(CascadeMappedField.class, field);
			addToOneField((CascadeMappedField)field);
			
		}else if(field.getMappedFieldType()==JFishMappedFieldType.ONE_TO_MANY){
			Assert.isInstanceOf(CascadeCollectionMappedField.class, field);
			addOneToManyField((CascadeCollectionMappedField)field);
			
		}else if(field.getMappedFieldType()==JFishMappedFieldType.MANY_TO_MANY){
			Assert.isInstanceOf(CascadeCollectionMappedField.class, field);
			addManyToManyField((CascadeCollectionMappedField)field);
		}*/
		
		return this;
	}
	
	/*public void addToOneField(CascadeMappedField manyToOne){
		throw new UnsupportedOperationException("unsupported this operation : " + getClass());
	}
	
	public void addOneToManyField(CascadeCollectionMappedField oneToMany){
		throw new UnsupportedOperationException("unsupported this operation : " + getClass());
	}
	
	public void addManyToManyField(CascadeCollectionMappedField oneToMany){
		throw new UnsupportedOperationException("unsupported this operation : " + getClass());
	}*/
	
	/*public void addManyToManyField(AbstractMappedField oneToMany){
		throw new UnsupportedOperationException("unsupported this operation : " + getClass());
	}*/
	
	/*public void addJoinableField(JoinableMappedField joinalbe){
		throw new UnsupportedOperationException("unsupported this operation : " + getClass());
	}*/

	@Override
	public Class<?> getEntityClass() {
		return entityClass;
	}

	@Override
	public <T> T newInstance() {
		return (T)ReflectUtils.newInstance(entityClass);
	}

	@Override
	public boolean isDynamic() {
		return dynamic;
	}

	@Override
	public void setDynamic(boolean dynamic) {
		this.checkFreezing("setDynamic");
		this.dynamic = dynamic;
	}

	@Override
	public TableInfo getTableInfo() {
		return tableInfo;
	}

	@Override
	public JFishMappedField getIdentifyField() {
		return identifyField;
	}

	@Override
	public JFishMappedField getVersionField() {
		return versionField;
	}

	protected void throwIfQueryableOnly(){
		if(isQueryableOnly()){
			LangUtils.throwBaseException("this entity is only for query  : " + this.entityClass);
		}
	}

	abstract protected EntrySQLBuilder getStaticInsertSqlBuilder();
	abstract protected EntrySQLBuilder getStaticUpdateSqlBuilder();
	abstract protected EntrySQLBuilder getStaticDeleteSqlBuilder();
	abstract protected EntrySQLBuilder getStaticFetchSqlBuilder();
	abstract protected EntrySQLBuilder getStaticFetchAllSqlBuilder();
	abstract protected EntrySQLBuilder getStaticSelectVersionSqlBuilder();
	
	protected EntrySQLBuilder getStaticDeleteAllSqlBuilder(){
		throw new UnsupportedOperationException("the "+getMappedType()+" entity unsupported this operation!");
	}

	@Override
	public JdbcStatementContext<Object[]> makeFetchAll(){
		EntrySQLBuilder sqlb = getStaticFetchAllSqlBuilder();
		sqlb.build();
//		KVEntry<String, Object[]> kv = new KVEntry<String, Object[]>(sql, (Object[])null);
		JdbcStatementContext<Object[]> kv = SimpleJdbcStatementContext.create(sqlb, (Object[])null);
		return kv;
	}

	@Override
	public JdbcStatementContext<Object[]> makeDeleteAll(){
		EntrySQLBuilder sqlb = getStaticDeleteAllSqlBuilder();
		sqlb.build();
//		KVEntry<String, Object[]> kv = new KVEntry<String, Object[]>(sql, (Object[])null);
		JdbcStatementContext<Object[]> kv = SimpleJdbcStatementContext.create(sqlb, (Object[])null);
		return kv;
	}
	

	@Override
	public JdbcStatementContext<Object[]> makeSelectVersion(Object object){
		if(LangUtils.isMultiple(object)){
			throw new JFishOrmException("not a entity: " + object);
		}
		EntrySQLBuilder sqlb = getStaticSelectVersionSqlBuilder();
		sqlb.build();
		JdbcStatementContext<Object[]> kv = SimpleJdbcStatementContext.create(sqlb, new Object[]{getId(object), getVersionField().getValue(object)});
		return kv;
	}
	
	@Override
	public JdbcStatementContext<List<Object[]>> makeFetch(Object objects, boolean isIdentify){
		JdbcStatementContextBuilder dsb = JdbcStatementContextBuilder.create(JFishEventAction.find, this, getStaticFetchSqlBuilder());
		
		if(LangUtils.isMultiple(objects)){
			List<Object> list = LangUtils.asList(objects);
			for(Object id : list){
				if(isIdentify)
					dsb.addCauseValue(id);
				else
					dsb.processWhereCauseValuesFromEntity(id);
				dsb.addBatch();
			}
		}else{
			if(isIdentify)
				dsb.addCauseValue(objects);
			else
				dsb.processWhereCauseValuesFromEntity(objects);
		}
		
		dsb.build();
//		KVEntry<String, List<Object[]>> kv = new KVEntry<String, List<Object[]>>(dsb.getSql(), dsb.getValues());
		JdbcStatementContext<List<Object[]>> kv = SimpleJdbcStatementContext.create(dsb.getSqlBuilder(), dsb.getValue());
		return kv;
	}
	
	@Override
	public JdbcStatementContext<List<Object[]>> makeInsert(Object entity){
		this.throwIfQueryableOnly();
		EntrySQLBuilder insertSqlBuilder = getStaticInsertSqlBuilder();
		JdbcStatementContextBuilder dsb = JdbcStatementContextBuilder.create(JFishEventAction.insert, this, insertSqlBuilder);
		if(LangUtils.isMultiple(entity)){
			List<Object> list = LangUtils.asList(entity);
			if(LangUtils.isEmpty(list))
				return null;
			for(Object en : list){
				this.processIBaseEntity(en, true);
//				dsb.setColumnValuesFromEntity(en).addBatch();
//				doEveryMappedFieldInStatementContext(dsb, en).addBatch();
				dsb.processColumnValues(en).addBatch();
			}
		}else{
			this.processIBaseEntity(entity, true);
//			dsb.setColumnValuesFromEntity(entity);
			dsb.processColumnValues(entity);
		}
		dsb.build();
//		KVEntry<String, List<Object[]>> kv = new KVEntry<String, List<Object[]>>(dsb.getSql(), dsb.getValues());
//		JdbcStatementContext<List<Object[]>> context = SqlBuilderJdbcStatementContext.create(dsb.getSql(), dsb); 
		return dsb;
	}
	
/*	private JdbcStatementContextBuilder doEveryMappedFieldInStatementContext(JdbcStatementContextBuilder statement, Object entity){
		Assert.notNull(entity);
		Object val = null;
		EntrySQLBuilder builder = statement.getSqlBuilder();
		for(JFishMappedField field : builder.getFields()){
			val = field.getColumnValueWithJFishEventAction(entity, statement.getEventAction());
			if(field.isVersionControll()){
				if(JFishEventAction.insert==statement.getEventAction()){
					val = field.getVersionValule(val);
					field.setValue(entity, val);//write the version value into the entity
				}else if(JFishEventAction.update==statement.getEventAction()){
					Assert.notNull(val, "version field["+field.getName()+"] can't be null: " + getEntityName());
					val = field.getVersionValule(val);
					field.setValue(entity, val);a
				}
			}
			statement.putColumnValue(field, val);
		}
		return statement;
	}*/
	
	private void processIBaseEntity(Object entity, boolean create){
		if(!(entity instanceof IBaseEntity))
			return ;
		Date now = new Date();
		IBaseEntity ib = (IBaseEntity) entity;
		if(create){
			ib.setCreateTime(now);
		}
		ib.setLastUpdateTime(now);
	}
	
	@Override
	public JdbcStatementContext<List<Object[]>> makeDelete(Object objects, boolean isIdentify){
		this.throwIfQueryableOnly();
		
		JdbcStatementContextBuilder dsb = JdbcStatementContextBuilder.create(JFishEventAction.delete, this, getStaticDeleteSqlBuilder());
		
		if(LangUtils.isMultiple(objects)){
			List<Object> list = LangUtils.asList(objects);
			for(Object obj : list){
				if(isIdentify){
					Object idValue = obj;
					if(isInstance(idValue)){
						idValue = getId(idValue);
					}
					dsb.addCauseValue(idValue);
				}else{
					dsb.processWhereCauseValuesFromEntity(obj);
				}
				dsb.addBatch();
			}
		}else{
			if(isIdentify){
				Object idValue = objects;
				if(isInstance(idValue)){
					idValue = getId(idValue);
				}
				dsb.addCauseValue(idValue);
			}else{
				dsb.processWhereCauseValuesFromEntity(objects);
			}
		}
		
		dsb.build();
//		KVEntry<String, List<Object[]>> kv = new KVEntry<String, List<Object[]>>(dsb.getSql(), dsb.getValues());
//		JdbcStatementContext<List<Object[]>> kv = SimpleJdbcStatementContext.create(dsb.getSqlBuilder(), dsb.getValue());
		return dsb;
	}
	
	@Override
	public JdbcStatementContext<List<Object[]>> makeUpdate(Object entity){
		this.throwIfQueryableOnly();
		
		JdbcStatementContextBuilder dsb = JdbcStatementContextBuilder.create(JFishEventAction.update, this, getStaticUpdateSqlBuilder());
		
		if(LangUtils.isMultiple(entity)){
			List<Object> list = LangUtils.asList(entity);
			for(Object en : list){
				this.checkIdValue(en);
				this.processIBaseEntity(en, false);
//				dsb.setColumnValuesFromEntity(en)
				dsb.processColumnValues(en)
					.processWhereCauseValuesFromEntity(en)
					.addBatch();
			}
		}else{
			this.checkIdValue(entity);
			this.processIBaseEntity(entity, false);
//			dsb.setColumnValuesFromEntity(entity);
			dsb.processColumnValues(entity);
			dsb.processWhereCauseValuesFromEntity(entity);
		}
		
		dsb.build();
//		KVEntry<String, List<Object[]>> kv = new KVEntry<String, List<Object[]>>(dsb.getSql(), dsb.getValues());
//		JdbcStatementContext<List<Object[]>> kv = SimpleJdbcStatementContext.create(dsb.getSqlBuilder(), dsb.getValue());
		return dsb;
	}
	
	protected void checkIdValue(Object entity){
		if(!hasIdentifyValue(entity))
			throw new JFishOrmException("entity["+entity+"] id can not be null");
	}
	
	@Override
	public JdbcStatementContext<List<Object[]>> makeDymanicUpdate(Object entity){//single entity
		this.throwIfQueryableOnly();

		this.checkIdValue(entity);
		this.processIBaseEntity(entity, false);
		JdbcStatementContextBuilder dsb = makeDymanicUpdateJdbcStatementContextBuilder(entity);
		dsb.build();
//		KVEntry<String, List<Object[]>> kv = new KVEntry<String, List<Object[]>>(dsb.getSql(), dsb.getValues());
//		JdbcStatementContext<List<Object[]>> kv = SimpleJdbcStatementContext.create(dsb);
		return dsb;
	}

	protected JdbcStatementContextBuilder makeDymanicUpdateJdbcStatementContextBuilder(Object entity){
		EntrySQLBuilder sb = sqlBuilderFactory.createQMark(this, this.getTableInfo().getAlias(), SqlBuilderType.update);
		JdbcStatementContextBuilder sqlBuilder = JdbcStatementContextBuilder.create(JFishEventAction.update, this, sb);
		Object val = null;
		for(JFishMappedField mfield : this.mappedColumns.values()){
			
//			val = mfield.getColumnValue(entity);
			val = mfield.getColumnValueWithJFishEventAction(entity, JFishEventAction.update);
			if(mfield.isIdentify()){
				Assert.notNull(val, "id can not be null : " + entity);
				sqlBuilder.appendWhere(mfield, val);
			}else{
				if(mfield.isVersionControll()){
					Assert.notNull(val, "version field["+mfield.getName()+"] can not be null : " + entity);
					sqlBuilder.appendWhere(mfield, val);
					val = mfield.getVersionableType().getVersionValule(val);
				}
				if(val!=null)
					sqlBuilder.append(mfield, val);
			}
		}
		return sqlBuilder;
	}
/*
	public Object getColumnValueWithJFishEventAction(JFishMappedField field, Object entity, JFishEventAction eventAction){
		Object oldValue = field.getColumnValue(entity);
		Object newValue = oldValue;
		boolean doListener = false;
		if(JFishEventAction.insert==eventAction){
			if(!field.getFieldListeners().isEmpty()){
				for(JFishEntityFieldListener fl : field.getFieldListeners()){
					newValue = fl.beforeFieldInsert(field.getName(), newValue);
					doListener = true;
				}
			}
		}else if(JFishEventAction.update==eventAction){
			if(!field.getFieldListeners().isEmpty()){
				for(JFishEntityFieldListener fl : field.getFieldListeners()){
					newValue = fl.beforeFieldUpdate(field.getName(), newValue);
					doListener = true;
				}
			}
		}
		if(doListener){
			field.setColumnValue(entity, newValue);
		}
		return newValue;
	}*/
	
	@Override
	public Map<String, AbstractMappedField> getMappedFields() {
		return mappedFields;
	}
	

	public Map<String, AbstractMappedField> getMappedColumns() {
		return mappedColumns;
	}

	//	@Override
	public boolean isQueryableOnly() {
		return getMappedType()==MappedType.QUERYABLE_ONLY;
	}

	@Override
	public MappedType getMappedType() {
		return MappedType.ENTITY;
	}

	@Override
	public boolean isJoined() {
		return getMappedType()==MappedType.JOINED;
	}

	@Override
	public boolean isEntity() {
		return getMappedType()==MappedType.ENTITY;
	}

	public String getEntityName() {
		return entityName;
	}

	public AnnotationInfo getAnnotationInfo() {
		return annotationInfo;
	}

	public void freezing() {
		for(JFishMappedField field : mappedFields.values()){
			field.freezing();
		}
		freezing = true;
		logger.info("mapped entry["+getEntityName()+"] has built and freezing!");
	}

	protected void checkFreezing(String name) {
		if(isFreezing()){
			throw new UnsupportedOperationException("the entry["+getEntityName()+"] is freezing now, don not supported this operation : " + name);
		}
	}

	public boolean isFreezing() {
		return freezing;
	}

	public SQLBuilderFactory getSqlBuilderFactory() {
		return sqlBuilderFactory;
	}

	public void setSqlBuilderFactory(SQLBuilderFactory sqlBuilderFactory) {
		this.sqlBuilderFactory = sqlBuilderFactory;
	}

	/*public DataHolder<String, Object> getDataHolder() {
		return dataHolder;
	}*/
	
	public String toString(){
		return LangUtils.append(getEntityName());
	}

	public List<JFishEntityListener> getEntityListeners() {
		return entityListeners;
	}

	public List<JFishEntityFieldListener> getFieldListeners() {
		return fieldListeners;
	}

	@Override
	public boolean isVersionControll() {
		return getVersionField()!=null;
	}

}

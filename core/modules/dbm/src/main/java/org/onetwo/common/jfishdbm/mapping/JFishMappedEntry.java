package org.onetwo.common.jfishdbm.mapping;

import java.util.List;
import java.util.Map;

import org.onetwo.common.jfishdbm.event.JFishEntityFieldListener;
import org.onetwo.common.jfishdbm.event.JFishEntityListener;
import org.onetwo.common.jfishdbm.mapping.SQLBuilderFactory.SqlBuilderType;

public interface JFishMappedEntry extends JFishMappedEntryMeta {

	public void setId(Object entity, Object value);

	public Object getId(Object entity);

	public void setFieldValue(Object entity, String fieldName, Object value);


	public String getColumnName(String field);


	public Object getFieldValue(Object entity, String fieldName);

	/*******
	 * 此方法会延迟调用，会设置各种属性和manager的事件回调后，才会调用，
	 * 所以，如果没有实现扫描和构建所有实体，而在运行时才build，就要注意多线程的问题
	 */
	public void buildEntry();


	

	public <T> T newInstance();

	public boolean isDynamic();

	public void setDynamic(boolean dynamic);



//	public String getStaticInsertSql();

//	public String getStaticUpdateSql();

//	public String getStaticFetchSql();

	public String getStaticSeqSql();
	
	public JdbcStatementContext<Object[]> makeSelectVersion(Object object);
	
	public JdbcStatementContext<Object[]> makeFetchAll();
	
	public JdbcStatementContext<Object[]> makeDeleteAll();
	
	public JdbcStatementContext<List<Object[]>> makeFetch(Object objects, boolean isIdentify);
	
	public JdbcStatementContext<List<Object[]>> makeInsert(Object entity);

	public JdbcStatementContext<List<Object[]>> makeDelete(Object objects, boolean isIdentify);

	public JdbcStatementContext<List<Object[]>> makeUpdate(Object entity);

	public JdbcStatementContext<List<Object[]>> makeDymanicUpdate(Object entity);

	public Map<String, AbstractMappedField> getMappedFields();

	/*public boolean isQueryableOnly();

	public void setQueryableOnly(boolean queryableOnly);*/
	
	/*public void addAnnotations(Annotation...annotations);
	
	public boolean hasAnnotation(Class<? extends Annotation> annoClass);
	
	public <T extends Annotation> T getAnnotation(Class<T> annoClass);*/
	
	public void freezing();
	public boolean isFreezing();
	
	public boolean hasIdentifyValue(Object entity);
//	public Collection<JoinableMappedField> getJoinMappedFields();
	
	public SQLBuilderFactory getSqlBuilderFactory();
	
//	public DataHolder<String, Object> getDataHolder();
	
	public EntrySQLBuilder createSQLBuilder(SqlBuilderType type);
	
	public JdbcStatementContextBuilder createJdbcStatementContextBuilder(SqlBuilderType type);
	
	public List<JFishEntityListener> getEntityListeners();
	public List<JFishEntityFieldListener> getFieldListeners();
	
}
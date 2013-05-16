package org.onetwo.common.db.sql;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.exception.ServiceException;

/***
 * 序列管理类
 * @author weishao
 *
 */
public class SequenceNameManager {
	
	
	public static final String CREATE_SEQUENCE_SQL = "database.sequence.sql";
	public static final String CREATE_SEQUENCE = "create sequence :sequenceName start with 100 increment by 1 maxvalue 999999999999999999999999999";

	/*private static SequenceNameManager instance = new SequenceNameManager();

	public static SequenceNameManager getInstance() {
		return instance;
	}*/

	protected Map<String, String> sequenceSqlCache = new ConcurrentHashMap<String, String>();
	
	public <T> String getSequenceName(Class<T> entityClass){
		String seqName = "SEQ_" + entityClass.getSimpleName().toUpperCase();
		if(StringUtils.isBlank(seqName))
			throw new ServiceException("can not find the sequence. class["+entityClass.getName()+"]");
		
		return seqName;
	}
	
	public <T> String getSequenceSql(Class<T> entityClass){
		String seqName = getSequenceName(entityClass);
		return getSequenceSql(seqName);
	}
	
	public <T> String getSequenceSql(String seqName){
		String cacheSql = sequenceSqlCache.get(seqName);
		if(StringUtils.isNotBlank(cacheSql)){
			return cacheSql;
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("select ").append(seqName).append(".nextval from dual");
		sequenceSqlCache.put(seqName, sql.toString());
		
		return sql.toString();
	}
	

	public String getCreateSequence(Class<?> entityClass){
		return getCreateSequence(getSequenceName(entityClass));
	}
	
	public String getCreateSequence(String sequenceName){
		String name = sequenceName;
//		String sql = sqlFile.getVariable(CREATE_SEQUENCE_SQL);
		String sql = CREATE_SEQUENCE;
		if(StringUtils.isBlank(sql))
			throw new ServiceException("sql is blank . can not create squence : " + name);
		sql = sql.replace(":sequenceName", name);
		return sql;
	}
}

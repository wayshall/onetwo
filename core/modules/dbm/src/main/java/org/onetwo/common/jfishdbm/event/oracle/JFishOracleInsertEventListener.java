package org.onetwo.common.jfishdbm.event.oracle;

import java.sql.SQLException;
import java.util.List;

import org.onetwo.common.convert.Types;
import org.onetwo.common.jfishdbm.event.JFishEventSource;
import org.onetwo.common.jfishdbm.event.JFishInsertEvent;
import org.onetwo.common.jfishdbm.event.JFishInsertEventListener;
import org.onetwo.common.jfishdbm.mapping.JFishMappedEntry;
import org.onetwo.common.jfishdbm.mapping.JdbcStatementContext;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.LangUtils;
import org.springframework.jdbc.BadSqlGrammarException;

public class JFishOracleInsertEventListener extends JFishInsertEventListener {

	protected void beforeDoInsert(JFishInsertEvent event, JFishMappedEntry entry){
		Object entity = event.getObject();

		if(entry.isEntity() && entry.getIdentifyField().isGeneratedValueFetchBeforeInsert()){
			Long id = null;
			if(LangUtils.isMultiple(entity)){
				List<Object> list = LangUtils.asList(entity);
				for(Object en : list){
					id = fetchIdentifyBeforeInsert(event, entry);
					entry.setId(en, id);
				}
			}else{
				id = fetchIdentifyBeforeInsert(event, entry);
				entry.setId(entity, id);
			}
		}
		
	}
	/********
	 * 不可以根据更新数量的数目来确定是否成功
	 * oracle如果使用了不符合jdbc3.0规范的本地批量更新机制（BatchPerformanceWorkaround=true），
	 * 每次插入的返回值都是{@linkplain java.sql.Statement#SUCCESS_NO_INFO -2}
	 */
	@Override
	protected void doInsert(JFishInsertEvent event, JFishMappedEntry entry) {
		JFishEventSource es = event.getEventSource();
		this.beforeDoInsert(event, entry);
		Object entity = event.getObject();
		JdbcStatementContext<List<Object[]>> insert = entry.makeInsert(entity);
		/*
		String sql = insert.getSql();
		List<Object[]> args = insert.getValue();
		
		int count = executeJdbcUpdate(event, sql, args, es);*/
		int count = this.executeJdbcUpdate(es, insert);
		
		event.setUpdateCount(count);
	}
	
	public Long fetchIdentifyBeforeInsert(JFishInsertEvent event, JFishMappedEntry entry){
		JFishEventSource es = event.getEventSource();
		Long id = null;
		try {
			id = es.getJFishJdbcTemplate().queryForObject(entry.getStaticSeqSql(), Long.class);
		} catch (BadSqlGrammarException e) {
			//ORA-02289: 序列不存在
			SQLException sqe = e.getSQLException();
			int vendorCode = Types.convertValue(ReflectUtils.getFieldValue(sqe, "vendorCode"), int.class);
			if(vendorCode==2289){
				es.getJFishJdbcTemplate().execute(entry.getStaticCreateSeqSql());
				id = es.getJFishJdbcTemplate().queryForObject(entry.getStaticSeqSql(), Long.class);
				if(id==null)
					throw e;
			}
		}
		return id;
	}

}

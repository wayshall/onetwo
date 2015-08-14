package org.onetwo.common.jfishdbm.query;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.filequery.QueryProvideManager;
import org.onetwo.common.spring.ftl.TemplateParser;
import org.onetwo.common.spring.sql.DefaultFileQueryImpl;
import org.onetwo.common.spring.sql.JFishNamedFileQueryInfo;
import org.onetwo.common.utils.Assert;
import org.springframework.jdbc.core.RowMapper;

public class JFishFileQueryImpl extends DefaultFileQueryImpl {

//	private JFishNamedFileQueryInfo info;
//	private QueryProvideManager baseEntityManager;
	

	public JFishFileQueryImpl(QueryProvideManager jfishFishDao, JFishNamedFileQueryInfo info, boolean count, TemplateParser parser) {
		super(jfishFishDao, info, count, parser);
		Assert.notNull(jfishFishDao);
//		this.baseEntityManager = jfishFishDao;
		
	}
	
	protected DataQuery createDataQuery(String sql, Class<?> mappedClass){
		DataQuery dataQuery = this.baseEntityManager.createSQLQuery(sql, mappedClass);
		return dataQuery;
	}


	public void setRowMapper(RowMapper<?> rowMapper) {
		this.getRawQuery(JFishDataQuery.class).getJfishQuery().setRowMapper(rowMapper);
	}



}

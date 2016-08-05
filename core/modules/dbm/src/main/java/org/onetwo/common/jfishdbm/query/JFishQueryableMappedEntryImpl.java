package org.onetwo.common.jfishdbm.query;

import java.util.Collection;

import org.onetwo.common.annotation.AnnotationInfo;
import org.onetwo.common.jfishdbm.mapping.AbstractJFishMappedEntryImpl;
import org.onetwo.common.jfishdbm.mapping.AbstractMappedField;
import org.onetwo.common.jfishdbm.mapping.EntrySQLBuilder;
import org.onetwo.common.jfishdbm.mapping.EntrySQLBuilderImpl;
import org.onetwo.common.jfishdbm.mapping.JFishMappedEntry;
import org.onetwo.common.jfishdbm.mapping.MappedType;
import org.onetwo.common.jfishdbm.mapping.SQLBuilderFactory.SqlBuilderType;
import org.onetwo.common.jfishdbm.mapping.TableInfo;
import org.onetwo.common.jfishdbm.support.SimpleDbmInnserServiceRegistry;

public class JFishQueryableMappedEntryImpl extends AbstractJFishMappedEntryImpl implements JFishMappedEntry {

	private EntrySQLBuilderImpl staticFetchSqlBuilder;
	

	public JFishQueryableMappedEntryImpl(AnnotationInfo annotationInfo, TableInfo tableInfo, SimpleDbmInnserServiceRegistry serviceRegistry) {
		super(annotationInfo, tableInfo, serviceRegistry);
	}
	
	protected void buildStaticSQL(TableInfo taboleInfo){
		Collection<AbstractMappedField> columns = getFields();
		
		staticFetchSqlBuilder = createSQLBuilder(SqlBuilderType.query);
		staticFetchSqlBuilder.setNamedPlaceHoder(false);
		staticFetchSqlBuilder.append(columns);
		if(taboleInfo.getPrimaryKey()!=null){
			staticFetchSqlBuilder.appendWhere(getIdentifyField());
		}
		staticFetchSqlBuilder.build();
	}
	/*
	@Override
	public String getStaticInsertSql() {
		return staticInsertSqlBuilder.getSql();
	}

	@Override
	public String getStaticUpdateSql() {
		return staticUpdateSqlBuilder.getSql();
	}*/

	/*@Override
	public String getStaticFetchSql() {
		return staticFetchSqlBuilder.getSql();
	}*/

	@Override
	public String getStaticSeqSql() {
		throw new UnsupportedOperationException("the queryable entity unsupported this operation!");
	}

	@Override
	protected EntrySQLBuilderImpl getStaticInsertSqlBuilder() {
		throw new UnsupportedOperationException("the queryable entity unsupported this operation!");
	}

	@Override
	protected EntrySQLBuilderImpl getStaticUpdateSqlBuilder() {
		throw new UnsupportedOperationException("the queryable entity unsupported this operation!");
	}

	@Override
	protected EntrySQLBuilderImpl getStaticDeleteSqlBuilder() {
		throw new UnsupportedOperationException("the queryable entity unsupported this operation!");
	}

	public boolean isQueryableOnly() {
		return true;
	}

	public MappedType getMappedType() {
		return MappedType.QUERYABLE_ONLY;
	}

	@Override
	protected EntrySQLBuilderImpl getStaticFetchSqlBuilder() {
		return staticFetchSqlBuilder;
	}

	@Override
	protected EntrySQLBuilder getStaticFetchAllSqlBuilder() {
		throw new UnsupportedOperationException("the queryable entity unsupported this operation!");
	}

	@Override
	protected EntrySQLBuilder getStaticSelectVersionSqlBuilder() {
		throw new UnsupportedOperationException("the queryable entity unsupported this operation!");
	}
}

package org.onetwo.common.jfishdb;

import java.util.Collection;

import org.onetwo.common.jfishdb.orm.AbstractJFishMappedEntryImpl;
import org.onetwo.common.jfishdb.orm.AbstractMappedField;
import org.onetwo.common.jfishdb.orm.EntrySQLBuilder;
import org.onetwo.common.jfishdb.orm.JFishMappedEntry;
import org.onetwo.common.jfishdb.orm.MappedType;
import org.onetwo.common.jfishdb.orm.TableInfo;
import org.onetwo.common.jfishdb.orm.SQLBuilderFactory.SqlBuilderType;
import org.onetwo.common.utils.AnnotationInfo;

public class JFishQueryableMappedEntryImpl extends AbstractJFishMappedEntryImpl implements JFishMappedEntry {

	private EntrySQLBuilder staticFetchSqlBuilder;
	

	public JFishQueryableMappedEntryImpl(AnnotationInfo annotationInfo, TableInfo tableInfo) {
		super(annotationInfo, tableInfo);
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
	protected EntrySQLBuilder getStaticInsertSqlBuilder() {
		throw new UnsupportedOperationException("the queryable entity unsupported this operation!");
	}

	@Override
	protected EntrySQLBuilder getStaticUpdateSqlBuilder() {
		throw new UnsupportedOperationException("the queryable entity unsupported this operation!");
	}

	@Override
	protected EntrySQLBuilder getStaticDeleteSqlBuilder() {
		throw new UnsupportedOperationException("the queryable entity unsupported this operation!");
	}

	public boolean isQueryableOnly() {
		return true;
	}

	public MappedType getMappedType() {
		return MappedType.QUERYABLE_ONLY;
	}

	@Override
	protected EntrySQLBuilder getStaticFetchSqlBuilder() {
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

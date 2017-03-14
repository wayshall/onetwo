package org.onetwo.dbm.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.onetwo.common.annotation.AnnotationInfo;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.mapping.SQLBuilderFactory.SqlBuilderType;
import org.onetwo.dbm.support.SimpleDbmInnerServiceRegistry;

public class DbmMappedEntryImpl extends AbstractDbmMappedEntryImpl implements DbmMappedEntry {
	

	private EntrySQLBuilderImpl staticInsertSqlBuilder;
	private EntrySQLBuilderImpl staticUpdateSqlBuilder;
	private EntrySQLBuilderImpl staticFetchAllSqlBuilder;
	private EntrySQLBuilderImpl staticFetchSqlBuilder;
	private EntrySQLBuilderImpl staticDeleteSqlBuilder;
	private EntrySQLBuilderImpl staticDeleteAllSqlBuilder;
	private EntrySQLBuilderImpl staticSeqSqlBuilder;
	private EntrySQLBuilderImpl staticCreateSeqSqlBuilder;
	private EntrySQLBuilderImpl staticSelectVersionSqlBuilder;
	
	public DbmMappedEntryImpl(AnnotationInfo annotationInfo, TableInfo tableInfo, SimpleDbmInnerServiceRegistry serviceRegistry) {
		super(annotationInfo, tableInfo, serviceRegistry);
	}


	protected Collection<DbmMappedField> getInsertableFields(){
		List<DbmMappedField> insertables = new ArrayList<DbmMappedField>();
		for(DbmMappedField field : getMappedColumns().values()){
			if(field.getColumn().isInsertable())
				insertables.add(field);
		}
		return insertables;
	}
	
	public List<DbmMappedField> getUpdateableFields(){
		List<DbmMappedField> updatables = new ArrayList<DbmMappedField>();
		for(DbmMappedField col : getMappedColumns().values()){
			if(col.getColumn().isUpdatable())
				updatables.add(col);
		}
		return updatables;
	}
	

	public Collection<DbmMappedField> getSelectableField() {
		List<DbmMappedField> cols = LangUtils.newArrayList();
		for(DbmMappedField col : this.getMappedColumns().values()){
			if(col.getColumn().isLazy())
				continue;
			cols.add(col);
		}
		return cols;
	}
	
	protected void buildStaticSQL(TableInfo taboleInfo){
//		List<ColumnInfo> idColumns = taboleInfo.getPrimaryKey().getColumns();

		staticInsertSqlBuilder = createSQLBuilder(SqlBuilderType.insert);
		staticInsertSqlBuilder.append(getInsertableFields());
		staticInsertSqlBuilder.build();
		
		staticUpdateSqlBuilder = createSQLBuilder(SqlBuilderType.update);
		staticUpdateSqlBuilder.append(getUpdateableFields());
		staticUpdateSqlBuilder.appendWhere(getIdentifyField());
		staticUpdateSqlBuilder.appendWhere(getVersionField());
		staticUpdateSqlBuilder.build();
		
		staticDeleteSqlBuilder = createSQLBuilder(SqlBuilderType.delete);
		staticDeleteSqlBuilder.setNamedPlaceHoder(false);
		staticDeleteSqlBuilder.appendWhere(getIdentifyField());
		staticDeleteSqlBuilder.build();
		
		staticSeqSqlBuilder = createSQLBuilder(SqlBuilderType.seq);
		staticSeqSqlBuilder.setNamedPlaceHoder(false);
		staticSeqSqlBuilder.build();
		
		staticCreateSeqSqlBuilder = createSQLBuilder(SqlBuilderType.createSeq);
		staticCreateSeqSqlBuilder.setNamedPlaceHoder(false);
		staticCreateSeqSqlBuilder.build();

		Collection<DbmMappedField> columns = getSelectableField();
		staticFetchSqlBuilder = createSQLBuilder(SqlBuilderType.query);
		staticFetchSqlBuilder.setNamedPlaceHoder(false);
		staticFetchSqlBuilder.append(columns);
		staticFetchSqlBuilder.appendWhere(getIdentifyField());
		staticFetchSqlBuilder.build();

		staticFetchAllSqlBuilder = createSQLBuilder(SqlBuilderType.query);
		staticFetchAllSqlBuilder.setNamedPlaceHoder(false);
		staticFetchAllSqlBuilder.append(columns);
		staticFetchAllSqlBuilder.build();

		staticDeleteAllSqlBuilder = createSQLBuilder(SqlBuilderType.delete);
		staticDeleteAllSqlBuilder.setNamedPlaceHoder(false);
		staticDeleteAllSqlBuilder.build();
		
		staticSelectVersionSqlBuilder = createSQLBuilder(SqlBuilderType.query);
		staticSelectVersionSqlBuilder.append(getVersionField());
		staticSelectVersionSqlBuilder.appendWhere(getIdentifyField());
		staticSelectVersionSqlBuilder.build();
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

/*	@Override
	public String getStaticFetchSql() {
		return staticFetchSqlBuilder.getSql();
	}*/

	@Override
	public String getStaticSeqSql() {
		return staticSeqSqlBuilder.getSql();
	}

	@Override
	public String getStaticCreateSeqSql() {
		return staticCreateSeqSqlBuilder.getSql();
	}

	@Override
	protected EntrySQLBuilderImpl getStaticInsertSqlBuilder() {
		return staticInsertSqlBuilder;
	}

	@Override
	protected EntrySQLBuilderImpl getStaticUpdateSqlBuilder() {
		return staticUpdateSqlBuilder;
	}

	@Override
	protected EntrySQLBuilderImpl getStaticDeleteSqlBuilder() {
		return staticDeleteSqlBuilder;
	}


	@Override
	protected EntrySQLBuilderImpl getStaticFetchSqlBuilder() {
		return staticFetchSqlBuilder;
	}

	public EntrySQLBuilder getStaticFetchAllSqlBuilder() {
		return staticFetchAllSqlBuilder;
	}


	@Override
	protected EntrySQLBuilder getStaticDeleteAllSqlBuilder() {
		return staticDeleteAllSqlBuilder;
	}

	@Override
	protected EntrySQLBuilder getStaticSelectVersionSqlBuilder() {
		return staticSelectVersionSqlBuilder;
	}


	public EntrySQLBuilderImpl getStaticCreateSeqSqlBuilder() {
		return staticCreateSeqSqlBuilder;
	}
	
}

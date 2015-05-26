package org.onetwo.common.jfishdbm.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.onetwo.common.jfishdbm.mapping.SQLBuilderFactory.SqlBuilderType;
import org.onetwo.common.utils.AnnotationInfo;
import org.onetwo.common.utils.LangUtils;

public class JFishMappedEntryImpl extends AbstractJFishMappedEntryImpl implements JFishMappedEntry {
	

	private EntrySQLBuilder staticInsertSqlBuilder;
	private EntrySQLBuilder staticUpdateSqlBuilder;
	private EntrySQLBuilder staticFetchAllSqlBuilder;
	private EntrySQLBuilder staticFetchSqlBuilder;
	private EntrySQLBuilder staticDeleteSqlBuilder;
	private EntrySQLBuilder staticDeleteAllSqlBuilder;
	private EntrySQLBuilder staticSeqSqlBuilder;
	private EntrySQLBuilder staticSelectVersionSqlBuilder;
	
	public JFishMappedEntryImpl(AnnotationInfo annotationInfo, TableInfo tableInfo) {
		super(annotationInfo, tableInfo);
	}


	protected Collection<JFishMappedField> getInsertableFields(){
		List<JFishMappedField> insertables = new ArrayList<JFishMappedField>();
		for(JFishMappedField field : getMappedColumns().values()){
			if(field.getColumn().isInsertable())
				insertables.add(field);
		}
		return insertables;
	}
	
	public List<JFishMappedField> getUpdateableFields(){
		List<JFishMappedField> updatables = new ArrayList<JFishMappedField>();
		for(JFishMappedField col : getMappedColumns().values()){
			if(col.getColumn().isUpdatable())
				updatables.add(col);
		}
		return updatables;
	}
	

	public Collection<JFishMappedField> getSelectableField() {
		List<JFishMappedField> cols = LangUtils.newArrayList();
		for(JFishMappedField col : this.getMappedColumns().values()){
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

		Collection<JFishMappedField> columns = getSelectableField();
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
	protected EntrySQLBuilder getStaticInsertSqlBuilder() {
		return staticInsertSqlBuilder;
	}

	@Override
	protected EntrySQLBuilder getStaticUpdateSqlBuilder() {
		return staticUpdateSqlBuilder;
	}

	@Override
	protected EntrySQLBuilder getStaticDeleteSqlBuilder() {
		return staticDeleteSqlBuilder;
	}


	@Override
	protected EntrySQLBuilder getStaticFetchSqlBuilder() {
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
	
}

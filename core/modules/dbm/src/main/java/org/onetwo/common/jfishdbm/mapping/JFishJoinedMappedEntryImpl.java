package org.onetwo.common.jfishdbm.mapping;

import java.util.Collection;

import org.onetwo.common.annotation.AnnotationInfo;
import org.onetwo.common.jfishdbm.mapping.SQLBuilderFactory.SqlBuilderType;
import org.onetwo.common.jfishdbm.support.SimpleDbmInnserServiceRegistry;

/****
 * 连接表映射？
 * @author way
 *
 */
public class JFishJoinedMappedEntryImpl extends AbstractJFishMappedEntryImpl implements JFishMappedEntry {

	private EntrySQLBuilder staticInsertSqlBuilder;
	private EntrySQLBuilder staticFetchSqlBuilder;
	private EntrySQLBuilder staticDeleteSqlBuilder;
	private EntrySQLBuilder staticFetchAllSqlBuilder;
	
	public JFishJoinedMappedEntryImpl(AnnotationInfo annotationInfo, TableInfo tableInfo, SimpleDbmInnserServiceRegistry serviceRegistry) {
		super(annotationInfo, tableInfo, serviceRegistry);
	}
	
	protected void buildStaticSQL(TableInfo tableInfo){
		Collection<AbstractMappedField> columns = getFields();
//		List<ColumnInfo> idColumns = tableInfo.getPrimaryKey().getColumns();

		staticInsertSqlBuilder = createSQLBuilder(SqlBuilderType.insert);
		staticInsertSqlBuilder.append(getFields());
		staticInsertSqlBuilder.build();
		
		staticDeleteSqlBuilder = createSQLBuilder(SqlBuilderType.delete);
		staticDeleteSqlBuilder.setNamedPlaceHoder(false);
		staticDeleteSqlBuilder.appendWhere(getFields());
		staticDeleteSqlBuilder.build();
		
		staticFetchSqlBuilder = createSQLBuilder(SqlBuilderType.query);
		staticFetchSqlBuilder.setNamedPlaceHoder(false);
		staticFetchSqlBuilder.append(columns);
		staticFetchSqlBuilder.appendWhere(columns);
		staticFetchSqlBuilder.build();

		staticFetchAllSqlBuilder = createSQLBuilder(SqlBuilderType.query);
		staticFetchAllSqlBuilder.setNamedPlaceHoder(false);
		staticFetchAllSqlBuilder.append(columns);
		staticFetchAllSqlBuilder.build();
	}

	@Override
	public String getStaticSeqSql() {
		throw new UnsupportedOperationException("the joined entity unsupported this operation!");
	}

	@Override
	protected EntrySQLBuilder getStaticInsertSqlBuilder() {
		return staticInsertSqlBuilder;
	}

	@Override
	protected EntrySQLBuilder getStaticUpdateSqlBuilder() {
		throw new UnsupportedOperationException("the joined entity unsupported this operation!");
	}

	@Override
	protected EntrySQLBuilder getStaticDeleteSqlBuilder() {
		return staticDeleteSqlBuilder;
	}

	@Override
	public MappedType getMappedType() {
//		return MappedType.JOINED;
		throw new UnsupportedOperationException();
	}

	@Override
	protected EntrySQLBuilder getStaticFetchSqlBuilder() {
		return staticFetchSqlBuilder;
	}

	public EntrySQLBuilder getStaticFetchAllSqlBuilder() {
		return staticFetchAllSqlBuilder;
	}

	@Override
	protected EntrySQLBuilder getStaticSelectVersionSqlBuilder() {
		throw new UnsupportedOperationException("the joined entity unsupported this operation!");
	}
	
}

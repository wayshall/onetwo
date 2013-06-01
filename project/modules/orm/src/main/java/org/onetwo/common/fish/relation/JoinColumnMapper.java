package org.onetwo.common.fish.relation;

import java.util.Collection;
import java.util.List;

import org.onetwo.common.fish.exception.JFishEntityNotSavedException;
import org.onetwo.common.fish.exception.JFishOrmException;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.JFishMappedField;
import org.onetwo.common.fish.orm.JFishMappedFieldType;
import org.onetwo.common.fish.orm.SQLBuilderFactory.SqlBuilderType;
import org.onetwo.common.fish.orm.TableSQLBuilder;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.map.KVEntry;

public class JoinColumnMapper {

//	private final CascadeMappedField mappedField;

	private JFishMappedField toOneMappedField;//of joinEntry
	private JFishMappedEntry joinEntry;//many side entry, to one entry
	private JoinColumnInfo joinColumn;//on many side
	private JFishMappedField joinColumnField;
	
	private JFishMappedEntry cascadeEntry;//one one side

	private TableSQLBuilder updateSingleRefSqlBuilder;
	private TableSQLBuilder updateAllRefSqlBuilder;


	public JoinColumnMapper(JFishMappedEntry manyEntry, JoinColumnInfo joinColumnOfMainEntry, JFishMappedEntry oneEntry) {
		this(null, manyEntry, joinColumnOfMainEntry, oneEntry);
	}
	
	public JoinColumnMapper(CascadeMappedField manyToOneMappedField, JFishMappedEntry manyEntry, JoinColumnInfo joinColumn, JFishMappedEntry oneEntry) {
		super();
		if(manyToOneMappedField==null){
			this.toOneMappedField = manyEntry.getFieldByColumnName(joinColumn.getName());
		}else{
			this.toOneMappedField = manyToOneMappedField;
		}
		this.joinEntry = manyEntry;
		this.cascadeEntry = oneEntry;
		this.joinColumn = joinColumn;
		this.joinColumnField = manyEntry.getFieldByColumnName(joinColumn.getName());
		this.createSqlBuilder();
	}

	protected void createSqlBuilder(){
//		updateSingleRefSqlBuilder = joinEntry.createSQLBuilder(SqlBuilderType.update);
		updateSingleRefSqlBuilder = joinEntry.getSqlBuilderFactory().createQMark(joinEntry.getTableInfo().getName(), joinEntry.getTableInfo().getAlias(), SqlBuilderType.update);
		updateSingleRefSqlBuilder.append(joinColumn).appendWhere(joinEntry.getIdentifyField().getColumn());
		updateSingleRefSqlBuilder.build();

//		updateAllRefSqlBuilder = joinEntry.createSQLBuilder(SqlBuilderType.update);
		updateAllRefSqlBuilder = joinEntry.getSqlBuilderFactory().createQMark(joinEntry.getTableInfo().getName(), joinEntry.getTableInfo().getAlias(), SqlBuilderType.update);
		updateAllRefSqlBuilder.append(joinColumn).appendWhere(joinColumn);
		updateAllRefSqlBuilder.build();
	}

	public KVEntry<String, Object[]> makeSaveRefOfToOne(Object joinEntity){
		Object oneEntity = toOneMappedField.getValue(joinEntity);
		if(oneEntity==null)
			return null;
		return makeSaveRefOfToOne(joinEntity, oneEntity);
	}

	public KVEntry<String, Object[]> makeSaveRefOfToOne(Object joinEntity, Object oneEntity){
		Object joinEntityId = joinEntry.getId(joinEntity);
		Object refValue = getReferencedField().getValue(oneEntity);
		if(refValue==null)
			throw new JFishEntityNotSavedException(oneEntity);
		KVEntry<String, Object[]> kv = KVEntry.create(updateSingleRefSqlBuilder.getSql(), new Object[]{refValue, joinEntityId});
		return kv;
	}

	public KVEntry<String, Object[]> makeDropRefOfToOne(Object joinEntity){
		Object mainId = joinEntry.getId(joinEntity);
		KVEntry<String, Object[]> kv = KVEntry.create(updateSingleRefSqlBuilder.getSql(), new Object[]{null, mainId});
		return kv;
	}

	public KVEntry<String, Object[]> makeDropAllRef(Object oneEntity){
		Object refValue = getReferencedField().getColumnValue(oneEntity);
		KVEntry<String, Object[]> kv = KVEntry.create(updateAllRefSqlBuilder.getSql(), new Object[]{null, refValue});
		return kv;
	}

	public KVEntry<String, List<Object[]>> makeSaveRefOfToMany(Object oneEntity, Collection<?> joinEntities){//one to many
//		Collection<?> relatedEntities = CUtils.toCollection(toOneMappedField.getValue(main));
		if(LangUtils.isEmpty(joinEntities))
			return null;

		Object refValue = getReferencedField().getValue(oneEntity);
		KVEntry<String, List<Object[]>> kv = KVEntry.create(updateSingleRefSqlBuilder.getSql(), makeRefOfToMany(refValue, joinEntities));
		return kv;
	}

	public KVEntry<String, List<Object[]>> makeDropRefOfToMany(Collection<?> joinEntities){
		if(LangUtils.isEmpty(joinEntities))
			return null;

		KVEntry<String, List<Object[]>> kv = KVEntry.create(updateSingleRefSqlBuilder.getSql(), makeRefOfToMany(null, joinEntities));
		return kv;
	}

	protected List<Object[]> makeRefOfToMany(Object refValue, Collection<?> joinEntities){
		List<Object[]> arglist = LangUtils.newArrayList(joinEntities.size());
		for(Object related : joinEntities){
			Object joinEntityId = joinEntry.getId(related);
			if(!joinEntry.hasIdentifyValue(related)){
				throw new JFishEntityNotSavedException(related);
			}
			arglist.add(new Object[]{refValue, joinEntityId});
		}
//		KVEntry<String, List<Object[]>> kv = KVEntry.create(staticUpdateRefSqlBuilder.getSql(), arglist);
		return arglist;
	}


	public void setJoinFieldValue(Object joinEntity, Object cascadeEntity) {
		if(JFishMappedFieldType.FIELD==joinColumnField.getMappedFieldType()){//un-bidirection
			JFishMappedField refField = getReferencedField();
			Object val = refField.getValue(cascadeEntity);
			joinColumnField.setValue(joinEntity, val);
		}else{//bidirection
			joinColumnField.setValue(joinEntity, cascadeEntity);
		}
	}
	
	public JFishMappedField getReferencedField(){
		String refColName = getReferencedColumnName();
		JFishMappedField referncedField = cascadeEntry.getFieldByColumnName(refColName);
		if(referncedField==null)
			throw new JFishOrmException("can not find join entry["+joinEntry.getEntityName()+"] reference field [" + joinColumn.getReferencedColumnName()+"] on ["+cascadeEntry.getEntityName()+"]");
		return referncedField;
	}
	
	public String getReferencedColumnName(){
		String refColName = joinColumn.getReferencedColumnName();
		if(StringUtils.isBlank(refColName)){
			refColName = cascadeEntry.getIdentifyField().getName();
		}
		return refColName;
	}
	
	
	/*public void setJoinColumnOfCascadeEntry(JoinColumnInfo joinColumnOfCascadeEntry) {
		this.joinColumnOfCascadeEntry = joinColumnOfCascadeEntry;
	}*/
	public JFishMappedField getJoinField() {
		return joinColumnField;
	}
	
	public JoinColumnInfo getJoinColumn() {
		return joinColumn;
	}

}

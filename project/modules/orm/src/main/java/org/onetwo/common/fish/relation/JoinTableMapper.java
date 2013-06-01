package org.onetwo.common.fish.relation;

import java.util.Collection;
import java.util.List;

import org.onetwo.common.fish.exception.JFishEntityNotSavedException;
import org.onetwo.common.fish.exception.JFishOrmException;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.JFishMappedField;
import org.onetwo.common.fish.orm.SQLBuilderFactory.SqlBuilderType;
import org.onetwo.common.fish.orm.TableSQLBuilder;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.map.KVEntry;
import org.springframework.util.Assert;


public class JoinTableMapper {

	private final JoinTableInfo joinTable;
//	private SQLBuilder joinTableBuilder;
	private final CascadeMappedField sourceMappedField;
	

	private TableSQLBuilder saveRefSqlBuilder;
	private TableSQLBuilder dropSingleRefSqlBuilder;
	private TableSQLBuilder dropAllRefSqlBuilder;
	
	/*****
	 * if map by mapped property, inverse is true
	 */
	private boolean inverse = false;
	
	public JoinTableMapper(CascadeMappedField mappedField, JoinTableInfo joinTable) {
		this.sourceMappedField = mappedField;
		this.joinTable = joinTable;
//		this.mappedField.setJoinTableMapper(this);

		saveRefSqlBuilder = createSQLBuilder(SqlBuilderType.insert, joinTable);
		saveRefSqlBuilder.append(joinTable.getJoinColum()).append(joinTable.getInverseJoinColumn());
		saveRefSqlBuilder.build();

		dropSingleRefSqlBuilder = createSQLBuilder(SqlBuilderType.delete, joinTable);
		dropSingleRefSqlBuilder.appendWhere(joinTable.getJoinColum()).appendWhere(joinTable.getInverseJoinColumn());
		dropSingleRefSqlBuilder.build();

		dropAllRefSqlBuilder = createSQLBuilder(SqlBuilderType.delete, joinTable);
		dropAllRefSqlBuilder.appendWhere(joinTable.getJoinColum());
		dropAllRefSqlBuilder.build();
	}

	/*public JFishMappedField getReferencedFieldOfInverseJoinCloumn(JFishMappedEntry referencedFieldEntry){
		String colName = getJoinTable().getInverseJoinColumn().getReferencedColumnName();
		JFishMappedField referncedField = referencedFieldEntry.getFieldByColumnName(colName);
		if(referncedField==null)
			throw new JFishOrmException("can not find the InverseJoinColumn reference field: " + colName);
		return referncedField;
	}*/
	

	final protected TableSQLBuilder createSQLBuilder(SqlBuilderType type, JoinTableInfo joinTable){
		TableSQLBuilder sqlb = sourceMappedField.getEntry().getSqlBuilderFactory().createQMark(joinTable.getTable(), "", type);
		return sqlb;
	}

	public JFishMappedField getReferencedFieldOfInverseJoinCloumn(){
		String colName = getJoinTable().getInverseJoinColumn().getReferencedColumnName();
		JFishMappedField referncedField = getCascadeEntry().getFieldByColumnName(colName);
		if(referncedField==null)
			throw new JFishOrmException("can not find the InverseJoinColumn reference field: " + colName);
		return referncedField;
	}
	
	public JFishMappedEntry getCascadeEntry() {
		return sourceMappedField.getCascadeEntry();
	}

	public JFishMappedEntry getEntry() {
		return sourceMappedField.getEntry();
	}

	public JFishMappedField getReferenceFieldOfJoinColumn(){
		JFishMappedField jfield = getEntry().getFieldByColumnName(getJoinTable().getJoinColum().getReferencedColumnName());
		return jfield;
	}

	public JoinTableInfo getJoinTable() {
		return joinTable;
	}
	
	public boolean isJoinTableField(){
		return true;
	}

	public JFishMappedEntry getJoinFieldEntry() {
		return getEntry();
	}

	/*public KVEntry<String, List<Object[]>> makeJoinTableInsert(Object mainEntity){
		return makeJoinTable(SqlBuilderType.insert, mainEntity);
	}*/

	public KVEntry<String, List<Object[]>> makeSaveRefOfToMany(Object mainEntity){
		Assert.isInstanceOf(CascadeCollectionMappedField.class, sourceMappedField);
		Collection<?> relatedEntities = CUtils.toCollection(sourceMappedField.getValue(mainEntity));
		if(LangUtils.isEmpty(relatedEntities)){
			return null;
//			throw new JFishOrmException("entity["+mainEntity+"] related field["+sourceMappedField.getName()+"]'s value is null");
		}
		
		KVEntry<String, List<Object[]>> jtableEntry = KVEntry.create(saveRefSqlBuilder.getSql(), makeJoinTableValues(mainEntity, relatedEntities));
		return jtableEntry;
	}

	public KVEntry<String, List<Object[]>> makeDropRefOfToMany(Object mainEntity){
		Collection<?> relatedEntities = CUtils.toCollection(sourceMappedField.getValue(mainEntity));
		if(LangUtils.isEmpty(relatedEntities))
			return null;
		KVEntry<String, List<Object[]>> jtableEntry = KVEntry.create(dropSingleRefSqlBuilder.getSql(), makeJoinTableValues(mainEntity, relatedEntities));
		return jtableEntry;
	}

	public KVEntry<String, Object[]> makeDropAllRef(Object mainEntity){
		Object joinValue = getReferenceFieldOfJoinColumn().getColumnValue(mainEntity);
		if(joinValue==null){
			throw new JFishOrmException("main entity may be not saved yet : " + mainEntity.getClass());
		}
		KVEntry<String, Object[]> jtableEntry = KVEntry.create(dropAllRefSqlBuilder.getSql(), new Object[]{joinValue});
		return jtableEntry;
	}

	protected List<Object[]> makeJoinTableValues(Object mainEntity, Collection<?> relatedEntities){
		Object joinValue = getReferenceFieldOfJoinColumn().getColumnValue(mainEntity);
		if(joinValue==null){
			throw new JFishEntityNotSavedException(mainEntity.getClass());
		}
		List<Object[]> values = LangUtils.newArrayList();
		for(Object related : relatedEntities){
//			Object inverseValue = getReferencedFieldOfInverseJoinCloumn(cascadeEntry).getColumnValue(related);
			Object inverseValue = getReferencedFieldOfInverseJoinCloumn().getColumnValue(related);
			if(inverseValue==null){
				throw new JFishEntityNotSavedException(mainEntity.getClass(), this.sourceMappedField.getName());
			}
			values.add(new Object[]{joinValue, inverseValue});
		}
		
		
		return values;
	}

	public KVEntry<String, Object[]> makeSaveRefOfToOne(Object mainEntity){
		Object relatedEntity = sourceMappedField.getValue(mainEntity);
		if(relatedEntity==null){
			return null;
//			throw new JFishOrmException("entity["+mainEntity+"] related field["+sourceMappedField.getName()+"]'s value is null");
		}
		KVEntry<String, Object[]> jtableEntry = KVEntry.create(saveRefSqlBuilder.getSql(), makeJoinTableValue(mainEntity, relatedEntity));
		return jtableEntry;
	}

	public KVEntry<String, Object[]> makeDropRefOfToOne(Object mainEntity){
		Object relatedEntity = sourceMappedField.getValue(mainEntity);
		if(relatedEntity==null)
			return null;
		KVEntry<String, Object[]> jtableEntry = KVEntry.create(dropSingleRefSqlBuilder.getSql(), makeJoinTableValue(mainEntity, relatedEntity));
		return jtableEntry;
	}
	
	protected Object[] makeJoinTableValue(Object mainEntity, Object relatedEntity){
		Object joinValue = getReferenceFieldOfJoinColumn().getColumnValue(mainEntity);
		if(joinValue==null){
			throw new JFishEntityNotSavedException(mainEntity.getClass());
		}
		Object inverseValue = getReferencedFieldOfInverseJoinCloumn().getColumnValue(relatedEntity);
		if(inverseValue==null){
			throw new JFishEntityNotSavedException(mainEntity.getClass(), this.sourceMappedField.getName());
		}
		
		return new Object[]{joinValue, inverseValue};
	}

	public boolean isInverse() {
		return inverse;
	}

	public void setInverse(boolean inverse) {
		this.inverse = inverse;
	}
}

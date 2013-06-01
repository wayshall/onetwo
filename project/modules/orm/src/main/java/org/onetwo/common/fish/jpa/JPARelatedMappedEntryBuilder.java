package org.onetwo.common.fish.jpa;

import javax.persistence.JoinColumn;

import org.onetwo.common.fish.exception.JFishOrmException;
import org.onetwo.common.fish.orm.AbstractMappedField;
import org.onetwo.common.fish.orm.BaseColumnInfo;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.JFishMappedField;
import org.onetwo.common.fish.orm.JFishMappedFieldType;
import org.onetwo.common.fish.orm.TableInfo;
import org.onetwo.common.fish.relation.JFishManyToManyMappedField;
import org.onetwo.common.fish.relation.JFishOneToManyMappedField;
import org.onetwo.common.fish.relation.JFishRelatedEntryImpl;
import org.onetwo.common.fish.relation.JFishToOneMappedField;
import org.onetwo.common.fish.relation.JoinColumnInfo;
import org.onetwo.common.utils.AnnotationInfo;
import org.onetwo.common.utils.JFishProperty;
import org.onetwo.common.utils.LangUtils;

public class JPARelatedMappedEntryBuilder extends JPAMappedEntryBuilder {

	
	public JPARelatedMappedEntryBuilder(){
		super();
		setOrder(super.getOrder()-1);
//		register(JoinColumn.class, new JPARelatedJoinColumnParser());
//		register(JoinTable.class, new JPARelatedJoinTableParser());
//		register(new JPARelatedJoinTableParser());
	}

	@Override
	protected JFishMappedEntry createJFishMappedEntry(AnnotationInfo annotationInfo) {
		JFishRelatedEntryImpl entry = new JFishRelatedEntryImpl(annotationInfo, newTableInfo(annotationInfo));
		entry.setSqlBuilderFactory(this.getDialect().getSqlBuilderFactory());
		return entry;
	}

	@Override
	protected AbstractMappedField newManyToOneField(JFishMappedEntry entry, JFishProperty prop){
		return new JFishToOneMappedField(entry, prop);
	}
	
	protected AbstractMappedField newManyToManyField(JFishMappedEntry entry, JFishProperty prop){
//		throw new UnsupportedOperationException("unsupported many to many : " + entry.getEntityClass());
		AbstractMappedField mfield = new JFishManyToManyMappedField(entry, prop);
		return mfield;
	}
	
	protected AbstractMappedField newOneToManyField(JFishMappedEntry entry, JFishProperty prop){
/*		JFishToManyMappedField mfield = new JFishToManyMappedField(entry, prop);a
		return mfield;*/
		AbstractMappedField mfield = new JFishOneToManyMappedField(entry, prop);
		return mfield;
	}
	
	protected AbstractMappedField newOneToOneField(JFishMappedEntry entry, JFishProperty prop){
		throw new UnsupportedOperationException("unsupported one to one : " + entry.getEntityClass());
	}
	

	@Override
	protected BaseColumnInfo buildColumnInfo(TableInfo tableInfo, JFishMappedField field){
		BaseColumnInfo col = null;
		try {
			if(field.getMappedFieldType()==JFishMappedFieldType.FIELD){
				col = super.buildColumnInfo(tableInfo, field);
			}else if(field.getMappedFieldType()==JFishMappedFieldType.TO_ONE){
				col = this.createJoinColumn(tableInfo, field.getPropertyInfo().getAnnotation(JoinColumn.class));
			}else{
				LangUtils.println("join field: ${0}, ${1}", field.getEntry().getEntityName(), field.getName());
			}
		} catch (Exception e) {
			throw new JFishOrmException("build column["+field.getName()+"] of "+field.getEntry().getEntityName()+" error :" + e.getMessage());
		}
		
		return col;
	}
	
	protected JoinColumnInfo createJoinColumn(TableInfo tableInfo, JoinColumn annotationJoin) {
		JoinColumnInfo joinCol = new JoinColumnInfo(tableInfo, annotationJoin.table(), annotationJoin.name(), annotationJoin.referencedColumnName());
		return joinCol;
	}
	
}

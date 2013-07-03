package org.onetwo.common.fish.jpa;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.onetwo.common.fish.exception.JFishOrmException;
import org.onetwo.common.fish.orm.BaseColumnInfo;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.JFishMappedField;
import org.onetwo.common.fish.orm.MappedEntryManager;
import org.onetwo.common.fish.relation.AbstractRelatedAnnotationParser;
import org.onetwo.common.fish.relation.CascadeCollectionMappedField;
import org.onetwo.common.fish.relation.CascadeMappedField;
import org.onetwo.common.fish.relation.JFishManyToManyMappedField;
import org.onetwo.common.fish.relation.JFishRelatedEntryImpl;
import org.onetwo.common.fish.relation.JFishToOneMappedField;
import org.onetwo.common.fish.relation.JoinColumnInfo;
import org.onetwo.common.fish.relation.JoinColumnMapper;
import org.onetwo.common.fish.relation.JoinTableInfo;
import org.onetwo.common.fish.relation.JoinTableMapper;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class JPARelatedJoinColumnParser extends AbstractRelatedAnnotationParser {

	@Override
	public void manyToOne(MappedEntryManager mappedEntryManager, JFishRelatedEntryImpl manyEntry, CascadeMappedField one) {
		JFishMappedEntry oneSide = mappedEntryManager.getEntry(one.getPropertyInfo().getType());
		one.setCascadeEntry(oneSide);

		if(one.getPropertyInfo().hasAnnotation(JoinTable.class)){
			JoinTableMapper joinTableMapper = this.createJoinTableMapper(one, manyEntry, oneSide);
			manyEntry.addJoinableField(one);
			one.setJoinTableMapper(joinTableMapper);
		}else if(one.getPropertyInfo().hasAnnotation(JoinColumn.class)){
			JoinColumnMapper joinColumnMapper = null;
			BaseColumnInfo joinCol = one.getColumn();
			Assert.isInstanceOf(JoinColumnInfo.class, joinCol);
			
			joinColumnMapper = new JoinColumnMapper(one, manyEntry, (JoinColumnInfo)joinCol, oneSide);
			one.setJoinColumnMapper(joinColumnMapper);
		}else{
			throw new JFishOrmException("it must explicit to set the JoinColumn.");
		}
	}
	
	protected JoinTableMapper createJoinTableMapper(CascadeMappedField cascadeField, JFishMappedEntry mainEntry, JFishMappedEntry cascadeEntry){
		JoinTable annoJoinTable = cascadeField.getPropertyInfo().getAnnotation(JoinTable.class);

		JoinColumn annotationJoin = LangUtils.getFirst(annoJoinTable.joinColumns());
		JoinColumn inverseJoinCol = LangUtils.getFirst(annoJoinTable.inverseJoinColumns());
		if(annotationJoin==null || inverseJoinCol==null)
			throw new JFishOrmException("@JoinTable miss joinColumns or inverseJoinColumns");
		
		JoinColumnInfo joinCol = this.createJoinColumn(mainEntry, annotationJoin);
		JoinColumnInfo inverseCol = this.createJoinColumn(cascadeEntry, inverseJoinCol);
		JoinTableInfo jtable = new JoinTableInfo(annoJoinTable.name(), joinCol, inverseCol);
		
		JoinTableMapper joinTableMapper = new JoinTableMapper(cascadeField, jtable);
		
		return joinTableMapper;
	}
	
	protected JoinTableMapper createInverseJoinTableMapper(CascadeMappedField cascadeField, JoinTableMapper joinTableMapper){
		JoinTableInfo jtable = joinTableMapper.getJoinTable();
		JoinTableInfo inverseJoinTable = new JoinTableInfo(jtable.getTable(), jtable.getInverseJoinColumn(), jtable.getJoinColum());
		JoinTableMapper inverseJointableMapper = new JoinTableMapper(cascadeField, inverseJoinTable);
		inverseJointableMapper.setInverse(true);
		return inverseJointableMapper;
	}

	@Override
	public void manyToMany(MappedEntryManager mappedEntryManager, JFishRelatedEntryImpl entry, CascadeCollectionMappedField manyField) {
		JFishMappedEntry  inverseManyEntry = mappedEntryManager.getEntry(manyField.getPropertyInfo().getFirstParameterType());
		manyField.setCascadeEntry(inverseManyEntry);

		ManyToMany manyToMany = manyField.getPropertyInfo().getAnnotation(ManyToMany.class);
		if(StringUtils.isNotBlank(manyToMany.mappedBy())){
			JFishMappedField mappedByField = inverseManyEntry.getField(manyToMany.mappedBy());
			if (mappedByField == null)
				throw new JFishOrmException("no mappedBy field : " + manyToMany.mappedBy());
			if (!JFishManyToManyMappedField.class.isInstance(mappedByField))
				throw new JFishOrmException("the mappedBy field must be have @ManyToOne : " + manyToMany.mappedBy());
			JFishManyToManyMappedField inverseManyField = (JFishManyToManyMappedField) mappedByField;
			if(inverseManyField.isJoinTableField()){
				manyField.setJoinTableMapper(this.createInverseJoinTableMapper(manyField, inverseManyField.getJoinTableMapper()));
			}else{
				manyField.setJoinColumnMapper(inverseManyField.getJoinColumnMapper());
			}
		}else if(manyField.getPropertyInfo().hasAnnotation(JoinTable.class)){
			JoinTableMapper joinTableMapper = this.createJoinTableMapper(manyField, entry, inverseManyEntry);
			entry.addJoinableField(manyField);
			manyField.setJoinTableMapper(joinTableMapper);
		}else {
			throw new JFishOrmException("it must explicit to set the mappedby  or JoinTable.");
		}
	}
	
	@Override
	public void oneToMany(MappedEntryManager mappedEntryManager, JFishRelatedEntryImpl oneEntry, CascadeCollectionMappedField oneToManyField) {
		Class<?> actualType = (Class<?>) LangUtils.getFirst(oneToManyField.getPropertyInfo().getParameterTypes());
		JFishMappedEntry manySideEntry = mappedEntryManager.getEntry(actualType);
		oneToManyField.setCascadeEntry(manySideEntry);

		OneToMany oneToMany = oneToManyField.getPropertyInfo().getAnnotation(OneToMany.class);

		String mappedBy = oneToMany.mappedBy();
		if (StringUtils.isNotBlank(mappedBy)) {
			JFishMappedField mappedByField = manySideEntry.getField(mappedBy);
			if (mappedByField == null)
				throw new JFishOrmException("no mappedBy field : " + mappedBy);
			if (!JFishToOneMappedField.class.isInstance(mappedByField))
				throw new JFishOrmException("the mappedBy field must be have @ManyToOne : " + mappedBy);
			JFishToOneMappedField manyToOneField = (JFishToOneMappedField) mappedByField;
			if(manyToOneField.isJoinTableField()){
				oneToManyField.setJoinTableMapper(this.createInverseJoinTableMapper(oneToManyField, manyToOneField.getJoinTableMapper()));
			}else{
				oneToManyField.setJoinColumnMapper(manyToOneField.getJoinColumnMapper());
			}
			
		}else if(oneToManyField.getPropertyInfo().hasAnnotation(JoinTable.class)){
			JoinTableMapper joinTableMapper = this.createJoinTableMapper(oneToManyField, oneEntry, manySideEntry);
			oneEntry.addJoinableField(oneToManyField);
			oneToManyField.setJoinTableMapper(joinTableMapper);
			
		} else if(oneToManyField.getPropertyInfo().hasAnnotation(JoinColumn.class)){
			JoinColumn annotationJoin = oneToManyField.getPropertyInfo().getAnnotation(JoinColumn.class);
			JoinColumnInfo joinInfo = createJoinColumn(oneEntry, annotationJoin);
			JoinColumnMapper joinColumnMapper = new JoinColumnMapper(manySideEntry, joinInfo, oneEntry);
			oneToManyField.setJoinColumnMapper(joinColumnMapper);

		} else {
			throw new JFishOrmException("it must explicit to set the mappedby or JoinColumn or JoinTable.");
		}
		
	}

	protected JoinColumnInfo createJoinColumn(JFishMappedEntry entry, JoinColumn annotationJoin) {
		String refName = entry.getIdentifyField().getColumn().getName();
		if (StringUtils.isNotBlank(annotationJoin.referencedColumnName())) {
			refName = annotationJoin.referencedColumnName();
		}
		JoinColumnInfo joinCol = new JoinColumnInfo(null, annotationJoin.table(), annotationJoin.name(), refName);
		return joinCol;
	}


}

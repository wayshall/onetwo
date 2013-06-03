package org.onetwo.common.fish.relation;

import java.util.Collection;
import java.util.HashSet;

import org.onetwo.common.fish.orm.JFishMappedEntryImpl;
import org.onetwo.common.fish.orm.TableInfo;
import org.onetwo.common.utils.AnnotationInfo;

public class JFishRelatedEntryImpl extends JFishMappedEntryImpl {

	private final Collection<CascadeMappedField> toOneFields = new HashSet<CascadeMappedField>(5);
	private final Collection<CascadeCollectionMappedField> oneToManyFields = new HashSet<CascadeCollectionMappedField>(5);
	private final Collection<CascadeCollectionMappedField> manyToManyFields = new HashSet<CascadeCollectionMappedField>(5);
	private final Collection<CascadeCollectionMappedField> toManyFields = new HashSet<CascadeCollectionMappedField>(5);
	private final Collection<CascadeMappedField> joinMappedFields = new HashSet<CascadeMappedField>(5);

	public JFishRelatedEntryImpl(AnnotationInfo annotationInfo, TableInfo tableInfo) {
		super(annotationInfo, tableInfo);
	}

	@Override
	public void addToOneField(CascadeMappedField manyToOne){
		this.toOneFields.add(manyToOne);
	}

	public Collection<CascadeMappedField> getToOneFields() {
		return toOneFields;
	}

	@Override
	public void addOneToManyField(CascadeCollectionMappedField oneToMany){
		this.oneToManyFields.add(oneToMany);
		this.toManyFields.add(oneToMany);
	}

	@Override
	public void addManyToManyField(CascadeCollectionMappedField manyToMany){
		this.manyToManyFields.add(manyToMany);
		this.toManyFields.add(manyToMany);
	}
	
	public void addJoinableField(CascadeMappedField joinalbe){
		this.joinMappedFields.add(joinalbe);
	}
	public Collection<CascadeMappedField> getJoinMappedFields() {
		return joinMappedFields;
	}

	public Collection<CascadeCollectionMappedField> getOneToManyFields() {
		return oneToManyFields;
	}

	public Collection<CascadeCollectionMappedField> getManyToManyFields() {
		return manyToManyFields;
	}

	public Collection<CascadeCollectionMappedField> getToManyFields() {
		return toManyFields;
	}
	

}

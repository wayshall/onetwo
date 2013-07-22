package org.onetwo.common.fish.relation;

import java.util.Collection;

import org.onetwo.common.fish.relation.JFishOneToManyMappedField.FieldValueHolder;

public interface CascadeCollectionMappedField extends CascadeMappedField {

	public Collection<?> getValueAsCollection(Object entity);
	public FieldValueHolder getValueAsValueHolder(Object entity);
	
	public CascadeMappedField getMappedOneField();
	
	public void setMappedOneField(CascadeMappedField mappedOneField);

}
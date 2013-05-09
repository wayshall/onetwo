package org.onetwo.common.fish.relation;

import org.onetwo.common.fish.orm.MappedEntryManager;

public interface RelatedAnnotationParser {

	public void manyToOne(MappedEntryManager mappedEntryManager, JFishRelatedEntryImpl entry, CascadeMappedField one);
	
	public void oneToMany(MappedEntryManager mappedEntryManager, JFishRelatedEntryImpl jpaEntry, CascadeCollectionMappedField many);
	
	public void manyToMany(MappedEntryManager mappedEntryManager, JFishRelatedEntryImpl entry, CascadeCollectionMappedField many);
	
}

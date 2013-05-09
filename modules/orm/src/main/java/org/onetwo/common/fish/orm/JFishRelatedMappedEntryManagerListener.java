package org.onetwo.common.fish.orm;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.fish.jpa.JPARelatedJoinColumnParser;
import org.onetwo.common.fish.relation.CascadeCollectionMappedField;
import org.onetwo.common.fish.relation.CascadeMappedField;
import org.onetwo.common.fish.relation.JFishRelatedEntryImpl;
import org.onetwo.common.fish.relation.RelatedAnnotationParser;
import org.onetwo.common.fish.spring.ScanedClassContext;
import org.springframework.core.Ordered;

public class JFishRelatedMappedEntryManagerListener implements MappedEntryManagerListener, Ordered{
	
	private List<RelatedAnnotationParser> relatedAnnotationParser = new ArrayList<RelatedAnnotationParser>();
	
	public JFishRelatedMappedEntryManagerListener(){
		register(new JPARelatedJoinColumnParser());
	}

	public final void register(RelatedAnnotationParser parser){
		relatedAnnotationParser.add(parser);
	}

	public void buildRelated(MappedEntryManager mappedEntryManager, JFishMappedEntry entry) {
		if(!JFishRelatedEntryImpl.class.isInstance(entry)){
			return ;
		}
		
		JFishRelatedEntryImpl jpaEntry = (JFishRelatedEntryImpl) entry;
		
		for(CascadeMappedField one : jpaEntry.getToOneFields()){
			for(RelatedAnnotationParser parser : relatedAnnotationParser){
				parser.manyToOne(mappedEntryManager, jpaEntry, one);
			}
		}
		
		for(CascadeCollectionMappedField many : jpaEntry.getOneToManyFields()){
			for(RelatedAnnotationParser parser : relatedAnnotationParser){
				parser.oneToMany(mappedEntryManager, jpaEntry, many);
			}
		}
		
		for(CascadeCollectionMappedField many : jpaEntry.getManyToManyFields()){
			for(RelatedAnnotationParser parser : relatedAnnotationParser){
				parser.manyToMany(mappedEntryManager, jpaEntry, many);
			}
		}
		
	}

	@Override
	public void beforeBuild(MappedEntryManager mappedEntryManager, List<ScanedClassContext> clssNameList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterAllEntryHasBuilt(MappedEntryManager mappedEntryManager, List<JFishMappedEntry> entryList) {
//		for(JFishMappedEntry entry : entryList){
//			buildRelated(mappedEntryManager, entry);
//		}
		
	}

	@Override
	public void afterBuilt(MappedEntryManager mappedEntryManager, JFishMappedEntry entry) {
		buildRelated(mappedEntryManager, entry);
	}

	@Override
	public int getOrder() {
		return 0;
	}
	
}

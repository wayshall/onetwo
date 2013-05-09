package org.onetwo.plugins.richmodel;

import org.onetwo.common.fish.JFishEntityManager;
import org.onetwo.common.fish.JFishQueryBuilder;

public interface RichModelEntityManager extends JFishEntityManager {

	public JFishQueryBuilder createCascadeQueryBuilder(Object entity, Class<?> relatedClass, String... relatedMappedFields);
	
	public CascadeModel createCascadeModel(Object entity, String relatedField);
}

package org.onetwo.dbm.richmodel;

import java.util.Collection;
import java.util.HashSet;

public class MultiEnhancer implements ModelEnhancer {

	private Collection<ModelEnhancer> enhancers = new HashSet<ModelEnhancer>(5);
	
	@Override
	public void enhance(EnhanceContext context) {
		for(ModelEnhancer enhancer : enhancers){
			enhancer.enhance(context);
		}
		
	}
	
	public void addEnhancer(ModelEnhancer enhancer){
		this.enhancers.add(enhancer);
	}

}

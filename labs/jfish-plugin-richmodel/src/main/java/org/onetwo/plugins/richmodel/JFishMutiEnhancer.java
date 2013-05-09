package org.onetwo.plugins.richmodel;

import java.util.Collection;
import java.util.HashSet;

public class JFishMutiEnhancer implements JFishEnhancer {

	private Collection<JFishEnhancer> enhancers = new HashSet<JFishEnhancer>(5);
	
	@Override
	public void enhance(EnhanceContext context) {
		for(JFishEnhancer enhancer : enhancers){
			enhancer.enhance(context);
		}
		
	}
	
	public void addEnhancer(JFishEnhancer enhancer){
		this.enhancers.add(enhancer);
	}

}

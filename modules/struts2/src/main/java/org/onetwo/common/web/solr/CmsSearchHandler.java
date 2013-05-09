package org.onetwo.common.web.solr;

import java.util.List;

import org.apache.solr.handler.StandardRequestHandler;

public class CmsSearchHandler extends StandardRequestHandler {
	
	public CmsSearchHandler(){
	}

	  protected List<String> getDefaultComponents() {
	    List<String> names = super.getDefaultComponents();
	    names.add(0, EncodeQueryComponent.COMPONENT_NAME);
	    return names;
	  }
}

package org.onetwo.common.tree;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties({"parent", "level", "index", "first", "last"})
public class DefaultTreeModel extends AbstractTreeModel<DefaultTreeModel> {

	public DefaultTreeModel() {
	    super();
    }

	public DefaultTreeModel(Object id, String name, Object parentId, Comparable<Object> sort) {
	    super(id, name, parentId, sort);
    }

	public DefaultTreeModel(Object id, String name, Object parentId) {
	    super(id, name, parentId);
    }

	public DefaultTreeModel(Object id, String name) {
	    super(id, name);
    }
	
	public void setSort(Comparable<?> sort){
		this.sort = sort;
	}
	
	public String getLabel() {
		return getName();
	}
 
}

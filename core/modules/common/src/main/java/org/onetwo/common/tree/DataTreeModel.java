package org.onetwo.common.tree;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties({"parent", "level", "index", "first", "last"})
public class DataTreeModel extends DefaultTreeModel {
	
	private Object data;

	public DataTreeModel() {
	    super();
    }

	public DataTreeModel(Object id, String name, Object parentId, Comparable<Object> sort) {
	    super(id, name, parentId, sort);
    }

	public DataTreeModel(Object id, String name, Object parentId) {
	    super(id, name, parentId);
    }
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}

package org.onetwo.boot.plugins.permission.entity;

import java.util.Arrays;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.tree.AbstractTreeModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties({"parent", "parentId", "sort", "level", "index"})
public class PermisstionTreeModel extends AbstractTreeModel<PermisstionTreeModel> {

	private String url;
	
	public PermisstionTreeModel() {
	    super();
    }

	public PermisstionTreeModel(Object id, String name, Object parentId, Comparable<Object> sort) {
	    super(id, name, parentId, sort);
    }

	public PermisstionTreeModel(Object id, String name, Object parentId) {
	    super(id, name, parentId);
    }

	public PermisstionTreeModel(Object id, String name) {
	    super(id, name);
    }

	public String getText(){
		return getName();
	}
	
	public String toJson(){
		return JsonMapper.IGNORE_NULL.toJson(Arrays.asList(this));
	}
	
	public String childrenToJson(){
		return JsonMapper.IGNORE_NULL.toJson(Arrays.asList(this.getChildren()));
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}

package org.onetwo.easyui;

import org.junit.Ignore;
import org.onetwo.common.utils.AbstractTreeModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties({"parent", "level", "index", "name", "leafage", "first", "last"})
public class EasyChildrenTreeModel extends AbstractTreeModel<EasyChildrenTreeModel>{

	public EasyChildrenTreeModel(){
	}
	
	public EasyChildrenTreeModel(Object id, String name, Object parentId, Comparable<Object> sort) {
		super(id, name, parentId, sort);
	}

	public EasyChildrenTreeModel(Object id, String name, Object parentId) {
		super(id, name, parentId);
	}
	
	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public Object getParentId() {
		return parentId;
	}

	public void setParentId(Object parentId) {
		this.parentId = parentId;
	}

	public String getText(){
		return name;
	}

	public void setText(String name){
		this.name = name;
	}
}

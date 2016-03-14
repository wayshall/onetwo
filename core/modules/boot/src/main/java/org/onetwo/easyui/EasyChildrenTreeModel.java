package org.onetwo.easyui;

import org.onetwo.common.tree.AbstractTreeModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties({"parent", "level", "index", "name", "leafage", "first", "last"})
public class EasyChildrenTreeModel extends AbstractTreeModel<EasyChildrenTreeModel>{

	private String state;
	private Boolean checked;
	
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	
}

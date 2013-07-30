package org.onetwo.common.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@SuppressWarnings({ "unchecked", "serial" })
@XmlType(name="TreeModel")
@XmlAccessorType(XmlAccessType.FIELD)
public class DefaultTreeModel implements Serializable, TreeModel<DefaultTreeModel> {
 
	protected Object id;

	protected String name;

	protected List<DefaultTreeModel> children = new ArrayList<DefaultTreeModel>();

	protected Object parentId;

	protected DefaultTreeModel parent;
	
	protected Comparable<Object> sort;
	
	protected int level = -1;
	protected int index;
	
	public DefaultTreeModel(){
	}

	public DefaultTreeModel(Object id, String name) {
		this(id, name, null);
	}

	public DefaultTreeModel(Object id, String name, Object parentId) {
		Assert.notNull(id, "id must not be null!");
		this.id = id;
		this.name = name;
		this.parentId = parentId;
		if(id instanceof Comparable)
			this.sort = (Comparable<Object>) id;
	}

	public DefaultTreeModel(Object id, String name, Object parentId, Comparable<Object> sort) {
		Assert.notNull(id, "id must not be null!");
		this.id = id;
		this.name = name;
		this.parentId = parentId;
		this.sort = sort;
	}

	public List<DefaultTreeModel> getChildren() {
		return children;
	}

	public void setChildren(List<DefaultTreeModel> children) {
		if(children==null)
			return ;
		for(DefaultTreeModel c : children){
			this.addChild(c);
		}
	}

	@Override
	public void addChild(DefaultTreeModel node) {
		node.setParent(this);
		node.setIndex(this.children.size());
		this.children.add(node);
	}
	
	public boolean isFirst(){
		return this.index==0;
	}
	
	public boolean isLast(){
		if(this.getParent()==null)
			return true;
		else
			return (this.index+1) == this.getParent().getChildren().size();
	}
	
	public DefaultTreeModel getChild(Object id){
		if(this.isLeafage())
			return null;
		for(DefaultTreeModel node : (List<DefaultTreeModel>)this.children){
			if(node.getId().equals(id))
				return node;
		}
		return null;
	}
	
	public DefaultTreeModel getNodeById(Object id){
		if(this.getId().toString().equals(id.toString()))
			return this;
		
		if(this.isLeafage())
			return null;

		DefaultTreeModel n = null;
		for(DefaultTreeModel node : (List<DefaultTreeModel>)this.children){
			n = node.getNodeById(id);
			if(n!=null)
				return n;
		}
		return null;
	}

	@Override
	public Object getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Comparable<Object> getSort() {
		return (Comparable<Object>) sort;
	}

	public Object getParentId() {
		return parentId;
	}

	public boolean isLeafage() {
		return this.children.isEmpty();
	}

	public DefaultTreeModel getParent() {
		return parent;
	}

	public void setParent(DefaultTreeModel parent) {
		this.parent = parent;
	}
	
	public boolean hasChildren(){
		return this.children!=null && !this.children.isEmpty();
	}

	@Override
	public boolean equals(Object o) {
		if(this==o)
			return true;
		if(!(o instanceof DefaultTreeModel))
			return false;
		DefaultTreeModel obj = (DefaultTreeModel) o;
		return new EqualsBuilder().append(this.getId(), obj.getId()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).toHashCode();
	}
	
	
	public int getLevel() {
		if(this.level!=-1)
			return this.level;
		int lv = 1;
		if(this.getParent()!=null)
			lv += this.getParent().getLevel();
		this.level = lv;
		return level;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<this.getLevel(); i++)
			sb.append("--");
		sb.append(">").append(getId()).append("|").append(getName()).append("\n");
		if(this.children==null || this.children.isEmpty())
			return sb.toString();
		for(DefaultTreeModel child : (List<DefaultTreeModel>)this.children){
			sb.append(child.toString());
		}
		return sb.toString();
	}
	
	public List<DefaultTreeModel> toList(){
		List<DefaultTreeModel> list = new ArrayList<DefaultTreeModel>();
		list.add(this);
		if(this.children==null)
			return list;
		for(DefaultTreeModel tm : this.children){
			list.addAll(tm.toList());
		}
		return list;
	}
	
}

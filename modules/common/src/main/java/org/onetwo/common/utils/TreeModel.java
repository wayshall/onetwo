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
public class TreeModel implements Comparable, Serializable {
 
	protected Object id;

	protected String name;

	protected List<TreeModel> children = new ArrayList<TreeModel>();

	protected Object parentId;

	protected TreeModel parent;
	
	protected Object sort;
	
	protected int level = -1;
	protected int index;
	
	public TreeModel(){
	}

	public TreeModel(Object id, String name) {
		this(id, name, null);
	}

	public TreeModel(Object id, String name, Object parentId) {
		Assert.notNull(id, "id must not be null!");
		this.id = id;
		this.name = name;
		this.parentId = parentId;
		if(id instanceof Comparable)
			this.sort = (Comparable) id;
	}

	public TreeModel(Object id, String name, Object parentId, Object sort) {
		Assert.notNull(id, "id must not be null!");
		this.id = id;
		this.name = name;
		this.parentId = parentId;
		this.sort = sort;
	}

	public List<TreeModel> getChildren() {
		return children;
	}

	public void setChildren(List<TreeModel> children) {
		if(children==null)
			return ;
		for(TreeModel c : children){
			this.addChild(c);
		}
	}

	public void addChild(TreeModel node) {
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
	
	public TreeModel getChild(Object id){
		if(this.isLeafage())
			return null;
		for(TreeModel node : (List<TreeModel>)this.children){
			if(node.getId().equals(id))
				return node;
		}
		return null;
	}
	
	public TreeModel getNodeById(Object id){
		if(this.getId().toString().equals(id.toString()))
			return this;
		
		if(this.isLeafage())
			return null;

		TreeModel n = null;
		for(TreeModel node : (List<TreeModel>)this.children){
			n = node.getNodeById(id);
			if(n!=null)
				return n;
		}
		return null;
	}

	public Object getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Comparable getSort() {
		return (Comparable) sort;
	}

	public Object getParentId() {
		return parentId;
	}

	public boolean isLeafage() {
		return this.children.isEmpty();
	}

	public TreeModel getParent() {
		return parent;
	}

	public void setParent(TreeModel parent) {
		this.parent = parent;
	}
	
	public boolean hasChildren(){
		return this.children!=null && !this.children.isEmpty();
	}

	@Override
	public int compareTo(Object tree2) {
		return getSort().compareTo(((TreeModel)tree2).getSort());
	}

	@Override
	public boolean equals(Object o) {
		if(this==o)
			return true;
		if(!(o instanceof TreeModel))
			return false;
		TreeModel obj = (TreeModel) o;
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
		for(TreeModel child : (List<TreeModel>)this.children){
			sb.append(child.toString());
		}
		return sb.toString();
	}
	
	public List<TreeModel> toList(){
		List<TreeModel> list = new ArrayList<TreeModel>();
		list.add(this);
		if(this.children==null)
			return list;
		for(TreeModel tm : this.children){
			list.addAll(tm.toList());
		}
		return list;
	}
	
}

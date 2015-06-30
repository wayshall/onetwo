package org.onetwo.common.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings({ "unchecked", "serial" })
@XmlType(name="TreeModel")
@XmlAccessorType(XmlAccessType.FIELD)
abstract public class AbstractTreeModel<T extends AbstractTreeModel<T>> implements Serializable, TreeModel<T> {
 
	protected Object id;

	protected String name;

	protected List<T> children = new ArrayList<T>();

	protected Object parentId;

	protected T parent;
	
	protected Comparable<Object> sort;
	
	protected int level = -1;
	protected int index;
	
	public AbstractTreeModel(){
	}

	public AbstractTreeModel(Object id, String name) {
		this(id, name, null);
	}

	public AbstractTreeModel(Object id, String name, Object parentId) {
		Assert.notNull(id, "id must not be null!");
		this.id = id;
		this.name = name;
		this.parentId = parentId;
		if(id instanceof Comparable)
			this.sort = (Comparable<Object>) id;
	}

	public AbstractTreeModel(Object id, String name, Object parentId, Comparable<Object> sort) {
		Assert.notNull(id, "id must not be null!");
		this.id = id;
		this.name = name;
		this.parentId = parentId;
		this.sort = sort;
	}

	public List<T> getChildren() {
		return children;
	}

	public void setChildren(List<T> children) {
		if(children==null)
			return ;
		for(T c : children){
			this.addChild(c);
		}
	}

	@Override
	public void addChild(T node) {
		node.setParent((T) this);
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
	
	public T getChild(Object id){
		if(this.isLeafage())
			return null;
		for(T node : (List<T>)this.children){
			if(node.getId().equals(id))
				return node;
		}
		return null;
	}
	
	public T getNodeById(Object id){
		if(this.getId().toString().equals(id.toString()))
			return (T) this;
		
		if(this.isLeafage())
			return null;

		T n = null;
		for(T node : (List<T>)this.children){
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

	public T getParent() {
		return parent;
	}

	public void setParent(T parent) {
		this.parent = parent;
	}
	
	public boolean hasChildren(){
		return this.children!=null && !this.children.isEmpty();
	}

	@Override
	public boolean equals(Object o) {
		if(this==o)
			return true;
		if(!(o instanceof AbstractTreeModel))
			return false;
		AbstractTreeModel obj = (AbstractTreeModel) o;
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
		for(T child : (List<T>)this.children){
			sb.append(child.toString());
		}
		return sb.toString();
	}
	
	public List<T> toList(){
		List<T> list = new ArrayList<T>();
		list.add((T) this);
		if(this.children==null)
			return list;
		for(T tm : this.children){
			list.addAll(tm.toList());
		}
		return list;
	}
	
}

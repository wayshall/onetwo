package org.onetwo.plugins.admin.model.vo;

import java.util.List;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.TreeModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ExtMenuModel implements TreeModel<ExtMenuModel> {
	
	private Object id;
	private String text;
	private String url;
	private Object parentId;
	private Integer sort;
	private List<ExtMenuModel> children;
	
	
	
	public ExtMenuModel(Object id, String text, String url) {
		this(id, text, url, null, null);
	}

	public ExtMenuModel(Object id, String text, String url, Object parentId,
			Integer sort) {
		super();
		this.id = id;
		this.text = text;
		this.url = url;
		this.parentId = parentId;
		this.sort = sort;
	}
	
	public void setParentId(Object parentId) {
		this.parentId = parentId;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getText(){
		return text;
	}

	public List<ExtMenuModel> getChildren() {
		if(LangUtils.isEmpty(this.children))
			return null;
		return children;
	}

	public void setChildren(List<ExtMenuModel> children) {
		this.children = children;
	}

	public boolean isLeaf(){
		return LangUtils.isEmpty(this.children);
	}
	
	public boolean isExpanded(){
		return true;
	}

	public String getUrl(){
		return url;
	}
	@Override
	public void addChild(ExtMenuModel node) {
		if(this.children==null)
			this.children = LangUtils.newArrayList();
		this.children.add(node);
	}

	@Override
	public Object getParentId() {
		return this.parentId;
	}

	@Override
	public Object getId() {
		return id;
	}

	@Override
	public String getName() {
		return text;
	}

	@Override
	public Comparable<Integer> getSort() {
		return sort;
	}
	
	@JsonIgnore
	public String getJsonString(){
		return JsonMapper.IGNORE_NULL.toJson(this);
	}

}

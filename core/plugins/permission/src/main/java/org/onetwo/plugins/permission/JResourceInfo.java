package org.onetwo.plugins.permission;

import java.util.List;

import org.onetwo.common.utils.LangUtils;

public class JResourceInfo {
	
	private final String id;
	private final String label;
	private String key;
	private boolean assembleTag;
//	private final boolean menu;
//	private final boolean permission;
	
	private JResourceInfo parent;
	private List<JResourceInfo> children;
	private List<JResourceInfo> assembles;
	
	public JResourceInfo(String id, String label) {
		super();
		this.id = id;
		this.label = label;
	}

	public String getId() {
		return id;
	}
	
	public String getLabel() {
		return label;
	}
	
	public boolean isMenu() {
		return false;
	}
	
	public boolean isPermission() {
		return false;
	}

	public void addAssemble(JResourceInfo res){
		if(assembles==null)
			assembles = LangUtils.newArrayList();
		this.assembles.add(res);
	}
	public void addChild(JResourceInfo res){
		if(children==null)
			children = LangUtils.newArrayList();
		this.children.add(res);
	}
	
	public JResourceInfo getParent() {
		return parent;
	}

	public void setParent(JResourceInfo parent) {
		this.parent = parent;
//		parent.addChild(this);
	}

	public List<JResourceInfo> getChildren() {
		return children;
	}

	public void setChildren(List<JResourceInfo> children) {
		this.children = children;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public List<JResourceInfo> getAssembles() {
		return assembles;
	}

	public void setAssembles(List<JResourceInfo> assembles) {
		this.assembles = assembles;
	}

	public boolean isAssembleTag() {
		return assembleTag;
	}

	public void setAssembleTag(boolean assembleTag) {
		this.assembleTag = assembleTag;
	}

	public String toString(){
		return getId();
	}
	
}

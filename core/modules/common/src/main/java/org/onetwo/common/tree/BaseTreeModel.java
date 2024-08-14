package org.onetwo.common.tree;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;

/**
 * @author weishao zeng
 * <br/>
 */
@JsonIgnoreProperties({"parent", "level", "index", "first", "last", "text"})
abstract public class BaseTreeModel<T> implements TreeModel<T> {
	
	protected List<T> children;

	@Override
	public void addChild(T node) {
		if (children==null) {
			children = Lists.newArrayList();
		}
		children.add(node);
	}

	@Override
	public List<T> getChildren() {
		return children;
	}
	
//	public String getText() {
//		return getName();
//	}

}

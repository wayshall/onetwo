package org.onetwo.plugins.admin.vo;

import java.util.Map;

import org.onetwo.common.tree.AbstractTreeModel;
import org.onetwo.common.utils.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Maps;

/**
 * @author wayshall
 * <br/>
 */
@SuppressWarnings("serial")
@JsonIgnoreProperties({"id", "parent", "parentId", "sort", "level", "index", "leafage", "first", "last"})
public class VueRouterTreeModel extends AbstractTreeModel<VueRouterTreeModel> {
	private String url;
	//menu is false, permission is true
	private boolean hidden;
	private Map<String, String> meta = Maps.newHashMap();

	public VueRouterTreeModel(String id, String title, String parentId) {
		super(id, id, parentId);
		meta.put("title", name);
	}
	
	public String getPath() {
		String path = (String)getId();
		String parentId = (String)getParentId();
		if(StringUtils.isNotBlank(parentId)) {
			path = path.substring(parentId.length()+1);
		}
		return path;
	}
	
	public String getRedirect() {
		if(getChildren().isEmpty()) {
			return null;
		}
		return "noredirect";
	}
	
	public String getComponentViewPath() {
		if(hidden) {
			return null;
		}
		String id = (String) getId();
		return "@/views/" + id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	public void setIcon(String icon) {
		this.meta.put("icon", icon);
	}

}

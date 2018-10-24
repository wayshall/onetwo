package org.onetwo.plugins.admin.vo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

import org.onetwo.common.tree.AbstractTreeModel;
import org.onetwo.common.utils.GuavaUtils;
import org.onetwo.common.utils.LangUtils;
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
	@Getter
	@Setter
	private String url;
	//menu is false, permission is true
	private boolean hidden;
	@Getter
	@Setter
	private Map<String, Object> meta = Maps.newHashMap();

	public VueRouterTreeModel(String id, String title, String parentId) {
		super(id, id, parentId);
		meta.put("title", title);
	}
	
	public String getPath() {
		String path = (String)getId();
		/*
		String parentId = (String)getParentId();
		if(StringUtils.isNotBlank(parentId)) {
			path = path.substring(parentId.length()+1);
		}
		return "/"+path;
		*/
		path = "/" + StringUtils.replaceEach(path, "_", "/");
		return path; 
	}
	
	public String getRedirect() {
		if(getChildren().isEmpty()) {
			return null;
		}
		return "noredirect";
	}
	
	public String getComponentViewPath() {
		if(!getChildren().isEmpty()) {
			return "Layout";
		}
		
//		String viewPath = (String) getId();
//		viewPath = viewPath.replace('_', '/');
		List<String> viewPaths = GuavaUtils.splitAsStream((String) getId(), "_")
										.map(str->StringUtils.uncapitalize(str))
										.collect(Collectors.toList());
		String viewPath = StringUtils.join(viewPaths, "/");
		viewPath = StringUtils.trimStartWith(viewPath, "/");
		return viewPath;
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
	
	public void addMetas(Map<String, Object> meta){
		if(LangUtils.isEmpty(meta)){
			return ;
		}
		if(this.meta == null){
			this.meta = Maps.newHashMap();
		}
		this.meta.putAll(meta);
	}

}

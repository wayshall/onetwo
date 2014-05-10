package org.onetwo.plugins.admin.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.TreeBuilder;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.plugins.admin.model.service.MenuItemRegistry;
import org.onetwo.plugins.admin.model.vo.ExtMenuModel;
import org.onetwo.plugins.admin.utils.AdminPluginConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("index")
public class AdminController extends PluginSupportedController {

	@Resource
	private MenuItemRegistry menuItemRegistry;

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView ext(String theme, UserDetail userDetail){
		if(StringUtils.isBlank(theme))
			theme = "neptune";
		
		Collection<ExtMenuModel> menus = null;
		
		if(userDetail==null){
			menus = menuItemRegistry.findAllMenus();
		}else{
			menus = menuItemRegistry.findUserMenus(userDetail);
		}
		
		final List<ExtMenuModel> extMenus = new ArrayList<ExtMenuModel>(menus);
		TreeBuilder<ExtMenuModel> builder = new TreeBuilder<ExtMenuModel>(extMenus);
		List<ExtMenuModel> menuTree = builder.buidTree();

		String title = AdminPluginConfig.getInstance().getTitle();
		String viewName = AdminPluginConfig.getInstance().getAdminView();
		if(StringUtils.isBlank(viewName))
			viewName = pluginView("manage-ext");
		
		if(menuTree.isEmpty())
			return mv(viewName, "treePanelDatas", "[]", "title", title);
		
		ExtMenuModel root = menuTree.get(0);
		
//		ObjectMapper om = FastUtils.jsonMapperIgnoreNull().getObjectMapper();
//		ArrayNode array = om.createArrayNode();
		StringBuilder treePanelDatas = new StringBuilder();
		treePanelDatas.append("[");
		int index = 0;
		List<ExtMenuModel> children = root.getChildren();
		for(ExtMenuModel child : children){
//			ObjectNode on = om.createObjectNode();
			if(index!=0)
				treePanelDatas.append(", ");
			treePanelDatas.append("{");
			treePanelDatas.append("title:\"").append(child.getText()).append("\",");
			treePanelDatas.append("iconCls:\"nav\",");
			treePanelDatas.append("xtype:\"treepanel\",");
			treePanelDatas.append("listeners:{ itemclick: clickMenuItem },");
			treePanelDatas.append("store: {root: ").append(child.getJsonString()).append("}");
			treePanelDatas.append("}");
//			on.put("iconCls", "nav");
//			on.put("width", 200);
//			on.put("xtype", "treepanel");
//			on.put("containerScroll", false);
//			on.put("autoScroll", false);
//			on.put("listeners", );
//			on.put("store", on.POJONode(on.put("root", on.POJONode(child))));
//			array.add(on);
			index++;
		}
		treePanelDatas.append("]");
//		String treePanelDatas = FastUtils.jsonMapperIgnoreNull().toJson(array);
//		System.out.println("json: " + treePanelDatas);
		
		return mv(viewName, "root", root, "treePanelDatas", treePanelDatas, "theme", theme, "title", title);
	}
}

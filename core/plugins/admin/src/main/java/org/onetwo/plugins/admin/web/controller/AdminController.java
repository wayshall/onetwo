package org.onetwo.plugins.admin.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.exception.NotLoginException;
import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.TreeBuilder;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.s2.security.config.annotation.Authentic;
import org.onetwo.plugins.admin.AdminPlugin;
import org.onetwo.plugins.admin.model.app.service.MenuItemRegistry;
import org.onetwo.plugins.admin.model.vo.ExtMenuModel;
import org.onetwo.plugins.admin.utils.AdminPluginConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("index")
public class AdminController extends PluginSupportedController {

	@Resource
	private MenuItemRegistry menuItemRegistry;
	
	private AdminPluginConfig config = AdminPlugin.getInstance().getConfig();

	@Authentic(checkLogin=true, ignore=true, redirect="")
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView ext(String theme, UserDetail userDetail){
		if(StringUtils.isBlank(theme))
			theme = config.getTheme();
		
		Collection<ExtMenuModel> menus = null;
		
		if(userDetail==null){
			if(BaseSiteConfig.getInstance().isDev())
				menus = menuItemRegistry.findAllMenus();
			else
				throw new NotLoginException();
		}else{
			menus = menuItemRegistry.findUserMenus(userDetail);
		}
		menus = CUtils.emptyIfNull(menus);
		final List<ExtMenuModel> extMenus = new ArrayList<ExtMenuModel>(menus);
		TreeBuilder<ExtMenuModel> builder = new TreeBuilder<ExtMenuModel>(extMenus);
		List<ExtMenuModel> menuTree = builder.buidTree();

//		String title = AdminPluginConfig.getInstance().getTitle();
		String viewName = config.getAdminView();
		if(StringUtils.isBlank(viewName))
			viewName = pluginView("manage-ext");
		
		if(menuTree.isEmpty())
			return mv(viewName, "treePanelDatas", "[]");
		
		ExtMenuModel root = menuTree.get(0);
		String treePanelDatas = config.isSinglePanel()?root.getTreePanel():root.getChildrenAsTreePanel();
//		ObjectMapper om = FastUtils.jsonMapperIgnoreNull().getObjectMapper();
//		ArrayNode array = om.createArrayNode();
		
//		String treePanelDatas = FastUtils.jsonMapperIgnoreNull().toJson(array);
//		System.out.println("json: " + treePanelDatas);
		
		return mv(viewName, "root", root, "treePanelDatas", treePanelDatas, "theme", theme);
	}
	
}

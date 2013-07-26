package org.onetwo.plugins.fmtagext.ui.datagrid;

import java.util.Collection;
import java.util.List;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.fmtagext.ui.FmUIComponent;
import org.onetwo.plugins.fmtagext.ui.UI.TemplateKeys;
import org.onetwo.plugins.fmtagext.ui.UIComponent;

public class MenuBarUI extends FmUIComponent {
	
	public static enum MenuType {
		builtin,
		template,
		defined;
	}
	
//	public static class Menu {
////		public static Menu create = new Menu(MenuType.builtin, "ui-menu-bar-create.ftl");
////		public static Menu delete = new Menu(MenuType.builtin, "ui-menu-bar-delete.ftl");
//		
//		private String template;
//		private MenuType type;
//
//		private Menu(MenuType toolBarMenuType, String template) {
//			this.type = toolBarMenuType;
//			if(MenuType.builtin==type){
//				this.template = FmtagextPlugin.PLUGIN_PATH + "lib/bootstrap/"+template;
//			}else{
//				this.template = template;
//			}
//			
//		}
//
//		public String getTemplate() {
//			return template;
//		}
//		
//		public boolean isTemplateMenu(){
//			return MenuType.builtin==type || MenuType.template == type;
//		}
//		
//		public boolean isDefineMenu(){
//			return MenuType.defined == type;
//		}
//		
//	}
	
	private Collection<UIComponent> menus;

	public MenuBarUI(UIComponent...menus) {
		this(null, menus);
	}

	public MenuBarUI(DataGridUI datagrid, UIComponent...menus) {
		super(datagrid, TemplateKeys.UI_MENU_BAR);
		List<UIComponent> list = LangUtils.newArrayList();
		for(UIComponent mu : menus){
//			mu.setParent(this);
			list.add(mu);
		}
		this.menus = list;
	}

	public DataGridUI getDatagrid() {
		return (DataGridUI)getParent();
	}

	public Collection<UIComponent> getMenus() {
		return menus;
	}

	public MenuBarUI addMenu(FmUIComponent menu) {
		this.menus.add(menu);
		return this;
	}
	
	public boolean hasToolbarMenu(){
		return !this.menus.isEmpty();
	}
	

}

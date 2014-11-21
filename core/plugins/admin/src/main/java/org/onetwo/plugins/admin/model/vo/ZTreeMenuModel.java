package org.onetwo.plugins.admin.model.vo;

import java.util.Collection;
import java.util.List;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.plugins.admin.model.entity.AdminFunctionEntity;
import org.onetwo.plugins.admin.model.entity.AdminMenuEntity;
import org.onetwo.plugins.admin.model.entity.AdminPermissionEntity;
import org.onetwo.plugins.permission.entity.IMenu;

public class ZTreeMenuModel {
	
	public static List<ZTreeMenuModel> asZtree(Collection<? extends AdminPermissionEntity> permlist, List<String> checkedPermissions){
		List<ZTreeMenuModel> trees = LangUtils.newArrayList();
		for(AdminPermissionEntity perm : permlist){
			trees.add(new ZTreeMenuModel(perm, checkedPermissions));
		}
		return trees;
	}
	protected AdminPermissionEntity permission;
	protected List<String> checkIeds;
	

	public ZTreeMenuModel(AdminPermissionEntity menu) {
		this(menu, null);
	}
	public ZTreeMenuModel(AdminPermissionEntity menu, List<String> checkIeds) {
		super();
		this.permission = menu;
		this.checkIeds = checkIeds;
	}
	public String getId(){
//		return permission.getId();
		return permission.getCode();
	}
	
	public String getPid(){
		if(AdminMenuEntity.class.isInstance(permission)){
			AdminMenuEntity menu = (AdminMenuEntity) permission;
//			return menu.getParent()!=null?menu.getParent().getId():0L;
			return menu.getParent()!=null?menu.getParent().getCode():"";
		}else{
			AdminFunctionEntity page = (AdminFunctionEntity) permission;
//			return page.getMenu()!=null?page.getMenu().getId():0L;
			return page.getMenu()!=null?page.getMenu().getCode():"";
		}
	}
	
	public String getName(){
		if(AdminMenuEntity.class.isInstance(permission)){
			AdminMenuEntity menu = (AdminMenuEntity) permission;
			return menu.getName();
		}else{
			AdminFunctionEntity page = (AdminFunctionEntity) permission;
			return page.getName();
		}
	}
	
	public boolean isOpen(){
		return true;
	}
	
	public boolean isIsHidden(){
		return permission.isHidden()!=null && permission.isHidden();
	}
	public String getMenuUrl(){
		if(!AdminMenuEntity.class.isInstance(permission))
			return "";
		return BaseSiteConfig.getInstance().getBaseURL()+ ((IMenu)permission).getUrl();
	}
	
	public boolean isChecked(){
		return checkIeds!=null && checkIeds.contains(getId());
	}
	
	public String getTarget(){
		return "iframe";
	}

}

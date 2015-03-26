package org.onetwo.plugins.permission;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.PermissionDetail;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.UserDetail;

final public class PermissionUtils {
	
	public static final String CLASS_PREFIX = "class:";
	public static final String PLUGIN_PREFIX = "plugin:";
	public static final String DOLOR = "$";
	public static final String UNDERLINE = "_";
	
	private static MenuInfoParser menuInfoParser;
	
	public static void setMenuInfoParser(MenuInfoParser menuInfoParser) {
		PermissionUtils.menuInfoParser = menuInfoParser;
	}

	public static boolean hasPermission(String permissionCode){
		if(StringUtils.isBlank(permissionCode))
			return true;
		UserDetail userDetail = JFishWebUtils.getUserDetail();
		if(!PermissionDetail.class.isInstance(userDetail))
			return false;
		PermissionDetail p = (PermissionDetail) userDetail;
		return hasPermission(p, permissionCode);
	}
	
	public static boolean hasPermission(Class<?> codeClass){
		UserDetail userDetail = JFishWebUtils.getUserDetail();
		if(!PermissionDetail.class.isInstance(userDetail))
			return false;
		PermissionDetail p = (PermissionDetail) userDetail;
		return hasPermission(p, codeClass);
	}
	
	public static boolean hasPermission(PermissionDetail userDetail, String permissionCode){
		if(permissionCode.startsWith(CLASS_PREFIX)){
			Class<?> permClass = null;
			try {
				permClass = ReflectUtils.loadClass(permissionCode.substring(CLASS_PREFIX.length()));
			} catch (Exception e) {
				throw new BaseException("no permission class found: " + e.getMessage());
			}
			return hasPermission(userDetail, permClass);
			
		}else if(permissionCode.startsWith(PLUGIN_PREFIX)){
			Class<?> permClass = null;
			PluginSupportedController controller = JFishWebUtils.currentController();
			if(controller!=null){
				String clsName = controller.getPluginMeta().getRootClass().getPackage().getName()+"."+permissionCode.substring(PLUGIN_PREFIX.length());
				try {
					permClass = ReflectUtils.loadClass(clsName);
				} catch (Exception e) {
					throw new BaseException("no permission class found: " + e.getMessage());
				}
			}
			return hasPermission(userDetail, permClass);
		}
		return userDetail.getPermissions()!=null && userDetail.getPermissions().contains(permissionCode);
	}
	
	public static boolean hasPermission(PermissionDetail userDetail, Class<?> codeClass){
		String permissionCode = menuInfoParser.getCode(codeClass);
		return userDetail.getPermissions()!=null && userDetail.getPermissions().contains(permissionCode);
	}
	
	
	private PermissionUtils(){}

}

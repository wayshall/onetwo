package org.onetwo.plugins.permission;

import org.onetwo.common.utils.Closure;
import org.onetwo.plugins.permission.entity.MenuEntity;
import org.onetwo.plugins.permission.entity.FunctionEntity;
import org.onetwo.plugins.permission.entity.PermissionEntity;

final public class MenuUtils {
	public static interface BuildBlock {
		public void execute(StringBuilder str, PermissionEntity perm);
	}

	public static void buildString(StringBuilder str, PermissionEntity node, String sp){
		buildString(str, node, sp, null);
	}
	public static void buildString(StringBuilder str, PermissionEntity node, String sp, Closure<PermissionEntity> block){
		if(block!=null){
			block.execute(node);
		}else{
			str.append(node.getCode()).append("\n");
		}
		if(FunctionEntity.class.isInstance(node))
			return ;
		MenuEntity menu = (MenuEntity) node;
		if(menu.getChildren()!=null){
			for(MenuEntity cnode : menu.getChildren()){
				str.append(sp);
				String newsp = sp + sp;
				buildString(str, cnode, newsp, block);
			}
		}
		if(menu.getFunctions()!=null){
			for(FunctionEntity cnode : menu.getFunctions()){
				str.append(sp);
				String newsp = sp + sp;
				buildString(str, cnode, newsp, block);
			}
		}
	}
	
	private MenuUtils(){}

}

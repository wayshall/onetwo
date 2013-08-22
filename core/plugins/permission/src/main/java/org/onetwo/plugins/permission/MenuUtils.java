package org.onetwo.plugins.permission;

import org.onetwo.common.utils.Closure;
import org.onetwo.plugins.permission.entity.IFunction;
import org.onetwo.plugins.permission.entity.IMenu;
import org.onetwo.plugins.permission.entity.IPermission;

final public class MenuUtils {
	public static interface BuildBlock {
		public void execute(StringBuilder str, IPermission perm);
	}

	public static void buildString(StringBuilder str, IPermission node, String sp){
		buildString(str, node, sp, null);
	}
	public static void buildString(StringBuilder str, IPermission node, String sp, Closure<IPermission> block){
		if(block!=null){
			block.execute(node);
		}else{
			str.append(node.getCode()).append("\n");
		}
		if(IFunction.class.isInstance(node))
			return ;
		IMenu<?, ?> menu = (IMenu<?, ?>) node;
		if(menu.getChildren()!=null){
			for(IMenu cnode : menu.getChildren()){
				str.append(sp);
				String newsp = sp + sp;
				buildString(str, cnode, newsp, block);
			}
		}
		if(menu.getFunctions()!=null){
			for(IFunction<?> cnode : menu.getFunctions()){
				str.append(sp);
				String newsp = sp + sp;
				buildString(str, cnode, newsp, block);
			}
		}
	}
	
	private MenuUtils(){}

}

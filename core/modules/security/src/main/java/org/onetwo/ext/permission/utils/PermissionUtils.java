package org.onetwo.ext.permission.utils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.onetwo.common.tree.TreeBuilder;
import org.onetwo.common.tree.TreeModel;
import org.onetwo.common.utils.func.Closure1;
import org.onetwo.ext.permission.api.IPermission;
import org.onetwo.ext.permission.api.PermissionType;
import org.onetwo.ext.permission.entity.PermisstionTreeModel;


final public class PermissionUtils {
	public static interface BuildBlock {
		public void execute(StringBuilder str, IPermission perm);
	}

	public static void buildString(StringBuilder str, IPermission node, String sp){
		buildString(str, node, sp, null);
	}
	
	public static boolean isFunction(IPermission node){
		return getPermissionType(node)==PermissionType.FUNCTION;
	}
	
	public static boolean isMenu(IPermission node){
		return getPermissionType(node)==PermissionType.MENU;
	}
	
	public static PermissionType getPermissionType(IPermission node){
		/*if(node==null)
			return PermissionType.RESOURCE;
		return PermissionType.of(node.getPtype());*/
		return node.getPermissionType();
	}
	public static void buildString(StringBuilder str, IPermission node, String sp, Closure1<IPermission> block){
		if(block!=null){
			block.execute(node);
		}else{
			str.append(node.getCode()).append(sp);
		}
		if(!isMenu(node))
			return ;
		IPermission menu = node;
		List<IPermission> permlist = menu.getChildrenMenu();
		if(permlist!=null){
			for(IPermission cnode : permlist){
				str.append(sp);
				String newsp = sp + sp;
				buildString(str, cnode, newsp, block);
			}
		}
		permlist = menu.getChildrenWithouMenu();
		if(permlist!=null){
			for(IPermission cnode : permlist){
				str.append(sp);
				String newsp = sp + sp;
				buildString(str, cnode, newsp, block);
			}
		}
	}
	
	public static TreeBuilder<PermisstionTreeModel> createMenuTreeBuilder(List<? extends IPermission> permissions){
		return createMenuTreeBuilder(permissions, p->{
			PermisstionTreeModel pm = new PermisstionTreeModel(p.getCode(), p.getName(), p.getParentCode());
			pm.setSort(p.getSort());
			pm.setUrl(p.getUrl());
			return pm;
		});
	}
	
	public static <T extends TreeModel<T>> TreeBuilder<T> createMenuTreeBuilder(List<? extends IPermission> permissions, Function<IPermission, T> treeModelCreator){
		List<T> pmlist = permissions.stream().map(p->{
//			PermisstionTreeModel pm = new PermisstionTreeModel(p.getCode(), p.getName(), p.getParentCode());
			T pm = treeModelCreator.apply(p);
			return pm;
		}).collect(Collectors.toList());
		
		TreeBuilder<T> builder = new TreeBuilder<>(pmlist);
	    return builder;
	}
	
	private PermissionUtils(){}

}

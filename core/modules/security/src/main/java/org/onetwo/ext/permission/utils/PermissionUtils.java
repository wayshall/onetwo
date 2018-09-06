package org.onetwo.ext.permission.utils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.onetwo.common.tree.TreeBuilder;
import org.onetwo.common.utils.func.Closure1;
import org.onetwo.ext.permission.api.PermissionType;
import org.onetwo.ext.permission.entity.DefaultIPermission;
import org.onetwo.ext.permission.entity.PermisstionTreeModel;

final public class PermissionUtils {
	public static interface BuildBlock {
		public void execute(StringBuilder str, DefaultIPermission<?> perm);
	}

	public static <T extends DefaultIPermission<T>> void buildString(StringBuilder str, T node, String sp){
		buildString(str, node, sp, null);
	}
	
	public static boolean isFunction(DefaultIPermission<?> node){
		return getPermissionType(node)==PermissionType.FUNCTION;
	}
	
	public static boolean isMenu(DefaultIPermission<?> node){
		return getPermissionType(node)==PermissionType.MENU;
	}
	
	public static PermissionType getPermissionType(DefaultIPermission<?> node){
		/*if(node==null)
			return PermissionType.RESOURCE;
		return PermissionType.of(node.getPtype());*/
		return node.getPermissionType();
	}
	public static <T extends DefaultIPermission<T>> void buildString(StringBuilder str, T node, String sp, Closure1<T> block){
		if(block!=null){
			block.execute(node);
		}else{
			str.append(node.getCode()).append(sp);
		}
		if(!isMenu(node))
			return ;
		T menu = node;
		List<T> permlist = menu.getChildrenMenu();
		if(permlist!=null){
			for(T cnode : permlist){
				str.append(sp);
				String newsp = sp + sp;
				buildString(str, cnode, newsp, block);
			}
		}
		permlist = menu.getChildrenWithouMenu();
		if(permlist!=null){
			for(T cnode : permlist){
				str.append(sp);
				String newsp = sp + sp;
				buildString(str, cnode, newsp, block);
			}
		}
	}
	
	public static TreeBuilder<PermisstionTreeModel> createMenuTreeBuilder(List<? extends DefaultIPermission<?>> permissions){
		return createMenuTreeBuilder(permissions, p->new PermisstionTreeModel(p.getCode(), p.getName(), p.getParentCode()));
	}
	
	public static TreeBuilder<PermisstionTreeModel> createMenuTreeBuilder(List<? extends DefaultIPermission<?>> permissions, Function<DefaultIPermission<?>, PermisstionTreeModel> treeModelCreator){
		List<PermisstionTreeModel> pmlist = permissions.stream().map(p->{
//			PermisstionTreeModel pm = new PermisstionTreeModel(p.getCode(), p.getName(), p.getParentCode());
			PermisstionTreeModel pm = treeModelCreator.apply(p);
			pm.setSort(p.getSort());
			pm.setUrl(p.getUrl());
			return pm;
		}).collect(Collectors.toList());
		
		TreeBuilder<PermisstionTreeModel> builder = new TreeBuilder<>(pmlist);
	    return builder;
	}
	
	private PermissionUtils(){}

}

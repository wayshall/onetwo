package org.onetwo.plugins.permission;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.utils.JFishResourcesScanner;
import org.onetwo.common.spring.utils.ResourcesScanner;
import org.onetwo.common.spring.utils.ScanResourcesCallback;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.map.CasualMap;
import org.onetwo.plugins.permission.anno.MenuMapping;
import org.onetwo.plugins.permission.entity.IFunction;
import org.onetwo.plugins.permission.entity.IMenu;
import org.onetwo.plugins.permission.entity.IPermission;
import org.onetwo.plugins.permission.entity.PermissionType;
import org.springframework.core.type.classreading.MetadataReader;

public class DefaultMenuInfoParser implements MenuInfoParser {
	private static final String CODE_SEPRATOR = "_";
	public static final Class<?> ROOT_MENU_TAG = MenuInfoParser.class;

	private final Map<String, IPermission> permissionMap = new LinkedHashMap<String, IPermission>(50);
	private final Map<Class<?>, IPermission> permissionMapByClass = new LinkedHashMap<Class<?>, IPermission>(50);
	private final Map<Class<?>, PermClassParser> permClassParserMap = new LinkedHashMap<Class<?>, PermClassParser>(50);
	
	private IMenu<? extends IMenu<?, ?> , ? extends IFunction<?>> rootMenu;
	private final ResourcesScanner scaner = new JFishResourcesScanner();

	@Resource
	private PermissionConfig menuInfoable;
	private int sortStartIndex = 1000;
	

	public Map<String, ? extends IPermission> getPermissionMap() {
		return permissionMap;
	}
	
	public String getRootMenuCode(){
		return parseCode(menuInfoable.getRootMenuClass());
	}

	/* (non-Javadoc)
	 * @see org.onetwo.plugins.permission.MenuInfoParser#parseTree()
	 */
	@Override
	public IMenu<?, ?> parseTree(){
		Assert.notNull(menuInfoable);
		Class<?> menuInfoClass = menuInfoable.getRootMenuClass();

		String appCode = null;
		IPermission perm = null;
		try {
//			appCode = getFieldValue(menuInfoClass, MenuMetaFields.APP_CODE, String.class, menuInfoClass.getSimpleName());
			appCode = PermClassParser.create(menuInfoClass).getAppCode();
			/*if(StringUtils.isBlank(sysname)){
				throw new BaseException("RootMenuClass must has a sysname field and it's value can not be blank.");
			}*/
			perm = parseMenuClass(menuInfoClass, appCode);
			perm.setSort(1);
		} catch (Exception e) {
			throw new BaseException("parse tree error: " + e.getMessage(), e);
		}
		if(!IMenu.class.isInstance(perm))
			throw new BaseException("root must be a menu node");
		rootMenu = (IMenu<?, ?>)perm;
		
		String[] childMenuPackages = menuInfoable.getChildMenuPackages();
		if(LangUtils.isEmpty(childMenuPackages))
			return rootMenu;
		
		List<Class<?>> childMenuClass = scaner.scan(new ScanResourcesCallback<Class<?>>(){

			/*@Override
			public boolean isCandidate(MetadataReader metadataReader) {
				if (metadataReader.getAnnotationMetadata().hasAnnotation(MenuMapping.class.getName()))
					return true;
				return false;
			}*/

			@Override
			public Class<?> doWithCandidate(MetadataReader metadataReader, org.springframework.core.io.Resource resource, int count) {
				if (!metadataReader.getAnnotationMetadata().hasAnnotation(MenuMapping.class.getName()))
					return null;
				Class<?> cls = ReflectUtils.loadClass(metadataReader.getClassMetadata().getClassName());
				return cls;
			}
			
		}, childMenuPackages);
		
		if(LangUtils.isEmpty(childMenuClass))
			return rootMenu;
		
		for(Class<?> childMc : childMenuClass){
			IPermission childPerm =  parseMenuClass(childMc, appCode);
			if(IMenu.class.isInstance(childPerm)){
				rootMenu.addChild((IMenu)childPerm);
			}else{
				rootMenu.addFunction((IFunction)childPerm);
			}
		}
		
		return rootMenu;
	}
	

	protected <T extends IPermission> T parseMenuClass(Class<?> menuClass, String syscode){
		IPermission perm;
		try {
			if(menuClass.getAnnotation(Deprecated.class)!=null)
				return null;
			perm = parsePermission(getPermClassParser(menuClass), syscode);
		} catch (Exception e) {
			throw new BaseException("parser permission error: " + e.getMessage(), e);
		}
		if(perm instanceof IFunction)
			return (T)perm;
		
		IMenu<? extends IMenu<?, ?> , ? extends IFunction<?>> menu = (IMenu) perm;
		Class<?>[] childClasses = menuClass.getDeclaredClasses();
//		Arrays.sort(childClasses);
		for(Class<?> childCls : childClasses){
			IPermission p = parseMenuClass(childCls, syscode);
			if(p==null)
				continue;
			if(p instanceof IFunction){
				menu.addFunction((IFunction)p);
			}else{
				menu.addChild((IMenu)p);
			}
		}
		return (T)menu;
	}
	
	
	public IPermission parsePermission(PermClassParser parser, String syscode) throws Exception{
		/*PermClassParser parser = permClassParserMap.get(permissionClass);
		if(parser==null){
			parser = PermClassParser.create(permissionClass);
		}*/
		Class<?> permissionClass = parser.getPermissionClass();
		
		Number sort = parser.getSort();
		if(sort==null){
			sort = sortStartIndex++;
		}
		String name = parser.getName();
		IPermission perm = null;
		if(parser.getPermissionType()==PermissionType.FUNCTION){
			perm = (IPermission)ReflectUtils.newInstance(this.menuInfoable.getIFunctionClass());
		}else{
			IMenu<?, ?> menu = (IMenu<?, ?>)ReflectUtils.newInstance(this.menuInfoable.getIMenuClass());
			Map<?, ?> param = parser.getParams();//getFieldValue(permissionClass, MenuMetaFields.PARAMS, Map.class, Collections.EMPTY_MAP);
			CasualMap casualmap = new CasualMap().addMapWithFilter(param);
			menu.setUrl(casualmap.toParamString());
			perm = menu;
		}
		perm.setName(name);
		String code = parseCode(permissionClass);
		perm.setCode(code);
		perm.setSort(sort.intValue());
		perm.setHidden(parser.isHidden());
		perm.setAppCode(syscode);
		this.permissionMap.put(perm.getCode(), perm);
		this.permissionMapByClass.put(permissionClass, perm);
		return perm;
	}
	
	protected PermClassParser getPermClassParser(Class<?> permissionClass){
		PermClassParser parser = permClassParserMap.get(permissionClass);
		if(parser==null){
			parser = PermClassParser.create(permissionClass);
			this.permClassParserMap.put(permissionClass, parser);
		}
		return parser;
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.plugins.permission.MenuInfoParser#parseCode(java.lang.Class)
	*/
	@Override
	public String parseCode(Class<?> menuClass){
		return parseCode(getPermClassParser(menuClass));
	}
	
	public String parseCode(PermClassParser parser){
		Class<?> menuClass = parser.getPermissionClass();
		
		String code = menuClass.getSimpleName();
		while(parser.getParentPermClass()!=null){
//			while(menuClass.getDeclaringClass()!=null){
			menuClass = parser.getParentPermClass();//menuClass.getDeclaringClass();
			code = menuClass.getSimpleName() + CODE_SEPRATOR + code;a
		}
		MenuMapping mapping = menuClass.getAnnotation(MenuMapping.class);
		if(mapping!=null){
			Class<?> pcls = mapping.parent()==ROOT_MENU_TAG?menuInfoable.getRootMenuClass():mapping.parent();
			IPermission perm = this.permissionMapByClass.get(pcls);
			if(perm==null)
				throw new BaseException("parse menu class["+menuClass+"] error. no parent menu found: " + pcls);
			code = perm.getCode() + CODE_SEPRATOR + code;
		}
		return code;
	}
	
	public IPermission getPermission(Class<?> clazz){
		return this.permissionMapByClass.get(clazz);
	}
	
	public IPermission getMenuNode(String code){
		return (IPermission)permissionMap.get(code);
	}

	public IMenu getRootMenu() {
		return rootMenu;
	}

	public void setRootMenu(IMenu rootMenu) {
		this.rootMenu = rootMenu;
	}

	public PermissionConfig getMenuInfoable() {
		return menuInfoable;
	}
	
	
}

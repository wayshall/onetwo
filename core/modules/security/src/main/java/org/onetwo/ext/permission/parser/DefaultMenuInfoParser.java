package org.onetwo.ext.permission.parser;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.utils.JFishResourcesScanner;
import org.onetwo.common.spring.utils.ResourcesScanner;
import org.onetwo.common.spring.utils.ScanResourcesCallback;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.map.ParamMap;
import org.onetwo.ext.permission.api.DataFrom;
import org.onetwo.ext.permission.api.PermissionConfig;
import org.onetwo.ext.permission.api.PermissionType;
import org.onetwo.ext.permission.api.annotation.MenuMapping;
import org.onetwo.ext.permission.entity.DefaultIPermission;
import org.onetwo.ext.permission.utils.PermissionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.type.classreading.MetadataReader;

public class DefaultMenuInfoParser<P extends DefaultIPermission<P>> implements MenuInfoParser<P>, InitializingBean {
	private final Logger logger = JFishLoggerFactory.logger(this.getClass());
	
	private static final String CODE_SEPRATOR = "_";
	public static final Class<?> ROOT_MENU_TAG = Object.class;
//	private static final int DEFAULT_SORT = 10000;

	private final int INIT_SIZE = 300;
	private final Map<String, P> permissionMap;
	private final Map<Class<?>, P> permissionMapByClass;
	private final Map<Class<?>, PermClassParser> permClassParserMap;
	
	private P rootMenu;
	private Integer firstNodeSort;
	private final ResourcesScanner scaner = new JFishResourcesScanner();

//	@Resource
	private PermissionConfig<P> permissionConfig;
//	private int sortStartIndex = 1000;
	
	private boolean parsed = false;
	
	
	
	public DefaultMenuInfoParser(PermissionConfig<P> permissionConfig) {
	    super();
	    this.permissionMap = new LinkedHashMap<>(INIT_SIZE);
	    this.permissionMapByClass = new LinkedHashMap<>(INIT_SIZE);
	    this.permClassParserMap = new LinkedHashMap<Class<?>, PermClassParser>(INIT_SIZE);
	    this.permissionConfig = permissionConfig;
	    this.firstNodeSort = this.generatedSort();
    }
	

	private Integer generatedSort(){
		String str = String.valueOf(new Date().getTime());
		Integer sort = Integer.parseInt(str.substring(str.length()-4, str.length()));
		return sort;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.parseTree();
	}
	public Map<String, P> getPermissionMap() {
		return Collections.unmodifiableMap(permissionMap);
	}
	
	public String getRootMenuCode(){
		return getCode(permissionConfig.getRootMenuClass());
	}
	
	public void clear(){
		this.permissionMap.clear();
		this.permissionMapByClass.clear();
		this.permClassParserMap.clear();
	}

	/* (non-Javadoc)
	 * @see org.onetwo.plugins.permission.MenuInfoParser#parseTree()
	 */
	@Override
	public synchronized P parseTree(){
		Assert.notNull(permissionConfig);
		
		if(parsed){
			return rootMenu;
		}
		
		this.clear();
		
		Class<?> menuInfoClass = permissionConfig.getRootMenuClass();
		
		List<PermClassParser> childMenuParser = LangUtils.newArrayList();

		String appCode = null;
		P perm = null;
		PermClassParser parser = getPermClassParser(menuInfoClass);
		try {
//			appCode = getFieldValue(menuInfoClass, MenuMetaFields.APP_CODE, String.class, menuInfoClass.getSimpleName());
			appCode = parser.getAppCode();
			/*if(StringUtils.isBlank(sysname)){
				throw new BaseException("RootMenuClass must has a sysname field and it's value can not be blank.");
			}*/
			perm = parseMenuClass(parser, appCode);
			perm.setSort(1);
		} catch (Exception e) {
			throw new BaseException("parse tree error: " + e.getMessage(), e);
		}
		if(!PermissionUtils.isMenu(perm))
			throw new BaseException("root must be a menu node : " + perm.getCode());
		rootMenu = perm;
		
		String[] childMenuPackages = permissionConfig.getChildMenuPackages();
		if(LangUtils.isEmpty(childMenuPackages)){
			this.afterParseTree();
			return rootMenu;
		}
		
		Collection<PermClassParser> childMenuPackageMenus = scaner.scan(new ScanResourcesCallback<PermClassParser>(){

			/*@Override
			public boolean isCandidate(MetadataReader metadataReader) {
				if (metadataReader.getAnnotationMetadata().hasAnnotation(MenuMapping.class.getName()))
					return true;
				return false;
			}*/

			@Override
			public PermClassParser doWithCandidate(MetadataReader metadataReader, org.springframework.core.io.Resource resource, int count) {
				if (!metadataReader.getAnnotationMetadata().hasAnnotation(MenuMapping.class.getName()))
					return null;
				Class<?> cls = ReflectUtils.loadClass(metadataReader.getClassMetadata().getClassName());
				return getPermClassParser(cls, null, true);
			}
			
		}, childMenuPackages);
		
		childMenuParser.addAll(childMenuPackageMenus);
		if(LangUtils.isEmpty(childMenuParser)){
			this.afterParseTree();
			return rootMenu;
		}
		
		for(PermClassParser childMc : childMenuParser){
			P childPerm =  parseMenuClass(childMc, appCode);
			rootMenu.addChild(childPerm);
			/*if(MenuUtils.isMenu(childPerm)){
				rootMenu.addChild(childPerm);
			}else{
				rootMenu.addFunction((IFunction)childPerm);
			}*/
		}

		this.afterParseTree();
		return rootMenu;
	}
	
	private void afterParseTree(){
		this.parsed = true;
//		logger.info("menu:\n" + rootMenu.toTreeString("\n"));
	}
	

	protected P parseMenuClass(PermClassParser parser, String syscode){
		P perm;
//		Class<?> menuClass = parser.getPermissionClass();
//		PermClassParser menuParser = getPermClassParser(menuClass);
		try {
			if(parser.isDeprecated())
				return null;
			perm = parsePermission(parser, syscode);
		} catch (Exception e) {
			throw new BaseException("parser permission error: " + e.getMessage(), e);
		}
		
		
		Class<?>[] childClasses = parser.getChildrenClasses();//menuClass.getDeclaredClasses();
		perm.setChildrenSize(childClasses.length);
		//如果是function类型，忽略解释children
//		if(perm instanceof IFunction)
		/*if(!PermissionUtils.isMenu(perm)){
			if(childClasses.length>0){
				throw new BaseException("the node with children must be a menu class: " + parser.getPermissionClass());
			}
			return perm;
		}*/

		P menu = perm;
//		Arrays.sort(childClasses);
		for(Class<?> childCls : childClasses){
			/*if(childCls.getName().startsWith("org.onetwo.plugins.admin.DataModule")){
				System.out.println("DataModule: " + childCls);
			}*/
			PermClassParser childParser = getPermClassParser(childCls, parser.getPermissionClass(), true);
//			childParser.setParentPermissionClass(parser.getPermissionClass());
			P p = parseMenuClass(childParser, syscode);
			if(p==null)
				continue;
			menu.addChild(p);
			/*if(p instanceof IFunction){
				menu.addFunction((IFunction)p);
			}else{
				menu.addChild((IMenu)p);
			}*/
		}
		return menu;
	}
	
	
	public P parsePermission(PermClassParser parser, String syscode) throws Exception{
		/*PermClassParser parser = permClassParserMap.get(permissionClass);
		if(parser==null){
			parser = PermClassParser.create(permissionClass);
		}*/
		Class<?> permissionClass = parser.getActualPermissionClass();//parser.getPermissionClass();
		/*if(permissionClass.getName().contains("org.onetwo.plugins.admin.DataModule$DictModule$List")){
			System.out.println("test");
		}*/
		
		String name = parser.getName();
		P perm = (P)ReflectUtils.newInstance(this.permissionConfig.getIPermissionClass());
		perm.setDataFrom(DataFrom.SYNC);
		perm.setPermissionType(parser.getPermissionType());
		if(parser.getPermissionType()==PermissionType.MENU){
			P menu = perm;
			Map<?, ?> param = parser.getParams();//getFieldValue(permissionClass, MenuMetaFields.PARAMS, Map.class, Collections.EMPTY_MAP);
			ParamMap casualmap = new ParamMap().addMapWithFilter(param);
			menu.setUrl(casualmap.toParamString());
			parser.setOptionFieldValue(menu, PermClassParser.MENU_CSS_CLASS, String.class, "");
			parser.setOptionFieldValue(menu, PermClassParser.MENU_SHOW_PROPS, String.class, "");
			perm = menu;
		}
		
		perm.setResourcesPattern(parser.getResourcesPattern());
		perm.setName(name);
		String code = parseCode(parser);
		/*if(code.endsWith("EstateUnitMgr")){
			System.out.println("test");
		}*/
		perm.setCode(code);
		//数字越小，排序越靠前
		Number sort = parser.getSort();
//		perm.setSort(sort==null?DEFAULT_SORT:sort.intValue());
		perm.setSort(sort==null?firstNodeSort+permClassParserMap.size():sort.intValue());
		perm.setHidden(parser.isHidden());
		perm.setAppCode(syscode);

		if(parser.getParentPermissionClass()!=null){
			String parentCode = parseCode(getPermClassParser(parser.getParentPermissionClass()));
			perm.setParentCode(parentCode);
		}
		this.permissionMap.put(perm.getCode(), perm);
		this.permissionMapByClass.put(permissionClass, perm);
		return perm;
	}
	

	protected PermClassParser getPermClassParser(Class<?> permissionClass){
		return getPermClassParser(permissionClass, permissionClass.getDeclaringClass(), false);
	}

	protected PermClassParser getPermClassParser(Class<?> permissionClass, Class<?> parentPermissionClass, boolean mustCreate){
		PermClassParser parser = permClassParserMap.get(permissionClass);
		if(parser==null){
			parser = PermClassParser.create(permissionClass, parentPermissionClass);
			this.permClassParserMap.put(permissionClass, parser);
		}else{
			if(mustCreate){
				throw new BaseException("the same PermissionClass["+permissionClass+"] has assigned to "+parser.getParentPermissionClass()+", can not assign to the other : " + parentPermissionClass);
			}
		}
		return parser;
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.plugins.permission.MenuInfoParser#parseCode(java.lang.Class)
	*/
	@Override
	public String getCode(Class<?> menuClass){
		P p = permissionMapByClass.get(menuClass);
		if(p==null){
			logger.info("root: " + rootMenu);
			throw new BaseException("no permission found : " + menuClass.getName());
		}
		String code = p.getCode();
		if(StringUtils.isBlank(code))
			throw new BaseException("no permission found : " + menuClass.getName());
		return code;
	}
	
/*
	public String parseCode(Class<?> menuClass){
		return parseCode(getPermClassParser(menuClass));
	}*/
	
	public String parseCode(PermClassParser parser){
		String code = parser.generatedSimpleCode();
		PermClassParser parent = parser;
		while(parent.getParentPermissionClass()!=null){
//			while(menuClass.getDeclaringClass()!=null){
//			menuClass = parser.getParentPermClass();//menuClass.getDeclaringClass();
			parent = getPermClassParser(parent.getParentPermissionClass());
			code = parent.generatedSimpleCode() + CODE_SEPRATOR + code;
		}
		Class<?> pcls = parent.getMappingParentClass();
		if(pcls!=null){
			pcls = (pcls==ROOT_MENU_TAG?permissionConfig.getRootMenuClass():pcls);
			P perm = this.permissionMapByClass.get(pcls);
			if(perm==null)
				throw new BaseException("parse menu class["+parent.getActualPermissionClass()+"] error. no parent menu found: " + pcls);
			code = perm.getCode() + CODE_SEPRATOR + code;
		}
		return code;
	}
	
	public P getPermission(Class<?> clazz){
		return this.permissionMapByClass.get(clazz);
	}
	
	public P getMenuNode(String code){
		return permissionMap.get(code);
	}

	public P getRootMenu() {
		return rootMenu;
	}

	public void setRootMenu(P rootMenu) {
		this.rootMenu = rootMenu;
	}

	public PermissionConfig<P> getMenuInfoable() {
		return permissionConfig;
	}
	
	
}

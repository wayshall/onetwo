package org.onetwo.ext.permission.parser;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.reflect.Intro;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.GuavaUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.ext.permission.api.PermissionType;
import org.onetwo.ext.permission.api.annotation.FullyAuthenticated;
import org.onetwo.ext.permission.api.annotation.MenuMapping;
import org.onetwo.ext.permission.api.annotation.PermissionMeta;
import org.onetwo.ext.permission.api.annotation.PermissionMetaData;
import org.onetwo.ext.permission.api.annotation.ProxyMenu;
import org.onetwo.ext.permission.api.annotation.ReservePermission;
import org.onetwo.ext.permission.utils.UrlResourceInfoParser;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

public class PermClassParser {
	public static final String APP_CODE = "appCode";
	public static final String SORT = "sort";
	public static final String PERMISSION_TYPE = "permissionType";
	public static final String HIDDEN = "hidden";
	public static final String URL = "url";
	public static final String RESOURCES_PATTERN = "resourcesPattern";
	public static final String PARAMS = "params";
	public static final String NAME = "name";
	public static final String CHILDREN = "children";
	public static final String META = "meta";
//	public static final String COMPONENT_VIEW_PATH = IPermission.COMPONENT_VIEW_PATH;
	
	//menu option
	public static final String MENU_CSS_CLASS = "cssClass";
	public static final String MENU_SHOW_PROPS = "showProps";
	
//	private static final String CODE_SEPRATOR = "_";
//	public static final Class<?> ROOT_MENU_TAG = MenuInfoParser.class;
	
	public static PermClassParser create(Class<?> permClass, Class<?> parentPermissionClass){
		return new PermClassParser(permClass, parentPermissionClass);
	}
	
	private final Class<?> permissionClass;
	private final Class<?> parentPermissionClass;
	private final PermissionMeta permissionMeta;
//	private final AnnotationAttributes permissionMetaAttrs;
	private Map<String, Object> meta;
	
	private PermClassParser(Class<?> permClass, Class<?> parentPermissionClass) {
		super();
		this.permissionClass = permClass;
		this.parentPermissionClass = parentPermissionClass;
		
		permissionMeta = AnnotationUtils.getAnnotation(getActualPermissionClass(), PermissionMeta.class);
		/*if (permissionMeta!=null) {
			permissionMetaAttrs = AnnotationUtils.getAnnotationAttributes(getActualPermissionClass(), permissionMeta);
		} else {
			permissionMetaAttrs = null;
		}*/
	}
	
	public Class<?> getPermissionClass() {
		return permissionClass;
	}
	
	final public Class<?> getActualPermissionClass() {
		return isProxyMenu()?getProxyPermClass():permissionClass;
	}

	public String getName(){
		if (permissionMeta!=null && StringUtils.isNotBlank(permissionMeta.name())) {
			return permissionMeta.name();
		}
		return getFieldValue(NAME, String.class, "");
		/*Object nameValue = ReflectUtils.getFieldValue(getActualPermissionClass(), NAME, true);
		String name = nameValue==null?"":nameValue.toString();
		return name;*/
	}
	
	/*public String getComponentViewPath() {
		if (permissionMeta!=null && StringUtils.isNotBlank(permissionMeta.componentViewPath())) {
			return permissionMeta.componentViewPath();
		}
		return getFieldValue(COMPONENT_VIEW_PATH, String.class, "");
	}*/
	
	public String getAppCode(){
		if (isFullyAuthenticated()) {
			return FullyAuthenticated.APP_CODE;
		}
		return getFieldValue(APP_CODE, String.class, getActualPermissionClass().getSimpleName());
	}
	
	public boolean isFullyAuthenticated() {
//		return this.parentPermissionClass == FullyAuthenticated.class;
		return this.permissionClass == FullyAuthenticated.class;
	}

	public boolean isReservePermission() {
		return ReservePermission.class.isAssignableFrom(parentPermissionClass);
	}
	
	/***
	 * 当前环境下是否启用
	 * @author weishao zeng
	 * @param env
	 * @return
	 */
	public boolean isEnabledOnProfiles(Environment env) {
		if (permissionMeta==null) {
			return true;
		}
		
		String[] conditionalOnProfiles = this.permissionMeta.conditionalOnProfiles();
		Set<String> actualProfiles = Stream.of(conditionalOnProfiles).filter(p -> StringUtils.isNotBlank(p)).collect(Collectors.toSet());
		if (LangUtils.isEmpty(actualProfiles)) {
			return true;
		}
		
		Profiles profiles = Profiles.of(actualProfiles.toArray(new String[0]));
		return env.acceptsProfiles(profiles);
	}
	
	public String generatedSimpleCode(){
		return permissionClass.getSimpleName();
	}
	
	public Class<?> getMappingParentClass(){
		MenuMapping map = this.permissionClass.getAnnotation(MenuMapping.class);
		return map==null?null:map.parent();
	}
	
	public ProxyMenu getProxyMenu(){
		return this.permissionClass.getAnnotation(ProxyMenu.class);
	}
	
	public Class<?> getProxyPermClass(){
//		if(!isDelegatedMenu())
//			throw new BaseException("it's not a delegate menu:" + permissionClass);
		Class<?> delegatePermClass = this.getProxyMenu().value();
		if(delegatePermClass==null)
			throw new BaseException("no delete menu class found:" + permissionClass);
		return delegatePermClass;
	}
	
	public boolean isDeprecated(){
		return permissionClass.getAnnotation(Deprecated.class)!=null;
	}
	
	public boolean isProxyMenu(){
		return getProxyMenu()!=null;
	}
	
	public Class<?>[] getChildrenClasses(){
		JFishList<Class<?>> list = JFishList.create();
		Class<?>[] children =  isProxyMenu()?getProxyPermClass().getDeclaredClasses():permissionClass.getDeclaredClasses();
		list.addArray(children);
		Class<?>[] c = getChildren();
		if(!LangUtils.isEmpty(c)){
			list.addArray(c);
		}
		return list.toArray(new Class<?>[0]);
	}
	
	public Class<?> getParentPermissionClass(){
//		return parentPermissionClass!=null?parentPermissionClass:permissionClass.getDeclaringClass();
		if (parentPermissionClass!=null) {
			return parentPermissionClass;
		} else if (permissionClass.getDeclaringClass()!=null && permissionClass.getDeclaringClass().isInterface()) {
			return permissionClass.getDeclaringClass();
		}
		return null;
	}
	
	protected Class<?>[] getChildren(){
		if (permissionMeta!=null) {
			return permissionMeta.children();
		}
		return getFieldValue(CHILDREN, Class[].class);
	}
	
	/***
	 * 设置可选字段，成功返回true，忽略返回false
	 * @param permObj
	 * @param fieldName
	 * @param type
	 * @param def
	 * @return
	 */
	public <T> boolean setOptionFieldValue(Object permObj, String fieldName, Class<T> type, T def){
		if(containsField(fieldName)){
			T value = getFieldValue(fieldName, type, def);
			ReflectUtils.setProperty(permObj, fieldName, value);
			return true;
		}
		return false;
	}
	
	public boolean containsField(String fieldName){
		return ReflectUtils.getIntro(permissionClass).containsField(fieldName, false);
	}
	
	public Number getSort(){
		int sort = Integer.MIN_VALUE;
		if (permissionMeta!=null) {
			sort = permissionMeta.sort();
		}
		if (sort!=Integer.MIN_VALUE) {
			return sort;
		}
		return getFieldValue(SORT, Number.class);
	}
	
	public PermissionType getPermissionType(){
		if (permissionMeta!=null) {
			return permissionMeta.permissionType();
		}
		return getFieldValue(PERMISSION_TYPE, PermissionType.class, PermissionType.MENU);
	}
	
	public Boolean isHidden(){
		if (permissionMeta!=null) {
			return permissionMeta.hidden();
		}
		return getFieldValue(HIDDEN, Boolean.class, false);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMeta(){
		if (this.meta!=null) {
			return this.meta;
		}
		if (permissionMeta!=null) {
			String json = permissionMeta.meta();
			if (StringUtils.isNotBlank(json)) {
				meta = JsonMapper.DEFAULT_MAPPER.fromJson(json, Map.class);
				return meta;
			}
		}
		
		this.meta = new HashMap<String, Object>();
		Map<String, Object> map = (Map<String, Object>)getFieldValue(META, Map.class, null);
		if (map!=null) {
			this.meta.putAll(map);
		}
		Intro.wrap(permissionClass).getAllFields().stream().filter(field -> {
			return field.getAnnotation(PermissionMetaData.class)!=null;
		}).forEach(field -> {
			try {
				Object val = field.get(permissionClass);
				this.meta.put(field.getName(), val);
			} catch (Exception e) {
				throw new BaseException("get @PermissionMetaData field value error, field:+"+field.getName()+", message: " + e.getMessage());
			}
		});
		return this.meta;
	}
	
	public String getUrl(){
		if (permissionMeta!=null) {
			return permissionMeta.url();
		}
		return getFieldValue(URL, String.class, null);
	}
	public String getResourcesPattern(){
		if (permissionMeta!=null) {
			String resourcePatterns = GuavaUtils.join(permissionMeta.resourcesPattern(), UrlResourceInfoParser.URL_JOINER);
			return resourcePatterns;
		}
		String[] resourcePatterns = getFieldValue(RESOURCES_PATTERN, String[].class, null);
		if (resourcePatterns==null || resourcePatterns.length==0) {
			return null;
		}
		return GuavaUtils.join(permissionMeta.resourcesPattern(), UrlResourceInfoParser.URL_JOINER);
	}
	
	public String getMenuCssClass(){
		/*if (permissionMeta!=null) {
			return permissionMeta.cssClass();
		}*/
		return getFieldValue(MENU_CSS_CLASS, String.class, "");
	}
	
	public String getMenuShowProps(){
		/*if (permissionMeta!=null) {
			return permissionMeta.showProps();
		}*/
		return getFieldValue(MENU_SHOW_PROPS, String.class, "");
	}
	
	public Map<?, ?> getParams(){
		if (permissionMeta!=null) {
			String json = permissionMeta.params();
			if (StringUtils.isNotBlank(json)) {
				return JsonMapper.DEFAULT_MAPPER.fromJson(json, Map.class);
			}
		}
		return getFieldValue(PARAMS, Map.class, Collections.EMPTY_MAP);
	}
	
	public <T> T getFieldValue(String fieldName, Class<T> fieldType) {
		return getFieldValue(fieldName, fieldType, null);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getFieldValue(String fieldName, Class<T> fieldType, T def) {
		Class<?> permissionClass = this.permissionClass;
		Field pageElementField = ReflectUtils.findField(permissionClass, fieldName);
		T fieldValue = def;
		if(pageElementField!=null){
			Object pvalue;
			try {
				pvalue = pageElementField.get(permissionClass);
			} catch (Exception e) {
				throw new BaseException("get field error: " + e.getMessage());
			} 
			if(!fieldType.isInstance(pvalue))
				throw new BaseException("field["+fieldName+"] of " + permissionClass + " must be " + fieldType);
			fieldValue = (T) pvalue;
		}
		return fieldValue;
	}

	@Override
	public String toString() {
		return "PermClassParser [permissionClass=" + permissionClass
				+ ", parentPermissionClass=" + parentPermissionClass + "]";
	}

/*	public void setParentPermissionClass(Class<?> parentPermissionClass) {
		this.parentPermissionClass = parentPermissionClass;
	}*/
	
}

package org.onetwo.plugins.permission.service;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.web.mvc.HandlerMappingListener;
import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.plugins.permission.anno.ByMenuClass;
import org.onetwo.plugins.permission.entity.IMenu;
import org.onetwo.plugins.permission.entity.IPermission;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

public class PermissionHandlerMappingListener implements HandlerMappingListener {
	
//	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	@Resource
	private PermissionManagerImpl permissionManagerImpl;
	
	
	@Override
	public void onHandlerMethodsInitialized(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
		this.permissionManagerImpl.build();
		for(Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()){
			if(entry.getValue().getMethodAnnotation(ByMenuClass.class)!=null){
				ByMenuClass menuClass = entry.getValue().getMethodAnnotation(ByMenuClass.class);
				for(Class<?> codeClass : menuClass.codeClass()){
					if(AnnotationUtils.findAnnotationWithDeclaring(codeClass, Deprecated.class)!=null)
						continue;
					
					IPermission perm = this.permissionManagerImpl.getMenuInfoParser().getMenuNode(codeClass);
					if(perm==null)
						throw new BaseException("can not find the menu code class["+ codeClass+"] in controller: " + entry.getValue());
					if(IMenu.class.isInstance(perm)){
						IMenu menu = (IMenu) perm;
						String url = entry.getKey().getPatternsCondition().getPatterns().iterator().next();
						menu.setUrl(url);
						Iterator<RequestMethod> it = entry.getKey().getMethodsCondition().getMethods().iterator();
						if(it.hasNext()){
							String method = entry.getKey().getMethodsCondition().getMethods().iterator().next().toString();
							menu.setMethod(method);
						}
					}
				}
			}/*else if(entry.getValue().getMethodAnnotation(ByFunctionClass.class)!=null){
				ByFunctionClass permClass = entry.getValue().getMethodAnnotation(ByFunctionClass.class);
				for(Class<?> codeClass : permClass.codeClass()){
					PermissionEntity perm = this.permissionManagerImpl.getMenuInfoParser().getMenuNode(codeClass);
					if(perm==null)
						throw new BaseException("can not find the function code class in menu tree : " + codeClass);
					if(FunctionEntity.class.isInstance(perm)){
						FunctionEntity func = (FunctionEntity) perm;
						String url = entry.getKey().getPatternsCondition().getPatterns().iterator().next();
						func.setUrl(url);
						
						Iterator<RequestMethod> it = entry.getKey().getMethodsCondition().getMethods().iterator();
						if(it.hasNext()){
							String method = entry.getKey().getMethodsCondition().getMethods().iterator().next().toString();
							func.setMethod(method);
						}
					}
				}
			}*/
		}

//		logger.info("rootMenu:\n" + permissionManagerImpl.getRootMenu());
	}
	
}

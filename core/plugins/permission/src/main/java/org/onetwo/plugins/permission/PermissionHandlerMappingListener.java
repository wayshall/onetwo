package org.onetwo.plugins.permission;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.web.mvc.HandlerMappingListener;
import org.onetwo.plugins.permission.anno.ByMenuClass;
import org.onetwo.plugins.permission.anno.ByFunctionClass;
import org.onetwo.plugins.permission.entity.FunctionEntity;
import org.onetwo.plugins.permission.entity.MenuEntity;
import org.onetwo.plugins.permission.entity.PermissionEntity;
import org.onetwo.plugins.permission.service.PermissionManagerImpl;
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
					PermissionEntity perm = this.permissionManagerImpl.getMenuInfoParser().getMenuNode(codeClass);
					if(perm==null)
						throw new BaseException("can not find the menu code class in menu tree : " + codeClass);
					if(MenuEntity.class.isInstance(perm)){
						MenuEntity menu = (MenuEntity) perm;
						String url = entry.getKey().getPatternsCondition().getPatterns().iterator().next();
						menu.setUrl(url);
						Iterator<RequestMethod> it = entry.getKey().getMethodsCondition().getMethods().iterator();
						if(it.hasNext()){
							String method = entry.getKey().getMethodsCondition().getMethods().iterator().next().toString();
							menu.setMethod(method);
						}
					}
				}
			}else if(entry.getValue().getMethodAnnotation(ByFunctionClass.class)!=null){
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
			}
		}

//		logger.info("rootMenu:\n" + permissionManagerImpl.getRootMenu());
	}
	
}

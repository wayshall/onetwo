package org.onetwo.plugins.permission;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.web.mvc.HandlerMappingListener;
import org.onetwo.common.utils.Assert;
import org.onetwo.plugins.permission.anno.ByMenuClass;
import org.onetwo.plugins.permission.entity.MenuEntity;
import org.onetwo.plugins.permission.entity.PermissionEntity;
import org.onetwo.plugins.permission.service.PermissionManagerImpl;
import org.slf4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

public class PermissionHandlerMappingListener implements HandlerMappingListener {
	
	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	@Resource
	private PermissionManagerImpl permissionManagerImpl;
	
	
	@Override
	public void onHandlerMethodsInitialized(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
		this.permissionManagerImpl.build();
		ByMenuClass menuClass = null;
		for(Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()){
			menuClass = entry.getValue().getMethodAnnotation(ByMenuClass.class);
			if(menuClass==null)
				continue;
			for(Class<?> codeClass : menuClass.codeClass()){
				PermissionEntity perm = this.permissionManagerImpl.getMenuNode(codeClass);
				if(perm==null)
					throw new BaseException("can not find the menu code class in menu tree : " + codeClass);
				if(MenuEntity.class.isInstance(perm)){
					MenuEntity menu = (MenuEntity) perm;
					String url = entry.getKey().getPatternsCondition().getPatterns().iterator().next();
					menu.setUrl(url);
				}
			}
		}

//		logger.info("rootMenu:\n" + permissionManagerImpl.getRootMenu());
	}
	
}

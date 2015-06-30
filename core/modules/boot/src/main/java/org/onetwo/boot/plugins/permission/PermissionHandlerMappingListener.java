package org.onetwo.boot.plugins.permission;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.onetwo.boot.core.web.mvc.HandlerMappingListener;
import org.onetwo.boot.plugins.permission.annotation.ByMenuClass;
import org.onetwo.boot.plugins.permission.entity.IPermission;
import org.onetwo.boot.plugins.permission.utils.PermissionUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.utils.RequestUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

public class PermissionHandlerMappingListener implements HandlerMappingListener {
	
//	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	@Resource
	private PermissionManager<?> permissionManager;
	
	
	@Override
	public void onHandlerMethodsInitialized(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
		this.permissionManager.build();
		for(Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()){
			if(entry.getValue().getMethodAnnotation(ByMenuClass.class)!=null){
				ByMenuClass menuClass = entry.getValue().getMethodAnnotation(ByMenuClass.class);
				for(Class<?> codeClass : menuClass.value()){
					if(AnnotationUtils.findAnnotationWithDeclaring(codeClass, Deprecated.class)!=null)
						continue;
					
					IPermission<?> perm = this.permissionManager.getPermission(codeClass);
					if(perm==null){
//						System.out.println("html: " + this.permissionManagerImpl.getMenuInfoParser().getRootMenu());
						throw new BaseException("can not find the menu code class["+ codeClass+"] in controller: " + entry.getValue());
					}
					if(PermissionUtils.isMenu(perm)){
						IPermission<?> menu = perm;
						String url = entry.getKey().getPatternsCondition().getPatterns().iterator().next();
						if(StringUtils.isNotBlank(menu.getUrl())){
							url = RequestUtils.appendParamString(url, menu.getUrl());
						}
						menu.setUrl(url);
						Iterator<RequestMethod> it = entry.getKey().getMethodsCondition().getMethods().iterator();
						if(it.hasNext()){
							String method = entry.getKey().getMethodsCondition().getMethods().iterator().next().toString();
							menu.setMethod(method);
						}else{
							menu.setMethod(RequestMethod.GET.name());
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

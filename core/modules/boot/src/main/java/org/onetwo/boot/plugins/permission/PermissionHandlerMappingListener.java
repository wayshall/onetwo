package org.onetwo.boot.plugins.permission;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.onetwo.boot.core.web.mvc.HandlerMappingListener;
import org.onetwo.boot.plugins.permission.annotation.ByFunctionClass;
import org.onetwo.boot.plugins.permission.annotation.ByMenuClass;
import org.onetwo.boot.plugins.permission.entity.IPermission;
import org.onetwo.boot.plugins.permission.utils.PermissionUtils;
import org.onetwo.boot.plugins.permission.utils.UrlResourceInfo;
import org.onetwo.boot.plugins.permission.utils.UrlResourceInfoParser;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.utils.RequestUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

public class PermissionHandlerMappingListener implements HandlerMappingListener {
	
	private UrlResourceInfoParser urlResourceInfoParser = new UrlResourceInfoParser();
	
//	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	@Resource
	private PermissionManager<?> permissionManager;
	
	
	private void setMenuUrlByRequestMappingInfo(Class<?> codeClass, Entry<RequestMappingInfo, HandlerMethod> entry){
		if(AnnotationUtils.findAnnotationWithDeclaring(codeClass, Deprecated.class)!=null){
//			continue;
			return;
		}
		
		IPermission<?> perm = this.permissionManager.getPermission(codeClass);
		if(perm==null || !PermissionUtils.isMenu(perm)){
//			System.out.println("html: " + this.permissionManagerImpl.getMenuInfoParser().getRootMenu());
			throw new BaseException("can not find the menu code class["+ codeClass+"] in controller: " + entry.getValue());
		}
		IPermission<?> menu = perm;
		
//		String url = entry.getKey().getPatternsCondition().getPatterns().iterator().next();
		String url = getFirstUrl(entry.getKey());
		if(StringUtils.isNotBlank(menu.getUrl())){
			url = RequestUtils.appendParamString(url, menu.getUrl());
		}
		menu.setUrl(url);
//		Iterator<RequestMethod> it = entry.getKey().getMethodsCondition().getMethods().iterator();

		String method = getFirstMethod(entry.getKey());
		menu.setMethod(method);
		/*if(it.hasNext()){
			//取第一个映射方法
			String method = entry.getKey().getMethodsCondition().getMethods().iterator().next().toString();
			menu.setMethod(method);
		}else{
			menu.setMethod(RequestMethod.GET.name());
		}*/
	}
	
	private void setResourcePatternByRequestMappingInfo(Class<?> codeClass, Entry<RequestMappingInfo, HandlerMethod> entry){
		if(AnnotationUtils.findAnnotationWithDeclaring(codeClass, Deprecated.class)!=null){
			return;
		}
		
		IPermission<?> perm = this.permissionManager.getPermission(codeClass);
		if(perm==null){
			throw new BaseException("can not found IPermission for code class: " + codeClass);
		}
		List<UrlResourceInfo> infos = urlResourceInfoParser.parseToUrlResourceInfos(perm.getResourcesPattern());
		Set<String> urlPattterns = entry.getKey().getPatternsCondition().getPatterns();
		
		if(urlPattterns.size()==1){
			String url = urlPattterns.stream().findFirst().orElse("");
			infos.add(new UrlResourceInfo(url, getFirstMethod(entry.getKey())));
			
		}else{
			//超过一个url映射的，不判断方法
			urlPattterns.stream().forEach(url->infos.add(new UrlResourceInfo(url)));
		}
		String urls = this.urlResourceInfoParser.parseToString(infos);
		perm.setResourcesPattern(urls);
	}
	
	private String getFirstMethod(RequestMappingInfo info){
		//取第一个映射方法
		return info.getMethodsCondition().getMethods().stream().findFirst().orElse(RequestMethod.GET).name();
	}
	
	private String getFirstUrl(RequestMappingInfo info){
		//取第一个映射地址
		return info.getPatternsCondition().getPatterns().stream().findFirst().orElse("");
	}
	
	@Override
	public void onHandlerMethodsInitialized(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
		this.permissionManager.build();
		for(Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()){
			if(entry.getValue().getMethodAnnotation(ByMenuClass.class)!=null){
				ByMenuClass menuClass = entry.getValue().getMethodAnnotation(ByMenuClass.class);
				for(Class<?> codeClass : menuClass.value()){
					this.setMenuUrlByRequestMappingInfo(codeClass, entry);
					this.setResourcePatternByRequestMappingInfo(codeClass, entry);
				}
			}else if(entry.getValue().getMethodAnnotation(ByFunctionClass.class)!=null){
				ByFunctionClass funcClass = entry.getValue().getMethodAnnotation(ByFunctionClass.class);
				for(Class<?> codeClass : funcClass.value()){
					this.setResourcePatternByRequestMappingInfo(codeClass, entry);
				}
			}
		}

//		logger.info("rootMenu:\n" + permissionManagerImpl.getRootMenu());
	}
	
}

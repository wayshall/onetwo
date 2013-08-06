package org.onetwo.plugins.permission;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.web.mvc.HandlerMappingListener;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.permission.anno.ByMenuClass;
import org.onetwo.plugins.permission.anno.ByPermissionClass;
import org.onetwo.plugins.permission.entity.MenuEntity;
import org.onetwo.plugins.permission.entity.PermissionEntity;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

@Service
public class JresourceHandlerMappingListener implements HandlerMappingListener {
	private static final String CONTROLLER_PACKAGE = "controller.";
	
	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	private JresourceManagerImpl jresourceManagerImpl;
	
	@Override
	public void onHandlerMethodsInitialized(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
		Map<String, PermissionEntity> infoMaps = new HashMap<String, PermissionEntity>();
		PermissionEntity cinfo = null;
		ByPermissionClass permission = null;
		for(Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()){
			Class<?> declarClass = entry.getValue().getMethod().getDeclaringClass();
			cinfo = parseClassInfo(infoMaps, declarClass);
			permission = entry.getValue().getMethodAnnotation(ByPermission.class);
			if(permission==null)
				continue;
			
			String methodName = declarClass.getSimpleName() + "." + entry.getValue().getMethod().getName();
			logger.info(" find jresource in {}", methodName);
			
			JResourceInfo minfo = parseAsResourceInfo(permission, declarClass, entry.getValue().getMethod());
			if(cinfo!=null){//如果类有jresource标注，方法的父资源必须就是类的resource
				minfo.setParent(cinfo);
			}else{
				Class<?> parentClass = permission.parentResource();
				if(!isRootResourceClass(parentClass)){
					cinfo = parseClassInfo(infoMaps, parentClass);
					if(cinfo!=null)
						minfo.setParent(cinfo);
				}
			}
			infoMaps.put(minfo.getKey(), minfo);
		}
		
		logger.info("resource info : {} ", infoMaps);
		this.jresourceManagerImpl = new JresourceManagerImpl(infoMaps);
	}
	
	protected PermissionEntity parseClassInfo(Map<String, PermissionEntity> infoMaps, Class<?> declarClass){
		ByPermissionClass bypermission = declarClass.getAnnotation(ByPermissionClass.class);
		if(bypermission==null)//ignore if no jresource annotation
			return null;
		
		String key = getKey(true, declarClass, null);
		PermissionEntity permission = infoMaps.get(key);
		if(permission!=null)
			return permission;
		
		if(bypermission!=null){
			permission = infoMaps.get(key);
			if(permission==null){
				permission = parseAsResourceInfo(bypermission, declarClass, null);
				infoMaps.put(key, permission);
			}
			if(!isRootResourceClass(bypermission.parentResource())){
				JResourceInfo parent = parseClassInfo(infoMaps, bypermission.parentResource());
				permission.setParent(parent);
			}
		}
		return permission;
	}
	
	protected boolean isRootResourceClass(Class<?> resClass){
		return resClass==Object.class;
	}
	
	protected String getKey(boolean isClassAnno, Class<?> declarClass, Method method){
		return isClassAnno?declarClass.getName():method.toGenericString();
	}
	
	protected PermissionEntity parseAsResourceInfo(ByMenuClass anno, Class<?> declarClass, Method method){
		Assert.notNull(anno);
		PermissionEntity info = null;
		Class<?>[] codeClasses = anno.codeClass();
		for(Class<?> codeClass : codeClasses){
			codeClass.getDeclaringClass();
		}
		String id = parseId(declarClass, method, anno.assemble());
		if(anno.){
			info = new MenuEntity();
		}else if(ByPermissionClass.class.isInstance(anno)){
			info = new JPermissionInfo(id, anno.label());
		}else{
			throw new ServiceException("unsupported annotation: " + anno);
		}
		String key = getKey(method==null, declarClass, method);
		info.setKey(key);
		info.setAssembleTag(StringUtils.isNotBlank(anno.assemble()));
		return info;
	}

	private static String parseId(Class<?> declarClass, Method method, String assemble){
		String pack = declarClass.getName();
		int cIndex = pack.indexOf(CONTROLLER_PACKAGE);
		if(cIndex!=-1){
			pack = pack.substring(cIndex+CONTROLLER_PACKAGE.length());
		}
//		String id = pack.replace('.', '_');
		String id = pack;
		if(StringUtils.isNotBlank(assemble)){
			id += "." + assemble;
		}else{
			if(method!=null)
				id += "." + method.getName();
		}
		return id.toUpperCase();
	}

	public JresourceManagerImpl getJresourceManager() {
		return jresourceManagerImpl;
	}
	
}

package org.onetwo.plugins.permission;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.web.mvc.HandlerMappingListener;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.permission.anno.JResource;
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
		Map<String, JResourceInfo> infoMaps = new HashMap<String, JResourceInfo>();
		JResourceInfo cinfo = null;
		JResource jres = null;
		for(Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()){
			Class<?> declarClass = entry.getValue().getMethod().getDeclaringClass();
			cinfo = parseClassInfo(infoMaps, declarClass);
			jres = entry.getValue().getMethodAnnotation(JResource.class);
			if(jres==null)
				continue;
			
			String methodName = declarClass.getSimpleName() + "." + entry.getValue().getMethod().getName();
			logger.info(" find jresource in {}", methodName);
			
			JResourceInfo minfo = parseAsResourceInfo(jres, declarClass, entry.getValue().getMethod());
			if(cinfo!=null){//如果类有jresource标注，方法的父资源必须就是类的resource
				minfo.setParent(cinfo);
			}else{
				Class<?> parentClass = jres.parentResource();
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
	
	protected JResourceInfo parseClassInfo(Map<String, JResourceInfo> infoMaps, Class<?> declarClass){
		JResource jres = declarClass.getAnnotation(JResource.class);
		if(jres==null)//ignore if no jresource annotation
			return null;
		
		String key = getKey(true, declarClass, null);
		JResourceInfo info = infoMaps.get(key);
		if(info!=null)
			return info;
		
		if(jres!=null){
			info = infoMaps.get(key);
			if(info==null){
				info = parseAsResourceInfo(jres, declarClass, null);
				infoMaps.put(key, info);
			}
			if(!isRootResourceClass(jres.parentResource())){
				JResourceInfo parent = parseClassInfo(infoMaps, jres.parentResource());
				info.setParent(parent);
			}
		}
		return info;
	}
	
	protected boolean isRootResourceClass(Class<?> resClass){
		return resClass==Object.class;
	}
	
	protected String getKey(boolean isClassAnno, Class<?> declarClass, Method method){
		return isClassAnno?declarClass.getName():method.toGenericString();
	}
	
	protected JResourceInfo parseAsResourceInfo(JResource jres, Class<?> declarClass, Method method){
		Assert.notNull(jres);
		JResourceInfo info = null;
		String id = parseId(declarClass, method, jres.assemble());
		if(jres.menu()){
			info = new JMenuInfo(id, jres.label());
		}else if(jres.permission()){
			info = new JPermissionInfo(id, jres.label());
		}else{
			info = new JResourceInfo(id, jres.label());
		}
		String key = getKey(method==null, declarClass, method);
		info.setKey(key);
		info.setAssembleTag(StringUtils.isNotBlank(jres.assemble()));
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

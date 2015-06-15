package org.onetwo.plugins.fmtagext.spring;

import org.onetwo.common.fish.plugin.JFishWebMvcPluginManager;
import org.onetwo.common.fish.plugin.JFishWebMvcPluginMeta;
import org.onetwo.common.jfishdbm.mapping.JFishMappedEntry;
import org.onetwo.common.jfishdbm.mapping.MappedEntryManager;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.rest.RestPather;
import org.onetwo.common.spring.rest.RestPather.EntityPathInfo;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.fmtagext.BaseRestController;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.bind.annotation.RequestMapping;

public class ControllerRestPatherBeanPostProcessor implements BeanPostProcessor {
	
	private final static Logger logger = JFishLoggerFactory.getLogger(ControllerRestPatherBeanPostProcessor.class);

	@Autowired
	private RestPather restPather;
	
	@Autowired
	private MappedEntryManager mappedEntryManager;
	
	@Autowired
	private JFishWebMvcPluginManager jfishPluginManager;
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if(!BaseRestController.class.isInstance(bean)){
			return bean;
		}
		BaseRestController<?> rc = (BaseRestController<?>) bean;
		String entityPath = rc.getBaseEntityPath();
		if(StringUtils.isBlank(entityPath)){
			RequestMapping rm = rc.getClass().getAnnotation(RequestMapping.class);
			String packageUrlMapping = "";
			if(rm!=null){
				packageUrlMapping = rm.value()[0];
			}
			JFishWebMvcPluginMeta plugin = jfishPluginManager.getJFishPluginMetaOf(rc.getClass());
			if(plugin!=null){
				entityPath = plugin.getPluginInfo().getContextPath() + packageUrlMapping;
//				logger.info("plugin entityPath: " + entityPath);
			}else{
				entityPath = packageUrlMapping;
			}
		}
		
		JFishMappedEntry entry = null;
		try {
			entry = this.mappedEntryManager.getEntry(rc.getEntityClass());
		} catch (Exception e) {
			logger.error("build rest pather error for controller[{}]", rc.getClass());
			return bean;
		}
//		this.restPather.addEntityPathInfo(rc.getClass(), entityPath, entry.getIdentifyField().getName());
		EntityPathInfo info = this.restPather.addEntityPathInfo(rc.getClass(), entityPath, entry.getIdentifyField().getName());
		logger.info("build rest EntityPathInfo : " + info);
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}

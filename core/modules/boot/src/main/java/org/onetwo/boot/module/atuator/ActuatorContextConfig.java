package org.onetwo.boot.module.atuator;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.core.web.BootMvcConfigurerAdapter;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.utils.NetUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnClass(ManagementServerProperties.class)
@ConditionalOnBean(BootMvcConfigurerAdapter.class)
//@AutoConfigureAfter(ManagementServerPropertiesAutoConfiguration.class)
public class ActuatorContextConfig  {

	public ActuatorContextConfig() {
	}

	@Configuration
	public static class ActuatorBeanPostProcessor implements BeanPostProcessor{

		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
			return bean;
		}
	
		/***
		 * 修改actuator的默认contextPath
		 */
		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
			if(bean instanceof ManagementServerProperties){
				ManagementServerProperties props = (ManagementServerProperties) bean;
				if(StringUtils.isBlank(props.getContextPath())){
					props.setContextPath(BootWebUtils.CONTROLLER_PREFIX+"/management");
				}
				if(props.getAddress()==null){
					props.setAddress(NetUtils.getInetAddress("127.0.0.1", null));
				}
			}
			return bean;
		}
	}

}

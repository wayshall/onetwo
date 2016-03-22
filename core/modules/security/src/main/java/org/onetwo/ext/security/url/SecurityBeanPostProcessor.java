package org.onetwo.ext.security.url;

import org.onetwo.ext.security.DatabaseSecurityMetadataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

public class SecurityBeanPostProcessor implements BeanPostProcessor {
	
	@Autowired(required=false)
	private DatabaseSecurityMetadataSource databaseSecurityMetadataSource;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		if(FilterSecurityInterceptor.class.isInstance(bean)){
			FilterSecurityInterceptor fsi = (FilterSecurityInterceptor) bean;
			if(databaseSecurityMetadataSource!=null){
				fsi.setSecurityMetadataSource(databaseSecurityMetadataSource.convertTo(fsi.getSecurityMetadataSource()));
			}
//			fsi.setSecurityMetadataSource(securityMetadataSource);
			
	        /*WebExpressionVoter expressionVoter = new WebExpressionVoter();
	        fsi.setAccessDecisionManager(new AffirmativeBased(Arrays.asList(expressionVoter)));
		    */    
			/*AbstractAccessDecisionManager adm = (AbstractAccessDecisionManager)fsi.getAccessDecisionManager();
			adm.setDecisionVoters(newList);*/
		}
		return bean;
	}
	
	

}

package org.onetwo.common.ds;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.onetwo.common.fish.utils.ContextHolder;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.utils.Assert;
import org.springframework.beans.factory.InitializingBean;

public class DatasourceSwitcherProxy implements InitializingBean {
	
	@Resource
	private ContextHolder contextHolder;
	private String timeKey = "controller";
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(contextHolder, "contextHolder can not be null.");
	}


	public Object autoSwitch(ProceedingJoinPoint pjp) throws Throwable{
		processSwitchInfo(pjp);
		try{
			UtilTimerStack.push(timeKey);
			return pjp.proceed();
		}finally{
			UtilTimerStack.pop(timeKey);
		}
	}
	
	public void processSwitchInfo(JoinPoint pjp){
		MethodSignature ms = (MethodSignature) pjp.getSignature();
		DataSourceSwitcherInfo info = null;
		
		System.out.println("====>>>>pjp:"+pjp);
		info = contextHolder.getContextAttribute(DataSourceSwitcherInfo.CURRENT_DATASOURCE_KEY);
		if(info!=null)
			return ;
		
		DataSourceSwitcher dsw = AnnotationUtils.findAnnotationWithStopClass(ms.getDeclaringType(), ms.getMethod(), DataSourceSwitcher.class, Object.class);
		
		if(dsw==null){
			info = DataSourceSwitcherInfo.DEFAULT_INFO;
		}else{
			info = new DataSourceSwitcherInfo(dsw.value());
		}
		contextHolder.setContextAttribute(DataSourceSwitcherInfo.CURRENT_DATASOURCE_KEY, info);
	}

}

package org.onetwo.common.ds;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.onetwo.common.profiling.UtilTimerStack;

@Aspect
public class AutoSwitchDatasourceByRequestMapping extends DatasourceSwitcherProxyImpl{

	private String timeKey = "controller";
	
	@Before("org.onetwo.common.jdbc.JFishPointcut.autoSwitchDatasourceByRequestMapping()")
	public void switchDatasource(JoinPoint jp){
		this.processSwitchInfo(jp);
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
}

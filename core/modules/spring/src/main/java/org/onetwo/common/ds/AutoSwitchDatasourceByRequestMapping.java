package org.onetwo.common.ds;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class AutoSwitchDatasourceByRequestMapping extends SwitcherProxyImpl{

//	@Before("org.onetwo.common.jdbc.JFishPointcut.autoSwitchDatasourceByRequestMapping()")
	public void switchDatasource(JoinPoint jp){
		this.processSwitchInfo(jp);
	}

	@Around("org.onetwo.common.ds.SwitchPoincut.byRequestMapping()")
	public Object autoSwitch(ProceedingJoinPoint pjp) throws Throwable{
		try{
			processSwitchInfo(pjp);
//			UtilTimerStack.push(timeKey);
			return pjp.proceed();
		}finally{
//			UtilTimerStack.pop(timeKey);
//			clearSwitchInfo();
		}
	}
}

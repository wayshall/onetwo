package org.onetwo.common.ds;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class AutoSwitchDatasourceByRequestMapping extends DatasourceSwitcherProxy{

	@Before("org.onetwo.common.jdbc.JFishPointcut.autoSwitchDatasourceByRequestMapping()")
	public void switchDatasource(JoinPoint jp){
		this.processSwitchInfo(jp);
	}
}

package org.onetwo.common.ds;

import org.aspectj.lang.JoinPoint;

public interface DatasourceSwitcherProxy {

	public void processSwitchInfo(JoinPoint pjp);

}
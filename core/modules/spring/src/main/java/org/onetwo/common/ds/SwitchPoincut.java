package org.onetwo.common.ds;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SwitchPoincut {

	@Pointcut("@within(org.springframework.stereotype.Controller) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void byRequestMapping(){}


	@Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
	public void byTransactional(){}
}

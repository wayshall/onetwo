package org.onetwo.dbm.event;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.jdbc.core.JdbcOperations;

@Deprecated
public class JdbcOperationEvent implements DbmEvent<JdbcOperations> {
	
	final private DbmEventAction action;
	final private JdbcOperations eventSource;
	final private ProceedingJoinPoint pjp;

	public JdbcOperationEvent(DbmEventAction action, ProceedingJoinPoint pjp) {
		super();
		this.action = action;
		this.pjp = pjp;
		this.eventSource = (JdbcOperations)this.pjp.getTarget();
	}

	@Override
	public DbmEventAction getAction() {
		return action;
	}

	@Override
	public JdbcOperations getEventSource() {
		return eventSource;
	}

}

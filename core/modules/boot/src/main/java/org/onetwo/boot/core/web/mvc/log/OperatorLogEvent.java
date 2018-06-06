package org.onetwo.boot.core.web.mvc.log;

import org.springframework.context.ApplicationEvent;

/**
 * @author wayshall
 * <br/>
 */
@SuppressWarnings("serial")
public class OperatorLogEvent extends ApplicationEvent {
	
	final private OperatorLogInfo operatorLog;

	public OperatorLogEvent(Object source, OperatorLogInfo operatorLog) {
		super(source);
		this.operatorLog = operatorLog;
	}

	public OperatorLogInfo getOperatorLog() {
		return operatorLog;
	}

}

package org.onetwo.common.web.tomcatmini;

import java.util.List;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

public class ContextCreatedEvent {
	final private Tomcat tomcat;
	final private List<Context> contexts;
	
	public ContextCreatedEvent(Tomcat tomcat, List<Context> contexts) {
		super();
		this.tomcat = tomcat;
		this.contexts = contexts;
	}
	public Tomcat getTomcat() {
		return tomcat;
	}
	public List<Context> getContexts() {
		return contexts;
	}
	
	
}

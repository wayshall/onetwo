package org.onetwo.common.web.server.event;


public interface WebappAddEvent<T> {

	T getEventSource();

	WebappAddEvent<T> addWebapp(String contextPath, String baseDir);

}
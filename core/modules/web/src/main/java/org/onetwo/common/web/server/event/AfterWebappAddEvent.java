package org.onetwo.common.web.server.event;


public interface AfterWebappAddEvent<T> {

	T getEventSource();

}
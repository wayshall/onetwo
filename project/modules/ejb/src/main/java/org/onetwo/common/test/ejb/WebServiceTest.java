package org.onetwo.common.test.ejb;

import org.onetwo.common.ws.WebServiceFactory;
import org.onetwo.common.ws.WebServiceFactory.Container;


public class WebServiceTest  {

	protected static String serviceBaseURL;
	protected static Container container = Container.weblogic;
 
	public static String getServiceBaseURL() {
		return serviceBaseURL;
	}

	public static void setServiceBaseURL(String url) {
		serviceBaseURL = url; 
	}

	public static Container getContainer() {
		return container;
	}

	public static void setContainer(Container container) {
		WebServiceTest.container = container;
	}

	public static <T> T getWebService(Class<T> clazz){
		T webservice = WebServiceFactory.createWebService(getServiceBaseURL(), clazz, getContainer());
		return webservice; 
	}
}

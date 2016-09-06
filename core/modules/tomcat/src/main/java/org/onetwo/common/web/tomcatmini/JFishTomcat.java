package org.onetwo.common.web.tomcatmini;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanner;

/*****
 * 
 * @author way
 *
 */
public class JFishTomcat extends Tomcat {
	private Class<? extends StandardContext> contextClass;
	/****
	 * 设置扫描目录，让实现了ServletContainerInitializer和WebApplicationInitializer接口而不在jar里面的类被扫描到
	 */
	public Context addWebapp(Host host, String url, String name, String path) {
		Context ctx = super.addWebapp(host, url, name, path);
		StandardJarScanner jarScanner = new StandardJarScanner();
		jarScanner.setScanAllDirectories(true);
		ctx.setJarScanner(jarScanner);
		return ctx;
	}
	
	public boolean hack(){
		return contextClass!=null;
	}
	
	public Context addWebapp(Host host, String contextPath, String docBase, ContextConfig config) {
		if(hack()){
	        if (host instanceof StandardHost) {
	            ((StandardHost) host).setContextClass(contextClass.getName());
	        }
		}
		Context ctx = super.addWebapp(host, contextPath, docBase, config);
		return ctx;

    }

	public void setContextClass(Class<? extends StandardContext> contextClass) {
		this.contextClass = contextClass;
	}
    
}

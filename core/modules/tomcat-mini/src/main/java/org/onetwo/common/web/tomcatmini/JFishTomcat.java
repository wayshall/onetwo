package org.onetwo.common.web.tomcatmini;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanner;

/*****
 * 
 * @author way
 *
 */
public class JFishTomcat extends Tomcat {
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
}

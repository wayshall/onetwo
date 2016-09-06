package org.onetwo.common.web.tomcatmini;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

import org.apache.catalina.core.StandardContext;

public class HackServletContextStandardContext extends StandardContext {
	private Map<String, String> initParametersMapper = new HashMap<String, String>();

	@Override
    public ServletContext getServletContext() {
		ServletContext sc = super.getServletContext();
		return new HackServletContext(sc);
    }

	final public void mapInitParameter(String name, String value){
		this.initParametersMapper.put(name, value);
	}
	
	public class HackServletContext implements ServletContext {
		private ServletContext servletContext;

		public HackServletContext(ServletContext servletContext) {
			super();
			this.servletContext = servletContext;
		}

		public String getContextPath() {
			return servletContext.getContextPath();
		}

		public ServletContext getContext(String uripath) {
			return servletContext.getContext(uripath);
		}

		public int getMajorVersion() {
			return servletContext.getMajorVersion();
		}

		public int getMinorVersion() {
			return servletContext.getMinorVersion();
		}

		public int getEffectiveMajorVersion() {
			return servletContext.getEffectiveMajorVersion();
		}

		public int getEffectiveMinorVersion() {
			return servletContext.getEffectiveMinorVersion();
		}

		public String getMimeType(String file) {
			return servletContext.getMimeType(file);
		}

		public Set<String> getResourcePaths(String path) {
			return servletContext.getResourcePaths(path);
		}

		public URL getResource(String path) throws MalformedURLException {
			return servletContext.getResource(path);
		}

		public InputStream getResourceAsStream(String path) {
			return servletContext.getResourceAsStream(path);
		}

		public RequestDispatcher getRequestDispatcher(String path) {
			return servletContext.getRequestDispatcher(path);
		}

		public RequestDispatcher getNamedDispatcher(String name) {
			return servletContext.getNamedDispatcher(name);
		}

		public Servlet getServlet(String name) throws ServletException {
			return servletContext.getServlet(name);
		}

		public Enumeration<Servlet> getServlets() {
			return servletContext.getServlets();
		}

		public Enumeration<String> getServletNames() {
			return servletContext.getServletNames();
		}

		public void log(String msg) {
			servletContext.log(msg);
		}

		public void log(Exception exception, String msg) {
			servletContext.log(exception, msg);
		}

		public void log(String message, Throwable throwable) {
			servletContext.log(message, throwable);
		}

		public String getRealPath(String path) {
			return servletContext.getRealPath(path);
		}

		public String getServerInfo() {
			return servletContext.getServerInfo();
		}

		public String getInitParameter(String name) {
			if(initParametersMapper.containsKey(name)){
				return initParametersMapper.get(name);
			}
			return servletContext.getInitParameter(name);
		}

		public Enumeration<String> getInitParameterNames() {
			return servletContext.getInitParameterNames();
		}

		public boolean setInitParameter(String name, String value) {
			return servletContext.setInitParameter(name, value);
		}

		public Object getAttribute(String name) {
			return servletContext.getAttribute(name);
		}

		public Enumeration<String> getAttributeNames() {
			return servletContext.getAttributeNames();
		}

		public void setAttribute(String name, Object object) {
			servletContext.setAttribute(name, object);
		}

		public void removeAttribute(String name) {
			servletContext.removeAttribute(name);
		}

		public String getServletContextName() {
			return servletContext.getServletContextName();
		}

		public Dynamic addServlet(String servletName, String className) {
			return servletContext.addServlet(servletName, className);
		}

		public Dynamic addServlet(String servletName, Servlet servlet) {
			return servletContext.addServlet(servletName, servlet);
		}

		public Dynamic addServlet(String servletName,
				Class<? extends Servlet> servletClass) {
			return servletContext.addServlet(servletName, servletClass);
		}

		public <T extends Servlet> T createServlet(Class<T> c)
				throws ServletException {
			return servletContext.createServlet(c);
		}

		public ServletRegistration getServletRegistration(String servletName) {
			return servletContext.getServletRegistration(servletName);
		}

		public Map<String, ? extends ServletRegistration> getServletRegistrations() {
			return servletContext.getServletRegistrations();
		}

		public javax.servlet.FilterRegistration.Dynamic addFilter(
				String filterName, String className) {
			return servletContext.addFilter(filterName, className);
		}

		public javax.servlet.FilterRegistration.Dynamic addFilter(
				String filterName, Filter filter) {
			return servletContext.addFilter(filterName, filter);
		}

		public javax.servlet.FilterRegistration.Dynamic addFilter(
				String filterName, Class<? extends Filter> filterClass) {
			return servletContext.addFilter(filterName, filterClass);
		}

		public <T extends Filter> T createFilter(Class<T> c)
				throws ServletException {
			return servletContext.createFilter(c);
		}

		public FilterRegistration getFilterRegistration(String filterName) {
			return servletContext.getFilterRegistration(filterName);
		}

		public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
			return servletContext.getFilterRegistrations();
		}

		public SessionCookieConfig getSessionCookieConfig() {
			return servletContext.getSessionCookieConfig();
		}

		public void setSessionTrackingModes(
				Set<SessionTrackingMode> sessionTrackingModes) {
			servletContext.setSessionTrackingModes(sessionTrackingModes);
		}

		public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
			return servletContext.getDefaultSessionTrackingModes();
		}

		public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
			return servletContext.getEffectiveSessionTrackingModes();
		}

		public void addListener(String className) {
			servletContext.addListener(className);
		}

		public <T extends EventListener> void addListener(T t) {
			servletContext.addListener(t);
		}

		public void addListener(Class<? extends EventListener> listenerClass) {
			servletContext.addListener(listenerClass);
		}

		public <T extends EventListener> T createListener(Class<T> c)
				throws ServletException {
			return servletContext.createListener(c);
		}

		public JspConfigDescriptor getJspConfigDescriptor() {
			return servletContext.getJspConfigDescriptor();
		}

		public ClassLoader getClassLoader() {
			return servletContext.getClassLoader();
		}

		public void declareRoles(String... roleNames) {
			servletContext.declareRoles(roleNames);
		}

		public String getVirtualServerName() {
			return servletContext.getVirtualServerName();
		}
		
		
	}

	
}

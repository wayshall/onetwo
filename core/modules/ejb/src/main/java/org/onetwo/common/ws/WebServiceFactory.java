package org.onetwo.common.ws;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.commons.lang.ArrayUtils;
import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;

public abstract class WebServiceFactory {
	
	public static enum Container {
		weblogic,
//		weblogic_1034,
		jboss
	}
	
	public static class WebServiceMeta {
		private Class<?> sourceClass;
		private String targetNamespace;
		private String serviceName;
		private String portName;
		private String name;
		private String endpointInterface;

		public QName buildServiceQName(){
			QName serviceQName = new QName(getTargetNamespace(), getServiceName());
			return serviceQName;
		}
		
		public QName buildPortQName(){
			QName portQName = new QName(getTargetNamespace(), getPortName());
			return portQName;
		}

		public String getTargetNamespace() {
			if(StringUtils.isBlank(targetNamespace))
				return defaultTargetNamespace();
			return targetNamespace;
		}

		@SuppressWarnings("rawtypes")
		protected String defaultTargetNamespace(){
			Class clazz = getEndpointInterfaceClass();
			String packageName = reversePackageName(clazz.getPackage().getName());
			String qurl = "http://"+packageName+"/";
			return qurl;
		}
		
		protected String defaultPortName(){
			return sourceClass.getSimpleName()+"Port";
		}
		
		protected String defaultServiceName(){
			return sourceClass.getSimpleName() + "Service";
		}

		public void setTargetNamespace(String targetNamespace) {
			this.targetNamespace = targetNamespace;
		}

		public String getServiceName() {
			if(StringUtils.isBlank(serviceName))
				return defaultServiceName();
			return serviceName;
		}
		
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}

		public Class<?> getSourceClass() {
			return sourceClass;
		}

		public void setSourceClass(Class<?> sourceClass) {
			this.sourceClass = sourceClass;
		}

		public String getPortName() {
			if(StringUtils.isBlank(portName))
				return defaultPortName();
			return portName;
		}

		public void setPortName(String portName) {
			this.portName = portName;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEndpointInterface() {
			return endpointInterface;
		}

		public void setEndpointInterface(String endpointInterface) {
			this.endpointInterface = endpointInterface;
		}
		
		public Class<?> getEndpointInterfaceClass(){
			if(StringUtils.isNotBlank(endpointInterface)){
				return ReflectUtils.loadClass(endpointInterface);
			}else{
				return sourceClass;
			}
		}
		
		/*public String getWsdlURL(String baseURL){
			Assert.hasText(baseURL, "baseURL can not be empty!");
			String wsdl = baseURL;
			if(wsdl.toLowerCase().endsWith("?wsdl"))
				return wsdl;
			if(!wsdl.endsWith("/"))
				wsdl += "/";
			wsdl += getServiceName() + "?wsdl";
			return wsdl;
		}*/
		

		public String getWsdlURL(String baseURL){
			String wsdl = baseURL;
			if(wsdl.toLowerCase().endsWith("?wsdl"))
				return wsdl;
			if(!wsdl.endsWith("/"))
				wsdl += "/";
			String sname = this.getSourceClass().getSimpleName();
			wsdl += sname + "/" + getServiceName() + "?wsdl";
			return wsdl;
		}
		
	}
	
	public static class JBossWebServiceMeta extends WebServiceMeta {

		@SuppressWarnings("rawtypes")
		@Override
		protected String defaultTargetNamespace() {
			Class clazz = getSourceClass();
			String packageName = reversePackageName(clazz.getPackage().getName());
			String qurl = "http://"+packageName+"/";
			return qurl;
		}

		@Override
		protected String defaultPortName() {
			return getSourceClass().getSimpleName()+"Port";
		}

		@Override
		protected String defaultServiceName() {
			return getSourceClass().getSimpleName()+"Service";
		}

		@Override
		public String getWsdlURL(String baseURL) {
			String wsdl = baseURL;
			if(wsdl.toLowerCase().endsWith("?wsdl"))
				return wsdl;
			if(!wsdl.endsWith("/"))
				wsdl += "/";
			String sname = this.getSourceClass().getSimpleName();
			wsdl += sname + "?wsdl";
			return wsdl;
		}
		
	}
	
	
	private static final Map<Container, Class<? extends WebServiceMeta>> metas;
	static {
		Map<Container, Class<? extends WebServiceMeta>> temp = new HashMap<Container, Class<? extends WebServiceMeta>>();
		temp.put(Container.weblogic, WebServiceMeta.class);
		temp.put(Container.jboss, JBossWebServiceMeta.class);
//		temp.put(Container.weblogic_1034, Weblogic1034Meta.class);
		 
		metas = Collections.unmodifiableMap(temp);
	}
	
	public static String reversePackageName(String packageName){
		String[] strs = StringUtils.split(packageName, ".");
		ArrayUtils.reverse(strs);
		String newpackageName = StringUtils.join(strs, ".");
		return newpackageName;
	}
	
	private static Class<? extends WebServiceMeta> getWebServiceMetaClass(Container container){
		if(container==null)
			container = Container.weblogic;
		Class<? extends WebServiceMeta> metaClass = metas.get(container);
		if(metaClass==null)
			LangUtils.throwBaseException("unsupport : " + container);
		return metaClass;
	}
	

	public static WebServiceMeta buildWebServiceMeta(Class<?> serviceInterface, Container container){
		Class<? extends WebServiceMeta> metaClass = getWebServiceMetaClass(container);
		return buildWebServiceMeta(serviceInterface, metaClass);
	}
	
	public static WebServiceMeta buildWebServiceMeta(Class<?> serviceInterface, Class<? extends WebServiceMeta> metaClass){
		WebServiceMeta meta = (WebServiceMeta) ReflectUtils.newInstance(metaClass);
		meta.setSourceClass(serviceInterface);
		WebService ws = AnnotationUtils.findAnnotation(serviceInterface, WebService.class);
		if(ws!=null){
			meta.setTargetNamespace(ws.targetNamespace());
			meta.setServiceName(ws.serviceName());
			meta.setPortName(ws.portName());
			meta.setName(ws.name());
			if(serviceInterface.isInterface()){
				meta.setEndpointInterface(serviceInterface.getName());
			}else{
				meta.setEndpointInterface(ws.endpointInterface());
			}
		}
		return meta;
	}
	
	public static <T> T createWebService(String wsdlAddress, Class<T> serviceInterface){
		return createWebService(wsdlAddress, serviceInterface, Container.weblogic);
	}
	
	public static <T> T createWebService(String wsdlAddress, Class<T> serviceInterface, Container container){
		Class<? extends WebServiceMeta> metaClass = getWebServiceMetaClass(container);
		return createWebService(wsdlAddress, serviceInterface, metaClass);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T createWebService(String wsdlAddress, Class<T> serviceInterface, Class<? extends WebServiceMeta> metaClass){
		T webService = null;
		WebServiceMeta meta = null;
		try {
			meta = buildWebServiceMeta(serviceInterface, metaClass);
			/*ServiceFactory sf = ServiceFactory.newInstance();
			Service service = sf.createService(new URL(wsdlAddress+"?wsdl"), meta.buildServiceQName());
			webService = (T)service.getPort(meta.buildPortQName(), serviceInterface);*/
			wsdlAddress = meta.getWsdlURL(wsdlAddress);
			System.out.println("wsdl: " + wsdlAddress);
			Service service = Service.create(new URL(wsdlAddress), meta.buildServiceQName());
//			service.addPort(meta.buildPortQName(), SOAPBinding.SOAP11HTTP_BINDING, wsdlAddress);
//			webService = service.getPort(serviceInterface);
			webService = service.getPort(meta.buildPortQName(), (Class<T>)meta.getEndpointInterfaceClass());
		} catch (Exception e) {
			e.printStackTrace();
			LangUtils.throwBaseException("create webservice error : wsdl[" + wsdlAddress + "], EndpointInterfaceClass[" + meta.getEndpointInterfaceClass() + "]", e);
		}catch (Error e) {
			e.printStackTrace();
			throw e;
		}
		return webService;
	}

}

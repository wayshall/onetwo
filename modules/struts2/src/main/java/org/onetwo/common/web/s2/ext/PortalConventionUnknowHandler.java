package org.onetwo.common.web.s2.ext;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.ConventionConstants;
import org.apache.struts2.convention.ConventionUnknownHandler;
import org.apache.struts2.convention.ConventionsService;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.apache.struts2.util.ClassLoaderUtils;
import org.onetwo.common.web.config.SiteConfig;
import org.onetwo.common.web.config.DefaultTemplatePathMapper;
import org.onetwo.common.web.config.TemplatePathMapper;
import org.onetwo.common.web.s2.struts2.DefaultPageAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.XWorkException;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.ResultConfig;
import com.opensymphony.xwork2.config.entities.ResultTypeConfig;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.TextParseUtil;

public class PortalConventionUnknowHandler extends ConventionUnknownHandler {
	public static final String REDIRECT_PREFIX = "rd:";
	public static final String FORWARD_PREFIX = "fd:";
	 
	public static class SimpleParsedValueEvaluator implements TextParseUtil.ParsedValueEvaluator {
	        public Object evaluate(Object parsedValue) {
	            if (parsedValue != null) {
	                try {
	                    return URLEncoder.encode(parsedValue.toString(), "UTF-8");
	                }
	                catch(UnsupportedEncodingException e) {
	                    logger.warn("error while trying to encode ["+parsedValue+"]", e);
	                }
	            }
	        return parsedValue;
	    }
	};
	
	protected static Logger logger = LoggerFactory.getLogger(PortalConventionUnknowHandler.class);

	protected ConventionsService conventionsService;
	protected String nameSeparator;
	protected TemplatePathMapper templatePathMapper;
	
	protected TextParseUtil.ParsedValueEvaluator simpleParser = new SimpleParsedValueEvaluator();

	@Inject
	public PortalConventionUnknowHandler(Configuration configuration, ObjectFactory objectFactory, ServletContext servletContext, Container container, @Inject("struts.convention.default.parent.package")
	String defaultParentPackageName, @Inject("struts.convention.redirect.to.slash")
	String redirectToSlash, @Inject("struts.convention.action.name.separator")
	String nameSeparator) {
		super(configuration, objectFactory, servletContext, container, defaultParentPackageName, redirectToSlash, nameSeparator);
		this.conventionsService = container.getInstance(ConventionsService.class, container.getInstance(String.class, ConventionConstants.CONVENTION_CONVENTIONS_SERVICE));
		this.templatePathMapper = container.getInstance(TemplatePathMapper.class, container.getInstance(String.class, ExtConstant.EXT_TEMPLATE_PATH_MAPER));
		this.nameSeparator = nameSeparator;
	}

	protected boolean isTemplateEnable(){
		return this.templatePathMapper!=null && this.templatePathMapper.isEnable();
	}

	public ActionConfig handleUnknownAction(String namespace, String actionName) throws XWorkException {
		if(!isTemplateEnable()){
			return super.handleUnknownAction(namespace, actionName);
		}
		
		if(this.templatePathMapper!=null)
		// String path = this.determinePath(null, namespace);
		if (namespace == null || "/".equals(namespace)) {
			namespace = "";
		}
		Map<String, ResultTypeConfig> resultsByExtension = conventionsService.getResultTypesByExtension(parentPackage);
		String pathPrefix = "";
		ActionConfig actionConfig = null;

		pathPrefix = determinePath(null, namespace);

		pathPrefix = this.templatePathMapper.parse(pathPrefix, false);

		if (!actionName.equals("")) {
			Resource resource = null;
			String parentPath = pathPrefix;
			while (StringUtils.isNotBlank(parentPath)) {
				resource = findAppResource(resultsByExtension.keySet(), parentPath, actionName);
				if (resource == null) {
					parentPath = templatePathMapper.getParent(parentPath);
				} else {
					break;
				}
			}
			if (resource != null) {
				actionConfig = buildActionConfig(resource.path, resultsByExtension.get(resource.ext));
			}
		}
		
		if (actionConfig == null)
			actionConfig = super.handleUnknownAction(namespace, actionName);
		
		return actionConfig;
	}


    @SuppressWarnings("unchecked")
	protected ActionConfig buildActionConfig(String path, ResultTypeConfig resultTypeConfig) {
        Map<String, ResultConfig> results = new HashMap<String,ResultConfig>();
        HashMap<String, String> params = new HashMap<String, String>();
        if (resultTypeConfig.getParams() != null) {
            params.putAll(resultTypeConfig.getParams());
        }
        params.put(resultTypeConfig.getDefaultResultParam(), path);

//        PackageConfig pkg = configuration.getPackageConfig(defaultParentPackageName);
//        List<InterceptorMapping> interceptors = InterceptorBuilder.constructInterceptorReference(pkg,
//            pkg.getFullDefaultInterceptorRef(), Collections.EMPTY_MAP, null, objectFactory);
        ResultConfig config = new ResultConfig.Builder(Action.SUCCESS, resultTypeConfig.getClassName()).
            addParams(params).build();
        results.put(Action.SUCCESS, config);

		ActionConfig def = this.configuration.getRuntimeConfiguration().getActionConfig("", DefaultPageAction.ACTION_NAME);
		  
        return new ActionConfig.Builder(defaultParentPackageName, "execute", DefaultPageAction.class.getName())
        	.addInterceptors(def!=null?def.getInterceptors():Collections.EMPTY_LIST)
            .addResultConfigs(results).build();
    }
    
	protected Resource findAppResource(Collection<String> extensions, String... parts) {
		for (String ext : extensions) {
			String canonicalPath = canonicalize(string(parts) + "." + ext);

			try {
				if (servletContext.getResource(canonicalPath) != null) {
					return new Resource(canonicalPath, ext);
				}
			} catch (MalformedURLException e) {
				if (logger.isErrorEnabled())
					logger.error("Unable to parse path to the web application resource [#0] skipping...", canonicalPath);
			}
		}

		return null;
	}

	public Object handleUnknownActionMethod(Object action, String methodName) throws NoSuchMethodException {
		throw new NoSuchMethodException();
	}

	public Result handleUnknownResult(ActionContext actionContext, String actionName, ActionConfig actionConfig, String resultCode) throws XWorkException {
		if(resultCode.startsWith(REDIRECT_PREFIX)){
            return handleRedirect(actionContext, resultCode);
		}else if(resultCode.startsWith(FORWARD_PREFIX)){
			return handleForward(actionContext, resultCode);
		}
		return super.handleUnknownResult(actionContext, actionName, actionConfig, resultCode);
	}
	
	public Result handleRedirect(ActionContext actionContext, String resultCode){
//		String location = resultCode.substring(REDIRECT_PREFIX.length());
		ActionInvocation invocation = actionContext.getActionInvocation();
//		location = conditionalParse(location, );
        ServletRedirectResult redirect = new ServletRedirectResult();
        actionContext.getContainer().inject(redirect);
        
        String newLocation = handleRedirectPath(invocation, resultCode);
        
        redirect.setLocation(newLocation);
        return redirect;
	}
	
	public static String handleRedirectPath(ActionInvocation invocation, String resultCode){
		String location = resultCode.substring(REDIRECT_PREFIX.length());
		 String newLocation = "";
        if(location.startsWith("/")){
        	newLocation = location;
        }else{
            if(location.startsWith("!")){
            	newLocation += invocation.getProxy().getActionName();
            }else{
            	newLocation = invocation.getProxy().getNamespace() + "/";
            }
            newLocation += location;
        }
        if(newLocation.indexOf("//")!=-1)
        	newLocation = newLocation.replaceAll("/+", "/");
        return newLocation;
	}
	
	public static String handleForwardPath(ActionInvocation invocation, String resultCode){
		String location = resultCode.substring(FORWARD_PREFIX.length());
		String newLocation = "";
        if(location.indexOf('/')==-1){
        	newLocation = invocation.getProxy().getNamespace() + "/";
        }
        newLocation += location;
        if(newLocation.indexOf("//")!=-1)
        	newLocation = newLocation.replaceAll("/+", "/");
        if(newLocation.indexOf(".")==-1){
        	newLocation = newLocation + ".jsp";
    		/*Map<String, ResultTypeConfig> resultsByExtension = conventionsService.getResultTypesByExtension(parentPackage);
    		Resource res = this.findAppResource(resultsByExtension.keySet(), newLocation);
			if(res==null){
				throw new ServiceException("can not find the path : " + newLocation);
			}
			newLocation = res.path;*/
        }
        return newLocation;
	}

	
	public Result handleForward(ActionContext actionContext, String resultCode){
//		String location = resultCode.substring(FORWARD_PREFIX.length());
		//TODO scanResultsByExtension
		ActionInvocation invocation = actionContext.getActionInvocation();
		
		ServletDispatcherResult result = new MutiLanguageDispatcherResult();
        actionContext.getContainer().inject(result);
        
		String newLocation = "/" + SiteConfig.getInstance().getLanguage() + handleForwardPath(invocation, resultCode);
		
        result.setLocation(newLocation);
        return result;
	}

    protected String conditionalParse(String param, ActionInvocation invocation) {
        if (param != null && invocation != null) {
            return TextParseUtil.translateVariables(param, invocation.getStack(), simpleParser);
        } else {
            return param;
        }
    }

	protected Result findResult(String path, String resultCode, String ext, ActionContext actionContext, Map<String, ResultTypeConfig> resultsByExtension) {
		try {
			path = DefaultTemplatePathMapper.getInstance().parse(path, true);
			if (servletContext.getResource(path) != null) {
				return buildResult(path, resultCode, resultsByExtension.get(ext), actionContext);
			}

			String classLoaderPath = path.startsWith("/") ? path.substring(1, path.length()) : path;
			if (ClassLoaderUtils.getResource(classLoaderPath, getClass()) != null) {
				return buildResult(path, resultCode, resultsByExtension.get(ext), actionContext);
			}
		} catch (MalformedURLException e) {
			if (logger.isErrorEnabled())
				logger.error("Unable to parse template path: [#0] skipping...", path);
		}

		return null;
	}

	/***************************************************************************
	 * 处理portal的模板目录
	 */
	/*@Override
	protected String determinePath(ActionConfig actionConfig, String namespace) {
		String path = "";

		
		 * path = (String)ActionContext.getContext().get(namespace);
		 * if(StringUtils.isNotBlank(path)) return path;
		 

		path = super.determinePath(actionConfig, namespace);
		if (StringUtils.isBlank(path)) {
			path = "/";
		}
		if (isAutoLanguage())
			path += StrutsUtils.getCurrentSessionLocale().toString().toLowerCase() + "/";

		// path = TemplatePathMapper.getInstance().parse(path, false);
		// ActionContext.getContext().put(namespace, path);

		return path;
	}*/

	public static class Resource {
		public final String path;
		public final String ext;

		public Resource(String path, String ext) {
			this.path = path;
			this.ext = ext;
		}
	}

}

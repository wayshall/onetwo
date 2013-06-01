package org.onetwo.common.web.subdomain;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.Expression;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.SimpleExpression;
import org.onetwo.common.utils.ValueProvider;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.WebLocaleUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

@SuppressWarnings("unchecked")
abstract public class AbstractSubdomainProcessor implements SubdomainProcessor{
	
	protected Logger logger = Logger.getLogger(AbstractSubdomainProcessor.class);
	
	protected List<SubdomainRule> rules;

	protected Expression se = SimpleExpression.WELL_INSTANCE;;
	
	protected String configFile = "subdomain.xml";
	
	public AbstractSubdomainProcessor() {
	}

	public Class getRuleClass(){
		return SubdomainRule.class;
	}

	public void readConfig() {
		Resource config = new ClassPathResource(configFile);
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("subdomain", List.class);
		xstream.alias("rule", getRuleClass());
		xstream.useAttributeFor(String.class);
		xstream.useAttributeFor(Integer.class);
		xstream.useAttributeFor(int.class);

		try {
			rules = (List<SubdomainRule>) xstream.fromXML(new FileInputStream(config.getFile()));
			logger.info("subdomain config has readed!");
		} catch (Exception e) {
			logger.error("read the subdomain config error!", e);
		}
	}
	
	public void process(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//		this.readConfig();
		String servletPath = RequestUtils.getServletPath(request);
		SubdomainRule rule = getMatchRule(servletPath);
		
		if(rule!=null){
			try{
				servletPath = processDomain(request, rule);

				if(rule.isDispatcher())
					this.sendForward(request, response, servletPath);
				else
					response.sendRedirect(request.getSession().getServletContext().getContextPath()+servletPath);
			}catch(Exception e){
				logger.error("subdomain error!", e);
				throw new ServiceException("subdomain error: " + e.getMessage(), e);
			}
		}else{
			if(logger.isDebugEnabled()){
				logger.info("just next filter...........");
			}
			filterChain.doFilter(request, response);
		}
	}
	
	protected void setCurrentSubDomain(HttpServletRequest request, SubdomainInfo info){
		request.setAttribute(SubDomainFilter.CURRENT_SUB_DOMAIN, info);
	}
	
	protected Locale getCurrentSessionLocale(HttpServletRequest request){
		return WebLocaleUtils.getDefault();
	}
	
	public String processDomain(HttpServletRequest request, SubdomainRule rule){
		String language = getCurrentSessionLocale(request).toString();
		String servletPath = RequestUtils.getServletPath(request);
//		String subdomain = this.getSubomian(servletPath);
		String subdomain = rule.getSubdomain(servletPath);
		
		SubdomainInfo info = getSubdomainMapping().getSubdomainInfo(subdomain, language);
		if(info==null)
			throw new ServiceException("do not support this subdomain ["+language+", "+subdomain+"].");
		
		setCurrentSubDomain(request, info);
		
		if(!language.equals(info.getLanguage())){
			request.getSession().setAttribute(WebLocaleUtils.ATTRIBUTE_KEY, WebLocaleUtils.getClosestLocale(info.getLanguage()));
			language = info.getLanguage();
		}
		
		
		if(logger.isDebugEnabled())
			logger.info("process the sub domain : " + subdomain + "    servletPath: " + servletPath);
		
		
		servletPath = rule.parse(servletPath);
		final Map<String, SubdomainInfo> context = new HashMap<String, SubdomainInfo>();
		context.put("subdomain", info);
		
		if (se.isExpresstion(servletPath)) {
			servletPath = se.parseByProvider(servletPath, new ValueProvider() {
				@Override
				public String findString(String var) {
					Object value = null;
					try {
//						value = Ognl.getValue(var, context);
						value = ReflectUtils.getExpr(context, var);
					} catch (Exception e) {
						logger.error("find subdomain error in context : " + e.getMessage());
					}
					return value == null ? null : value.toString();
				}
			});
		}
		
		if(logger.isDebugEnabled()){
			logger.info("after parse, the servletPath is : " + servletPath);
		}		
		
		return servletPath;
	}
	
	protected void sendForward(HttpServletRequest request, HttpServletResponse response, String uri) throws IOException, ServletException{
		RequestDispatcher rd = request.getRequestDispatcher(uri);
		rd.forward(request, response);
	}
	
	protected SubdomainRule getMatchRule(String servletPath){
		for (SubdomainRule r : rules) {
			if (!r.isMatche(servletPath))
				continue;
			return r;
		}
		return null;
	}
	
	public void reloadConfig(){
		this.rules.clear();
		this.readConfig();
	}
	
	public static void main(String[] args){
		 final Pattern subdomain = Pattern.compile("(?i)(.*)/exhibition-(\\w+)/(.*)");
		AbstractSubdomainProcessor d = null;//new DefaultSubdomainProcessor();
		d.readConfig();
		String path = "/exhibition-guangdong/index.do";
		SubdomainRule r = d.getMatchRule(path);
		System.out.println(r.getForward());
		
		Matcher m = subdomain.matcher(path);
		String domain = "";
		if(m.find()){
			domain = m.group(2);
			System.out.println(domain);
		}
		
		if(r.isMatche(path)){
			System.out.println(r.getSubdomain(path));
		}
	}

}

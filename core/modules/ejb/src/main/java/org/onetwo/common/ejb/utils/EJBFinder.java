package org.onetwo.common.ejb.utils;

import java.util.Enumeration;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.propconf.PropConfig;
import org.onetwo.common.utils.propconf.PropUtils;
import org.slf4j.Logger;

@SuppressWarnings("unchecked")
public class EJBFinder {

    public static final String EJB_REMOTE = "remote";
    public static final String EJB_LOCAL = "local";
    
    public static EJBFinder create(){
    	return create("");
    }
    
    public static EJBFinder create(String jndi){
    	if(StringUtils.isBlank(jndi))
    		jndi = "jndi.properties";
    	EJBFinder ejbfinder = new EJBFinder(jndi);
    	ejbfinder.iniitContext();
    	return ejbfinder;
    }

	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	private Properties jndiConfig;
    private InitialContext context;
	
    protected EJBFinder(String jndiConfigFile){
		PropConfig config = PropUtils.loadPropConfig(jndiConfigFile);
		this.jndiConfig = (Properties) config.getConfig();
	}
	
	protected EJBFinder(Properties properties){
		this.jndiConfig = properties;
	}
	
	protected void iniitContext(){
		 try {
			 Properties prop = new Properties();
			 Enumeration<String> enu = (Enumeration<String>)jndiConfig.propertyNames();
			 while(enu.hasMoreElements()){
				 String key = (String)enu.nextElement();
				 prop.setProperty(key, jndiConfig.getProperty(key));
			 }
//			 context = new InitialContext(prop);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			LangUtils.throwBaseException("InitialContext error : " + e.getMessage(), e);
		}
	}
	

	public <T> T getRemote(Class<T> clazz) {
		return getEJB(clazz, true);
	}
	
	public <T> T getEJB(Class<T> clazz, boolean isRemote) {
		return getEJB(clazz.getSimpleName(), isRemote);
	}

	public String getTheJndiName(String ejbName, boolean isRemote){
        String type = isRemote?EJB_REMOTE:EJB_LOCAL;
		String jndi = ejbName + "/" + type;
		return jndi;
	}
	
	public <T> T getEJB(String ejbName, boolean isRemote) {
        Assert.hasText(ejbName);
        
        T ejbInterface = null;
		try {
			ejbInterface = (T) context.lookup(getTheJndiName(ejbName, isRemote));
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
			LangUtils.throwBaseException("getEJB lookup error : " + e.getMessage(), e);
		}
        return ejbInterface;
    }

	public Properties getJndiConfig() {
		return jndiConfig;
	}

	public InitialContext getContext() {
		return context;
	}
}

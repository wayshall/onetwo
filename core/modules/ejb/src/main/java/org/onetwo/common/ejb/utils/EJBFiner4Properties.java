package org.onetwo.common.ejb.utils;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.propconf.PropConfig;
import org.onetwo.common.utils.propconf.PropUtils;
import org.slf4j.Logger;

public class EJBFiner4Properties {
	private static final Logger logger = MyLoggerFactory.getLogger(EJBFinder.class);
	
	private EJBFinder ejbFinder;
	private PropConfig nameMapper;
	
	public EJBFiner4Properties(String nameMapperFile, String jndiConfig) {
		this.ejbFinder = EJBFinder.create(jndiConfig);
		this.nameMapper = PropUtils.loadPropConfig(nameMapperFile);
	}
	
	public <T> T getRemote(Class<T> intface){
		return getEJB(intface, null, true);
	}
	
	public <T> T getRemote(String ejbNameKey){
		return getEJB(null, ejbNameKey, true);
	}
	
	public <T> T getEJB(Class<T> intface, String ejbNameKey, boolean isRemote){
        String ejbName = null;
        T ejbService = null;
		try {
			if(StringUtils.isNotBlank(ejbNameKey)){
		        ejbName = nameMapper.getProperty(ejbNameKey);
			}else if(intface!=null){
				ejbName = intface.getSimpleName();
			}else{
				throw new RuntimeException("no jndi!");
			}
	        ejbService = ejbFinder.getEJB(ejbName, isRemote);
	        if(ejbService==null)
				LangUtils.throwBaseException("can not find ejb : " + ejbName);
		} catch (Exception e) {
			String msg = "get ejb service error : ejbNameKey["+ejbNameKey+"], ejb interface["+intface+"], message[" + e.getMessage()+"]";
			logger.error(msg, e);
			LangUtils.throwBaseException(msg, e);
		}
		return ejbService;
	}
	
}

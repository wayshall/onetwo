package org.onetwo.common.utils.bean;

import java.io.File;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.propconf.PropUtils;

public abstract class BeanDuplicatorFacotry {
	
	public static final String DEFAULT_CONFIG = "mydozer.properties";
	
	private static BeanDuplicator DEFAULT_DUPLICATOR;
	
	
	public synchronized static BeanDuplicator getDefault(){
		if(DEFAULT_DUPLICATOR==null){
			DEFAULT_DUPLICATOR = create();
		}
		return DEFAULT_DUPLICATOR;
	}
	
	public static BeanDuplicator create(){
		return create(DEFAULT_CONFIG);
	}

	public static BeanDuplicator create(String config) {
		return create(PropUtils.class.getClassLoader(), config);
	}
	
	public static Properties loadConfig(ClassLoader cld, String config){
		if(StringUtils.isBlank(config))
			config = DEFAULT_CONFIG;
		Properties props = null;
		try {
			props = PropUtils.loadProperties(new File(FileUtils.getResourcePath(cld, config)));
		} catch (Exception e) {
			LangUtils.throwBaseException("create BeanDuplicator error : " + e.getMessage(), e);
		}
		return props;
	}
	
	public static BeanDuplicator create(ClassLoader cld, String config) {
		Properties props = loadConfig(cld, config);
		BeanDuplicator beanDuplicator = new BeanDuplicator(props);
		beanDuplicator.initDozer();
		return beanDuplicator;
	}
	
	public static BeanDuplicator create(Properties props) {
		Assert.notNull(props);
		BeanDuplicator beanDuplicator = new BeanDuplicator(props);
		beanDuplicator.initDozer();
		return beanDuplicator;
	}

	public static BeanDuplicator inst(){
		return getDefault();
	}
}

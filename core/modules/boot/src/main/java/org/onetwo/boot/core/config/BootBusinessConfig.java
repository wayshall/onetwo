package org.onetwo.boot.core.config;

import org.onetwo.common.propconf.JFishProperties;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

/***
 * 需要使用到的bussiness的配置
 * 继承JFishProperties，使用map
 * @author way
 *
 */
@SuppressWarnings("serial")
@ConfigurationProperties(prefix="business")
public class BootBusinessConfig extends JFishProperties {
	
	@Autowired
	private BootSpringConfig bootSpringConfig;
	
	private BeanWrapper bw = SpringUtils.newBeanMapWrapper(this);

	public BootSpringConfig getBootSpringConfig() {
		return bootSpringConfig;
	}

    public String getProperty(String key) {
    	Object val = bw.getPropertyValue(key);
    	return StringUtils.emptyIfNull(val);
    }

    public String getProperty(String key, String def) {
    	Object val = bw.getPropertyValue(key);
    	return StringUtils.defaultValue(StringUtils.emptyIfNull(val), def);
    }
	
}

package org.onetwo.common.ds;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.DataSource;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.config.JFishPropertyPlaceholder;
import org.onetwo.common.spring.utils.BeanPropertiesMapper;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class DatasourceFactoryBean implements FactoryBean<DataSource>, InitializingBean {

//	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private String implementClassName = "org.apache.tomcat.jdbc.pool.DataSource";
	
	private DataSource dataSource;
	
	private Class<? extends DataSource> implementClass;
	private Properties config;
	private String prefix;
	private Map<String, Object> configMap;
	
	@Autowired(required = false)
	private JFishPropertyPlaceholder configHolder;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() throws Exception {
		if(config==null){
			if (configHolder!=null) {
				config = configHolder.getMergedConfig();
			} else {
				config = new Properties();
			}
		}
//		boolean hasPrefix = StringUtils.isNotBlank(prefix);
		
		if (implementClass!=null) {
			dataSource = BeanUtils.instantiate(implementClass);
		} else if (StringUtils.isNotBlank(implementClassName)) {
			this.implementClass  = (Class<? extends DataSource>)ReflectUtils.loadClass(implementClassName);
			dataSource = BeanUtils.instantiate(implementClass);
		} else {
			throw new BaseException("implementClass can not be null!");
		}
		
		if (this.configMap!=null) {
			for (Entry<String, Object> config : this.configMap.entrySet()) {
				this.config.setProperty(config.getKey(), config.getValue().toString());
			}
		}
		
		BeanPropertiesMapper mapper = new BeanPropertiesMapper(config, prefix);
		mapper.mapToObject(dataSource);
	}

	@Override
	public DataSource getObject() throws Exception {
		return dataSource;
	}

	@Override
	public Class<?> getObjectType() {
		return DataSource.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setImplementClass(Class<? extends DataSource> implementClass) {
		this.implementClass = implementClass;
	}

	public void setConfig(Properties config) {
		this.config = config;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setImplementClassName(String implementClassName) {
		this.implementClassName = implementClassName;
	}

	public void setConfigMap(Map<String, Object> configMap) {
		this.configMap = configMap;
	}
	

}

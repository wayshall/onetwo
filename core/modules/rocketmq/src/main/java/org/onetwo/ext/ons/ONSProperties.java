package org.onetwo.ext.ons;

import java.util.Map;
import java.util.Properties;

import lombok.Data;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.google.common.collect.Maps;

/**
 * @author wayshall
 * <br/>
 */
@Data
public class ONSProperties {

	String accessKey;
	String secretKey;
	String onsAddr;
	Map<String, ProducerProperties> producers = Maps.newHashMap();
	Map<String, ConsumerProperties> consumers = Maps.newHashMap();
	

	public Properties baseProperties(){
		Properties baseConfig = new Properties();
		baseConfig.setProperty(PropertyKeyConst.AccessKey, accessKey);
		baseConfig.setProperty(PropertyKeyConst.SecretKey, secretKey);
		baseConfig.setProperty(PropertyKeyConst.ONSAddr, onsAddr);
		return baseConfig;
	}
	public class ConsumerProperties {
	}
	public class ProducerProperties {
	}
	
}

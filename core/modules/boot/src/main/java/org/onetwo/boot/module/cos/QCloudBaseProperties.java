package org.onetwo.boot.module.cos;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @see org.onetwo.ext.QCloudBaseProperties.qcloud.QCloudProperties
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(QCloudBaseProperties.PREFIX)
@Data
public class QCloudBaseProperties {

	final public static String PREFIX = "qcloud";
	
	//腾讯云账户secretId，secretKey
	String secretId;
	String secretKey;
	
}

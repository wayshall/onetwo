package org.onetwo.cloud.feign;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */

@ConfigurationProperties("ribbon")
@Data
public class RibbonProperties {
	
    int connectTimeout = 10000;
    int readTimeout = 50000;
    /***
     * 0: no timeout
     */
    int writeTimeout = 0;

}

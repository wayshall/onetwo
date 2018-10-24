package org.onetwo.plugins.admin.vo;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@Data
public class CaptchaResponse {
	String data;
	String sign;
}

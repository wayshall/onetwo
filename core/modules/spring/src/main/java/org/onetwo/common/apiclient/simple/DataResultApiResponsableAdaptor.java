package org.onetwo.common.apiclient.simple;

import org.onetwo.common.apiclient.ApiResponsable;
import org.onetwo.common.data.Result;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@AllArgsConstructor
@Data
public class DataResultApiResponsableAdaptor implements ApiResponsable<String> {
	
	final private Result result;

	@Override
	public String resultCode() {
		return result.getCode();
	}

	@Override
	public String resultMessage() {
		return result.getMessage();
	}
	
	public boolean isSuccess() {
		return result.isSuccess();
	}

}


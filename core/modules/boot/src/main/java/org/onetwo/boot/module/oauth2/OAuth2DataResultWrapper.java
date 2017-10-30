package org.onetwo.boot.module.oauth2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.onetwo.boot.core.web.view.DefaultDataResultWrapper;
import org.onetwo.common.data.AbstractDataResult;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wayshall
 * <br/>
 */
public class OAuth2DataResultWrapper extends DefaultDataResultWrapper {

	@Override
	public Object wrapResult(final Object data) {
		Object result = super.wrapResult(data);
		if(result instanceof AbstractDataResult){
			AbstractDataResult<?> dr = (AbstractDataResult<?>)super.wrapResult(data);
			if(dr.isError()){
				OAuth2Result or = OAuth2Result.builder()
						  .error(dr.getCode())
						  .errorDescription(dr.getMessage())
						  .build();
				return or;
			}
			result = dr.getData();
		}
		return result;
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class OAuth2Result {
		String error;
		@JsonProperty("error_description")
		String errorDescription;
		Object data;
	}
}

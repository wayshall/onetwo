package org.onetwo.common.apiclient.simple;

import java.util.Map;

import org.onetwo.common.apiclient.ApiClientMethod;
import org.onetwo.common.apiclient.ApiCustomizeMappingField;
import org.onetwo.common.apiclient.ApiResponsable;
import org.onetwo.common.apiclient.impl.DefaultApiClientResponseHandler;
import org.onetwo.common.data.Result;
import org.onetwo.common.exception.ApiClientException;
import org.onetwo.common.exception.ErrorTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

/**
 * @author wayshall
 * <br/>
 */
public class SimpleApiClientResponseHandler<M extends ApiClientMethod> extends DefaultApiClientResponseHandler<M> {
	private String resultCodeField = "";
	private String resultMessageField = "";
	
	public SimpleApiClientResponseHandler() {
	}
	
	public SimpleApiClientResponseHandler(String resultCodeField, String resultMessageField) {
		super();
		this.resultCodeField = resultCodeField;
		this.resultMessageField = resultMessageField;
	}

	@Override
	public Class<?> getActualResponseType(M invokeMethod){
		Class<?> responseType = invokeMethod.getMethodReturnType();
		if (ApiCustomizeMappingField.class.isAssignableFrom(responseType)) {
			return Map.class;
		}
		return responseType;
	}
	
	protected boolean hasResultCodeField(Map<String, ?> responseMap) {
		return responseMap.containsKey(resultCodeField);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Object handleResponse(M invokeMethod, ResponseEntity<?> responseEntity, Class<?> actualResponseType){
		Object response = responseEntity.getBody();
		ApiResponsable<?> baseResponse = null;
		if(responseEntity.getStatusCode().is2xxSuccessful()){
			if(ApiResponsable.class.isInstance(response)){
				baseResponse = (ApiResponsable<?>) response;
			} else if (Result.class.isAssignableFrom(actualResponseType)) {
				Result result = (Result) response;
				baseResponse = new DataResultApiResponsableAdaptor(result);
			} else if (Map.class.isAssignableFrom(actualResponseType)){
				//reponseType have not define errcode and errmsg
				Map<String, ?> map = (Map<String, ?>) response;
				if (hasResultCodeField(map)) {
					baseResponse = createBaseResponseByMap(map);
					if(!invokeMethod.isReturnVoid()){
//						response = map2Bean(map, invokeMethod.getMethodReturnType());
						response = handleResponseMap(map, invokeMethod.getMethodReturnType());
					}
				} else if (invokeMethod.isReturnVoid()) {
					//返回值为void，并且请求没有返回错误，则返回null
					return null;
				}
				else {
//					response = map2Bean(map, invokeMethod.getMethodReturnType());
					response = handleResponseMap(map, invokeMethod.getMethodReturnType());
				}
			} else {
				if(logger.isDebugEnabled()){
					logger.debug("Non-ApiResponse type: {}", response.getClass());
				}
			}
			
			if(baseResponse!=null && !baseResponse.isSuccess() && invokeMethod.isAutoThrowIfErrorCode()){
				logger.error("api[{}] error response: {}", invokeMethod.getMethod().getName(), baseResponse);
				throw translateToApiClientException(invokeMethod, baseResponse, responseEntity);
//				throw new ApiClientException(ErrorTypes.of(baseResponse.getErrcode().toString(), baseResponse.getErrmsg(), responseEntity.getStatusCodeValue()));
			}
			
			if(invokeMethod.isReturnVoid()){
				//返回值为void，并且请求没有返回错误，则返回null
				return null;
			}
			
			return response;
		} else {
			// 其它非200的http代码，视为失败s
			if (response instanceof ApiResponsable) {
				baseResponse = (ApiResponsable<?>) response;
				throw new ApiClientException("api[" + invokeMethod.getMethod().getName() + "] error response, code: " + baseResponse.resultCode() + ", message: " + baseResponse.resultMessage());
			} else {
				throw new RestClientException("api[" + invokeMethod.getMethod().getName() + "] error response: " + responseEntity.getStatusCodeValue());
			}
		}
	}
	
	public Object handleResponseMap(Map<String, ?> map, Class<?> responseType) {
		if (Map.class.isAssignableFrom(responseType)) {
			return map;
		}
		Object response = map2Bean(map, responseType);
		if (response instanceof ApiCustomizeMappingField) {
			ApiCustomizeMappingField customRes = (ApiCustomizeMappingField) response;
			customRes.mappingFields(map);
		}
		return response;
	}
	
	protected ApiResponsable<?> createBaseResponseByMap(Map<String, ?> map) {
		SimpleApiResponsable baseResponse = new SimpleApiResponsable();
		baseResponse.setResultCode(map.get(resultCodeField));
		baseResponse.setResultMessage((String)map.get(resultMessageField));
		return baseResponse;
	}
	

	protected ApiClientException translateToApiClientException(ApiClientMethod invokeMethod, ApiResponsable<?> baseResponse, ResponseEntity<?> responseEntity){
		return new ApiClientException(ErrorTypes.of(baseResponse.resultCode().toString(), 
					baseResponse.resultMessage(), 
					responseEntity.getStatusCodeValue())
				);
	}

	public String getResultCodeField() {
		return resultCodeField;
	}

	public void setResultCodeField(String resultCodeField) {
		this.resultCodeField = resultCodeField;
	}

	public String getResultMessageField() {
		return resultMessageField;
	}

	public void setResultMessageField(String resultMessageField) {
		this.resultMessageField = resultMessageField;
	}

}

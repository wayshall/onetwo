package org.onetwo.boot.module.oauth2.result;

import java.util.List;

import org.onetwo.boot.core.json.ObjectMapperProvider;
import org.onetwo.boot.core.web.view.ExtJackson2HttpMessageConverter;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.CUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.provider.error.DefaultOAuth2ExceptionRenderer;

/**
 * @author wayshall
 * <br/>
 */
public class DataResultOAuth2ExceptionRenderer extends DefaultOAuth2ExceptionRenderer /*implements OAuth2ExceptionRenderer*/ {

	@SuppressWarnings("unchecked")
	public DataResultOAuth2ExceptionRenderer(ObjectMapperProvider provider) {
		super();
		List<HttpMessageConverter<?>> messageConverters = (List<HttpMessageConverter<?>>) SpringUtils.newPropertyAccessor(this, true).getPropertyValue("messageConverters");
		ExtJackson2HttpMessageConverter jsonConverter = new ExtJackson2HttpMessageConverter();
		jsonConverter.setObjectMapperProvider(provider);
		try {
			jsonConverter.afterPropertiesSet();
		} catch (Exception e) {
			throw new BaseException("create ExtJackson2HttpMessageConverter error!", e);
		}
		CUtils.replaceOrAdd(messageConverters, MappingJackson2HttpMessageConverter.class, jsonConverter);
	}
	
	

	/*private OAuth2ExceptionRenderer exceptionRenderer = new DefaultOAuth2ExceptionRenderer();
	
	@SuppressWarnings("unchecked")
	@Override
	public void handleHttpEntityResponse(HttpEntity<?> responseEntity, ServletWebRequest webRequest) throws Exception {
		ResponseEntity<OAuth2Exception> resEntity = (ResponseEntity<OAuth2Exception>) responseEntity;
		OAuth2Exception ex = resEntity.getBody();
		DataResult<?> dr = DataResults.error(ex.getMessage())
									  .code(ex.getOAuth2ErrorCode())
									  .data(ex.getAdditionalInformation())
									  .build();
		HttpEntity<?> dataEntity = new ResponseEntity<DataResult<?>>(dr, responseEntity.getHeaders(), resEntity.getStatusCode());
		exceptionRenderer.handleHttpEntityResponse(dataEntity, webRequest);
	}*/

}

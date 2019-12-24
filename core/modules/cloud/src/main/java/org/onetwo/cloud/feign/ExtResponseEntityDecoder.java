package org.onetwo.cloud.feign;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.core.web.mvc.exception.BootWebExceptionHandler;
import org.onetwo.cloud.feign.ResultErrorDecoder.FeignResponseAdapter;
import org.onetwo.common.data.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.data.DataResult;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.zuul.filters.route.okhttp.OkHttpRibbonCommand;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpMessageConverterExtractor;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.exception.HystrixRuntimeException.FailureType;

import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wayshall
 * <br/>
 */
@Slf4j
public class ExtResponseEntityDecoder implements Decoder {
	private ObjectFactory<HttpMessageConverters> messageConverters;

	public ExtResponseEntityDecoder(ObjectFactory<HttpMessageConverters> messageConverter) {
		this.messageConverters = messageConverter;
	}


	@SuppressWarnings({ "rawtypes" })
	protected Object decode(FeignResponseAdapter response, Type type) throws IOException, FeignException {
		Object res = null;
		List<String> errorHeaders = response.getHeaders().get(BootWebExceptionHandler.ERROR_RESPONSE_HEADER);
		try {
			if(LangUtils.isEmpty(errorHeaders)){
				//没有错误
				res = decodeByType(response, type);
			}else{
				SimpleDataResult dr = decodeByType(response, SimpleDataResult.class);
				/*if(isCutoutError(response, dr)){
					ServiceException cause = new ServiceException(dr.getMessage(), dr.getCode());
					String message = "cutoutError, remote service error:"+dr.getMessage();
					JFishLoggerFactory.findMailLogger().error(message);
					
					throw new HystrixRuntimeException(FailureType.SHORTCIRCUIT, OkHttpRibbonCommand.class, message, cause, null);
				}else if(dr.isError()){
					throw new HystrixBadRequestException(dr.getMessage(), new ServiceException(dr.getMessage(), dr.getCode()));
				}
				res = dr.getData();*/
				res = dr;
			}
			res = handleDataResult(response, res);
		} catch (HttpMessageNotReadableException e) {
			if(log.isErrorEnabled()){
				String msg = String.format("decode error, try to use[%s] to decode again, error message: %s", SimpleDataResult.class.getSimpleName(), e.getMessage());
				log.error(msg);
				JFishLoggerFactory.findMailLogger().error(msg, e);
			}
			//兼容。。。。。。。。正常解码失败后尝试用SimpleDataResult解码
			response.getBody().reset();
			SimpleDataResult dr = decodeByType(response, SimpleDataResult.class);
			if(dr.isError()){
				if(StringUtils.isNotBlank(dr.getCode())){
					throw new HystrixBadRequestException(dr.getMessage(), new ServiceException(dr.getMessage(), dr.getCode()));
				}else{
					throw new HystrixBadRequestException(e.getMessage(), new ServiceException("decode error", e));
				}
			}
			res = dr.getData();
		}
		return res;
	}
	
	protected Object handleDataResult(FeignResponseAdapter response, Object res) {
		Object result = res;
		if (result instanceof DataResult) {
			DataResult<?> dr = (DataResult<?>) result;
			if(isCutoutError(response, dr)){
				ServiceException cause = new ServiceException(dr.getMessage(), dr.getCode());
				String message = "cutoutError, remote service error:"+dr.getMessage();
				JFishLoggerFactory.findMailLogger().error(message);
				
				throw new HystrixRuntimeException(FailureType.SHORTCIRCUIT, OkHttpRibbonCommand.class, message, cause, null);
			}else if(!dr.isSuccess()){
				throw new HystrixBadRequestException(dr.getMessage(), new ServiceException(dr.getMessage(), dr.getCode()));
//				throw new ServiceException(dr.getMessage(), dr.getCode());
			}
			res = dr.getData();
		}
		return result;
	}
	
	protected boolean isCutoutError(FeignResponseAdapter response, DataResult<?> dr){
		//TODO dr.getCode().startWith("SHORTCIRCUIT_")，暂时不需要
		return false;
	}

	@SuppressWarnings({ "rawtypes" })
	@Deprecated
	protected Object decode2(FeignResponseAdapter response, Type type) throws IOException, FeignException {
		Object res = null;
		try {
			res = decodeByType(response, type);
		} catch (HttpMessageNotReadableException e) {
			//正常解码失败后尝试用SimpleDataResult解码
			response.getBody().reset();
			SimpleDataResult dr = decodeByType(response, SimpleDataResult.class);
			if(dr.isError()){
//				throw new ServiceException(dr.getMessage(), dr.getCode());
				throw new HystrixBadRequestException(dr.getMessage(), new ServiceException(dr.getMessage(), dr.getCode()));
			}
			res = dr.getData();
		}
		return res;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T> T decodeByType(FeignResponseAdapter response, Type type) throws IOException, FeignException {
		HttpMessageConverterExtractor<SimpleDataResult> extractor = new HttpMessageConverterExtractor(
				type, this.messageConverters.getObject().getConverters());
		T dr = (T)extractor.extractData(response);
		return dr;
	}


	@SuppressWarnings("resource")
	@Override
	public Object decode(final Response response, Type type) throws IOException, FeignException {
		FeignResponseAdapter responseAdapter = new FeignResponseAdapter(response);
		//这部分判断基本copy cloud的实现，实现兼容性
		if (isParameterizeHttpEntity(type)) {
			type = ((ParameterizedType) type).getActualTypeArguments()[0];
			Object decodedObject = decode(responseAdapter, type);

			return createResponse(decodedObject, response);
		} else if (isHttpEntity(type)) {
			return createResponse(null, response);
		} else if (isOptional(type)) {
			Type actualType = ReflectUtils.getGenricType(type, 0);
			Object result = decode(responseAdapter, actualType);
			return Optional.ofNullable(result);
		}
		else {
			return decode(responseAdapter, type);
		}
		
	}

	private boolean isParameterizeHttpEntity(Type type) {
		if (type instanceof ParameterizedType) {
			return isHttpEntity(((ParameterizedType) type).getRawType());
		}
		return false;
	}
	
	private boolean isOptional(Type type) {
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			Type rawType = parameterizedType.getRawType();
			return Optional.class.equals(rawType);
		}
		return false;
	}

	private boolean isHttpEntity(Type type) {
		if (type instanceof Class) {
			Class<?> c = (Class<?>) type;
			return HttpEntity.class.isAssignableFrom(c);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private <T> ResponseEntity<T> createResponse(Object instance, Response response) {

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		for (String key : response.headers().keySet()) {
			headers.put(key, new LinkedList<>(response.headers().get(key)));
		}

		return new ResponseEntity<>((T) instance, headers, HttpStatus.valueOf(response
				.status()));
	}

}

package org.onetwo.cloud.feign;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;

import org.onetwo.cloud.feign.ResultErrorDecoder.FeignResponseAdapter;
import org.onetwo.common.data.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.exception.ServiceException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpMessageConverterExtractor;

import com.netflix.hystrix.exception.HystrixBadRequestException;

import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;

/**
 * @author wayshall
 * <br/>
 */
public class ExtResponseEntityDecoder implements Decoder {
	private ObjectFactory<HttpMessageConverters> messageConverters;

	public ExtResponseEntityDecoder(ObjectFactory<HttpMessageConverters> messageConverter) {
		this.messageConverters = messageConverter;
	}


	@SuppressWarnings({ "rawtypes" })
	protected Object decode(FeignResponseAdapter response, Type type) throws IOException, FeignException {
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
		}
		else if (isHttpEntity(type)) {
			return createResponse(null, response);
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

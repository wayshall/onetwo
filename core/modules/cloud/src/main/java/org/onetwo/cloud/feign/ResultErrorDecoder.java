package org.onetwo.cloud.feign;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.onetwo.common.data.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpMessageConverterExtractor;

import com.netflix.hystrix.exception.HystrixBadRequestException;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

/** 除了2xx和404，都算错误，会调用，详见：
 * SynchronousMethodHandler#executeAndDecode
 * @author wayshall
 * <br/>
 */
@Slf4j
public class ResultErrorDecoder implements ErrorDecoder {
	
	final private Default defaultDecoder = new Default();
	private HttpMessageConverters httpMessageConverters;
	
    public ResultErrorDecoder(HttpMessageConverters httpMessageConverters) {
		super();
		this.httpMessageConverters = httpMessageConverters;
	}

	@Override
    public Exception decode(String methodKey, Response response) {
		if((response.status() >= 200 && response.status() < 300) || 
				response.status()==HttpStatus.BAD_REQUEST.value() ||
				response.status()==HttpStatus.INTERNAL_SERVER_ERROR.value() ){
			try {
				HttpMessageConverterExtractor<SimpleDataResult<Object>> extractor = new HttpMessageConverterExtractor<SimpleDataResult<Object>>(SimpleDataResult.class, this.httpMessageConverters.getConverters());
				SimpleDataResult<Object> result = extractor.extractData(new FeignResponseAdapter(response));
				log.error("method[{}], error code: {}, result: {}", methodKey, response.status(), result);
				//防止普通异常也被熔断,如果不转为 HystrixBadRequestException 并且 fallback 也抛了异常, it will be enabled short-circuited get "Hystrix circuit short-circuited and is OPEN" when client frequently invoke
				if(result!=null){
					return new HystrixBadRequestException(result.getMessage(), new ServiceException(result.getMessage(), result.getCode()));
				}
			} catch (IOException e) {
				throw new BaseException("error feign response : " + e.getMessage(), e);
			}
		}
		
    	Exception exception = defaultDecoder.decode(methodKey, response);
    	return exception;
    }
	
	public static class FeignResponseAdapter implements ClientHttpResponse {

		private final Response response;
		private InputStream inputStream;

		public FeignResponseAdapter(Response response) {
			this.response = response;
		}

		@Override
		public HttpStatus getStatusCode() throws IOException {
			return HttpStatus.valueOf(this.response.status());
		}

		@Override
		public int getRawStatusCode() throws IOException {
			return this.response.status();
		}

		@Override
		public String getStatusText() throws IOException {
			return this.response.reason();
		}
		
		public String toString(){
			try {
				return IOUtils.toString(getBody());
			} catch (Exception e) {
				throw new BaseException("error read input body!");
			}
		}

		@Override
		public void close() {
			try {
				this.response.body().close();
			}
			catch (IOException ex) {
				// Ignore exception on close...
			}
		}

		@Override
		public InputStream getBody() throws IOException {
			InputStream in = this.inputStream;
			if(in==null){
				in = this.response.body().asInputStream();
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				try {
					IOUtils.copy(in, output);
				} finally{
					IOUtils.closeQuietly(in);
				}
				in = new ByteArrayInputStream(output.toByteArray());
				this.inputStream = in;
			}
			return in;
		}

		@Override
		public HttpHeaders getHeaders() {
			return getHttpHeaders(this.response.headers());
		}

	}

	static HttpHeaders getHttpHeaders(Map<String, Collection<String>> headers) {
		HttpHeaders httpHeaders = new HttpHeaders();
		for (Map.Entry<String, Collection<String>> entry : headers.entrySet()) {
			httpHeaders.put(entry.getKey(), new ArrayList<>(entry.getValue()));
		}
		return httpHeaders;
	}
}

package org.onetwo.common.apiclient.api.simple;

import org.onetwo.common.apiclient.ApiClientMethod;
import org.onetwo.common.apiclient.ApiHeaderCallback;
import org.onetwo.common.apiclient.CustomResponseHandler;
import org.onetwo.common.apiclient.annotation.ResponseHandler;
import org.onetwo.common.apiclient.annotation.RestApiClient;
import org.onetwo.common.apiclient.response.WeatherResponse;
import org.onetwo.common.jackson.JsonMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author wayshall
 * <br/>
 */
@RestApiClient
public interface WeatherClient {
	
	String BASE_URL = "http://www.weather.com.cn/data";
	
	@GetMapping(value="/sk/{cityid}.html", produces=MediaType.APPLICATION_JSON_VALUE)
	WeatherResponse getWeather(@PathVariable String cityid, HttpHeaders header, ApiHeaderCallback callback);
	

	@GetMapping(value="/sk/{cityid}.html")
	@ResponseHandler(WeatherResponseHandler.class)
	WeatherResponse getWeatherWithHandler(@PathVariable String cityid);
	
	class WeatherResponseHandler implements CustomResponseHandler<byte[]> {
		@Override
		public Object handleResponse(ApiClientMethod apiMethod, ResponseEntity<byte[]> responseEntity) {
			return JsonMapper.IGNORE_NULL.fromJson(responseEntity.getBody(), WeatherResponse.class);
		}
	}

}

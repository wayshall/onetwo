package org.onetwo.common.apiclient.api;

import org.onetwo.common.apiclient.annotation.RestApiClient;
import org.onetwo.common.apiclient.response.WeatherResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author wayshall
 * <br/>
 */
@RestApiClient
public interface WeatherClient {
	
	@GetMapping(value="/sk/{cityid}.html")
	WeatherResponse getWeather(@PathVariable("cityid") String cityid);

}

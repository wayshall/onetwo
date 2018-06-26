package org.onetwo.common.apiclient;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.onetwo.common.apiclient.api.WeatherClient;
import org.onetwo.common.apiclient.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

/**
 * @author wayshall
 * <br/>
 */
public class WeatherClientTest extends ApicientBaseTests {
	
	@Autowired
	WeatherClient weatherClient;
	
	@Test
	public void testGetWeather(){
		HttpHeaders headers = new HttpHeaders();
		headers.set("header1", "header value");
		WeatherResponse res = this.weatherClient.getWeather("101010100", headers, h->{
			h.set("auth", "testvalue");
			System.out.println("headers: " + h);
		});
		assertThat(res.getWeatherinfo().getCity()).isEqualTo("北京");
		

		res = this.weatherClient.getWeatherWithHandler("101010100");
		assertThat(res.getWeatherinfo().getCity()).isEqualTo("北京");
	}

}

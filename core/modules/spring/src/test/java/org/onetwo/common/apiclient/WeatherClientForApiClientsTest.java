package org.onetwo.common.apiclient;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.onetwo.common.apiclient.api.simple.WeatherClient;
import org.onetwo.common.apiclient.response.WeatherResponse;
import org.onetwo.common.apiclient.utils.ApiClients;
import org.springframework.http.HttpHeaders;

/**
 * @author wayshall
 * <br/>
 */
public class WeatherClientForApiClientsTest {
	
	@Test
	public void testGetWeather(){
		WeatherClient weatherClient = ApiClients.newClient(WeatherClient.class);
		HttpHeaders headers = new HttpHeaders();
		headers.set("header1", "header value");
		WeatherResponse res = weatherClient.getWeather("101010100", headers, h->{
			h.set("auth", "testvalue");
			System.out.println("headers: " + h);
		});
		assertThat(res.getWeatherinfo().getCity()).isEqualTo("北京");
		

		res = weatherClient.getWeatherWithHandler("101010100");
		assertThat(res.getWeatherinfo().getCity()).isEqualTo("北京");
	}

}

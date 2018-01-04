package org.onetwo.common.apiclient;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.onetwo.common.apiclient.api.WeatherClient;
import org.onetwo.common.apiclient.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wayshall
 * <br/>
 */
public class WeatherClientTest extends ApicientBaseTests {
	
	@Autowired
	WeatherClient weatherClient;
	
	@Test
	public void test(){
		WeatherResponse res = this.weatherClient.getWeather("101010100", headers->{
			headers.set("auth", "testvalue");
			System.out.println("headers: " + headers);
		});
		assertThat(res.getWeatherinfo().getCity()).isEqualTo("北京");
	}

}

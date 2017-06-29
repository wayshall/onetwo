package org.onetwo.common.apiclient;

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
		WeatherResponse res = this.weatherClient.getWeather("101010100");
		System.out.println("res: " + res);
	}

}

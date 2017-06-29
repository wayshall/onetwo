package org.onetwo.common.apiclient.response;

import lombok.Data;

import org.onetwo.common.apiclient.annotation.SupportedMediaType;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wayshall
 * <br/>
 */
@Data
@SupportedMediaType(MediaType.TEXT_HTML_VALUE)
public class WeatherResponse {
	
	private Weatherinfo weatherinfo;

	@Data
	public static class Weatherinfo {
		String city;
		String cityid;
		String temp;
		@JsonProperty("WD")
		String WD;
		@JsonProperty("WS")
		String WS;
		@JsonProperty("SD")
		String SD;
		@JsonProperty("WSE")
		String WSE;
		String time;
		String isRadar;
		@JsonProperty("Radar")
		String radar;
		String njd;
		String qy;
		String rain;
	}

}

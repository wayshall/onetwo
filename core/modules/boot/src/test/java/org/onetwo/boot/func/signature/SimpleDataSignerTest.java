package org.onetwo.boot.func.signature;
/**
 * @author weishao zeng
 * <br/>
 */

import org.junit.Test;
import org.onetwo.common.annotation.IgnoreField;
import org.onetwo.common.utils.DataSigner.SignableRequest;
import org.onetwo.common.utils.DataSigner.SigningCheckableData;
import org.onetwo.common.utils.DataSigner.SigningConfig;
import org.onetwo.common.utils.DataSigner.SigningData;

import lombok.Data;

public class SimpleDataSignerTest {
	
	@Test
	public void test() {
		SimpleDataSigner singer = new SimpleDataSigner();
		
		SigningConfig config = new SigningConfig();
		config.setMaxDelayTimeInSeconds(10);
		config.setSigningKey("lcgNTCJl8Q652N2as7iTlangchaovJ7dx5zH7Ls1XHJlrMwFgtP9FRzwYa0JgTabM9NtYOiRrC9VUWJdcGQSsPybEuVot8langchaoIHXgOsBrZLVvItruoX1IepbHpZORs59M4nL8BJkbyOlangchao");
		
		TestSignableRequest signRequest = new TestSignableRequest();
		signRequest.setBeginDate("20201201");
		signRequest.setEndDate("20201202");
		
		long timestamp = System.currentTimeMillis()/1000;
		SigningData data = new SigningData();
		data.setSecretkey(config.getSigningKey());
		data.setTimestamp(timestamp);
		data.setParams(signRequest);
		data.setDebug(true);
		String signkey = singer.sign(data);
		
		signRequest.setSignkey(signkey);
		signRequest.setTimestamp(timestamp);
		
		SigningCheckableData sdata = new SigningCheckableData();
		sdata.setDebug(true);
		sdata.setSigningConfig(config);
		sdata.setSignRequest(signRequest);
		singer.checkSign(sdata);
	}

	@Data
	public static class TestSignableRequest implements SignableRequest {
		@IgnoreField
//		@JsonProperty("Signkey")
		private String signkey;

//		@JsonProperty("Timestamp")
		@IgnoreField
		private Long timestamp;

		private String beginDate;
		private String endDate;
	}
}

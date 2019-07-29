package org.onetwo.boot.func.signature;
/**
 * @author weishao zeng
 * <br/>
 */

import org.junit.Test;
import org.onetwo.common.annotation.IgnoreField;
import org.onetwo.common.utils.DataSigner.SignableRequest;
import org.onetwo.common.utils.DataSigner.SigningConfig;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

public class SimpleDataSignerTest {
	
	@Test
	public void test() {
		SimpleDataSigner singer = new SimpleDataSigner();
		
		SigningConfig config = new SigningConfig();
		config.setMaxDelayTimeInSeconds(10);
		config.setSigningKey("testtest");
		
		SignableRequest signRequest = new TestSignableRequest();
		singer.checkSign(config, signRequest, null);
	}

	@Data
	public static class TestSignableRequest implements SignableRequest {
		@IgnoreField
		@JsonProperty("Signkey")
		private String signkey;

		@JsonProperty("Timestamp")
		@IgnoreField
		private Long timestamp;

		@JsonProperty("Id")
		private Long id;

		@JsonProperty("UserName")
		private String userName;

		@JsonProperty("Password")
		private String password;
	}
}

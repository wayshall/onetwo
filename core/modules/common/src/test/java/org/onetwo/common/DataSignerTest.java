package org.onetwo.common;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.date.NiceDate;
import org.onetwo.common.utils.DataSigner.BaseSignableRequest;
import org.onetwo.common.utils.DataSigner.DefaultDataSigner;
import org.onetwo.common.utils.DataSigner.SignableRequest;
import org.onetwo.common.utils.DataSigner.SigningCheckableData;
import org.onetwo.common.utils.DataSigner.SigningConfig;
import org.onetwo.common.utils.DataSigner.SigningData;
import org.onetwo.common.utils.DataSigner.SimpleSignableRequest;

import com.google.common.collect.Maps;

public class DataSignerTest {

    private SigningConfig signing;
    private DefaultDataSigner dataSinger = new DefaultDataSigner();
    
    @Before
    public void setup() {
    	signing = new SigningConfig();
    	signing.setMaxDelayTimeInSeconds(60);
    	signing.setSecretkey("just-test~");
    }
    
    @Test
    public void testSignMap() {
    	Map<String, Object> params = Maps.newHashMap();
    	params.put("username", "test");
    	params.put("password", "passwordtest");
    	params.put("validCode", "validCodetest");

    	long tsInSeconds = NiceDate.New().getTime().getTime()/1000;
    	SigningData data = new SigningData();
    	data.setParams(params);;
    	data.setTimestamp(tsInSeconds);
    	data.setSecretkey(signing.getSecretkey());
    	data.setDebug(true);
    	
    	String signkey = dataSinger.sign(data);
    	
    	System.out.println("check sign===========");
    	
    	SignableRequest signRequest = new SimpleSignableRequest(params, tsInSeconds, signkey);
    	
    	SigningCheckableData sdata = new SigningCheckableData();
		sdata.setDebug(true);
		sdata.setSigningConfig(signing);
		sdata.setSignRequest(signRequest);
		
    	dataSinger.checkSign(sdata);
    }

    @Test
    public void testSignBaseSignableRequest() {
    	DataRequest params = new DataRequest();
    	params.setUsername("test");
    	params.setPassword("passwordtest");
    	params.setValidCode("validCodetest");

    	long tsInSeconds = NiceDate.New().getTime().getTime()/1000;
    	SigningData data = new SigningData();
    	data.setParams(params);;
    	data.setTimestamp(tsInSeconds);
    	data.setSecretkey(signing.getSecretkey());
    	data.setDebug(true);
    	
    	String signkey = dataSinger.sign(data);
    	
    	System.out.println("check sign===========");
    	
    	SignableRequest signRequest = new SimpleSignableRequest(params, tsInSeconds, signkey);
    	
    	SigningCheckableData sdata = new SigningCheckableData();
		sdata.setDebug(true);
		sdata.setSigningConfig(signing);
		sdata.setSignRequest(signRequest);
		
    	dataSinger.checkSign(sdata);
    }
    
    static class DataRequest extends BaseSignableRequest {
    	String username;
    	String password;
    	String validCode;
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getValidCode() {
			return validCode;
		}
		public void setValidCode(String validCode) {
			this.validCode = validCode;
		}
    	
    }
}

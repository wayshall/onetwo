package org.onetwo.common.jackson;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.date.NiceDate;
import org.onetwo.common.jackson.datasign.JacksonDataSignerService;
import org.onetwo.common.utils.DataSigner.SignableRequest;
import org.onetwo.common.utils.DataSigner.SigningCheckableData;
import org.onetwo.common.utils.DataSigner.SigningConfig;
import org.onetwo.common.utils.DataSigner.SigningData;
import org.onetwo.common.utils.DataSigner.SimpleSignableRequest;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonDataSignerTest {


    private SigningConfig signing;
    private JacksonDataSignerService dataSinger = new JacksonDataSignerService();
    
    @Before
    public void setup() {
    	signing = new SigningConfig();
    	signing.setMaxDelayTimeInSeconds(60);
    	signing.setSecretkey("just-test~");
    }

    @Test
    public void testSignJsonObject() {
    	String json = "{username: \"test\", password: \"passwordtest\", validCode: \"validCodetest\"}";
    	ObjectNode params = JsonMapper.fromJsonString(json, ObjectNode.class);

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
    public void testSignArrayObject() {
    	String json = "["
    			+ "{username: \"user1\", password: \"passwordtest\", validCode: \"validCodetest\"},"
    			+ "{username: \"user2\", password: \"passwordtest\", validCode: \"validCodetest\"}"
    			+ "]";
    	ArrayNode params = JsonMapper.fromJsonString(json, ArrayNode.class);

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
}

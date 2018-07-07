package org.onetwo.ext.alimq;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.reflect.ReflectUtils;

/**
 * @author wayshall
 * <br/>
 */
public class JsonMessageSerializer implements MessageSerializer {
	public static final JsonMessageSerializer INSTANCE = new JsonMessageSerializer(false);
	public static final JsonMessageSerializer CHECKED_INSTANCE = new JsonMessageSerializer(true);
	
	public static final String PROP_BODY_TYPE = "PROP_BODY_TYPE";
	private JsonMapper jsonMapper = JsonMapper.defaultMapper();
	private boolean checkMessageBodyInstantiate;
	
	public JsonMessageSerializer(){
		//不使用enableDefaultTyping模式，携带了太多类型信息，对反序列化不友好
		/*ObjectMapper mapper = jsonMapper.getObjectMapper();
		mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);*/
	}
	
	public JsonMessageSerializer(boolean checkMessageBodyInstantiate) {
		super();
		this.checkMessageBodyInstantiate = checkMessageBodyInstantiate;
	}

	@Override
	public byte[] serialize(Object body, MessageDelegate messageDelegate) {
		if(checkMessageBodyInstantiate){
			//检查消息是否可以用反射实例化，保证反序列化时不会因为没有默认构造函数而出错
			ReflectUtils.newInstance(body.getClass());
		}
		messageDelegate.putUserProperties(PROP_BODY_TYPE, body.getClass().getName());
		return jsonMapper.toJsonBytes(body);
	}

	public void setCheckMessageBodyInstantiate(boolean checkMessageBodyInstantiate) {
		this.checkMessageBodyInstantiate = checkMessageBodyInstantiate;
	}

}

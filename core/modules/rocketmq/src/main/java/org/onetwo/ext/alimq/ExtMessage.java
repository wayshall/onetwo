package org.onetwo.ext.alimq;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageConst;
import org.onetwo.common.convert.Types;

/****
 * 用于兼容message类api的变动
 * @author way
 *
 */
@SuppressWarnings("serial")
public class ExtMessage extends Message {
    public static final String PROPERTY_DELAY_TIME_AT = "DELAY_TIME_AT";


    public void setKey(String keys) {
    	this.setKeys(keys);
    }

    public String getKey() {
    	return this.getKeys();
    }
    
    public String getTag() {
        return this.getTags();
    }

    public void setTag(String tags) {
        this.setTags(tags);
    }
    
	/***
	 * rocketmqy原生延迟消息
	 * @see {@link com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.Message#setDelayTimeLevel(int)}
	 * MessageConst.PROPERTY_DELAY_TIME_LEVEL
	 * 
	 * 默认支持18个level的延迟消息，这是通过broker端的messageDelayLevel配置项确定的，如下：
	 * messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
	 * 
	 * level == 0，消息为非延迟消息
1<=level<=maxLevel，消息延迟特定时间，例如level==1，延迟1s
level > maxLevel，则level== maxLevel，例如level==20，延迟2
	 * @param delayTimeLevel
	 */
	public void setDelayTimeLevel(Integer delayTimeLevel) {
		if (delayTimeLevel==null) {
			return ;
		}
//		putSystemProperties(MessageConst.PROPERTY_DELAY_TIME_LEVEL, delayTimeLevel.toString());
		putUserProperty(MessageConst.PROPERTY_DELAY_TIME_LEVEL, delayTimeLevel.toString());
	}
	
	public void setStartDeliverTime(Long StartDeliverTime) {
		putUserProperty(PROPERTY_DELAY_TIME_AT, StartDeliverTime.toString());
	}
	
	public Long getStartDeliverTime() {
		String startDeliverTimeStr = getUserProperty(PROPERTY_DELAY_TIME_AT);
		return Types.convertValue(startDeliverTimeStr, Long.class, null);
	}
	
	public void putUserProperties(String key, String value) {
		if (StringUtils.isBlank(value)) {
			return ;
		}
		super.putUserProperty(key, value);
	}
	
	public String getUserProperties(String key) {
		return this.getUserProperty(key);
	}
	
	public void setUserProperties(Properties userProperties) {
		if (userProperties==null || userProperties.isEmpty()) {
			return ;
		}
		userProperties.keySet().forEach(key -> {
			String keyStr = (String) key;
			if (StringUtils.isBlank(keyStr)) {
				return ;
			}
			String val = userProperties.getProperty(keyStr);
			this.putUserProperty(keyStr, val);
		});
	}
}

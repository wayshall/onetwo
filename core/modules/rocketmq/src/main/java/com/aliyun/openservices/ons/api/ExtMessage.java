package com.aliyun.openservices.ons.api;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageConst;

/****
 * putSystemProperties的访问级别是package……
 * @author way
 *
 */
@SuppressWarnings("serial")
public class ExtMessage extends Message {

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
		putSystemProperties(MessageConst.PROPERTY_DELAY_TIME_LEVEL, delayTimeLevel.toString());
	}
}

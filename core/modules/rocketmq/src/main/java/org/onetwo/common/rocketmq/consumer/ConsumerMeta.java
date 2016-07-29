package org.onetwo.common.rocketmq.consumer;

import java.util.Arrays;
import java.util.List;

import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;

public class ConsumerMeta {
	final private String groupName;
	final private String topic;
	final private List<String> tags;
	final private MessageModel messageModel;
	final private ConsumeFromWhere consumeFromWhere;
    
	public ConsumerMeta(String groupName, String topic, String... tags) {
		super();
		this.groupName = groupName;
		this.topic = topic;
		this.tags = Arrays.asList(tags);
		this.messageModel = MessageModel.CLUSTERING;
		this.consumeFromWhere = ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET;
	}

	public ConsumerMeta(String groupName, String topic, List<String> tags,
			MessageModel messageModel, ConsumeFromWhere consumeFromWhere) {
		super();
		this.groupName = groupName;
		this.topic = topic;
		this.tags = tags;
		this.messageModel = messageModel;
		this.consumeFromWhere = consumeFromWhere;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getTopic() {
		return topic;
	}

	public List<String> getTags() {
		return tags;
	}

	public MessageModel getMessageModel() {
		return messageModel;
	}

	public ConsumeFromWhere getConsumeFromWhere() {
		return consumeFromWhere;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((consumeFromWhere == null) ? 0 : consumeFromWhere.hashCode());
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result
				+ ((messageModel == null) ? 0 : messageModel.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		result = prime * result + ((topic == null) ? 0 : topic.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConsumerMeta other = (ConsumerMeta) obj;
		if (consumeFromWhere != other.consumeFromWhere)
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (messageModel != other.messageModel)
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		if (topic == null) {
			if (other.topic != null)
				return false;
		} else if (!topic.equals(other.topic))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConsumerMeta [groupName=" + groupName + ", topic=" + topic
				+ ", tags=" + tags + ", messageModel=" + messageModel
				+ ", consumeFromWhere=" + consumeFromWhere + "]";
	}
    
}

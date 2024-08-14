package org.onetwo.boot.mq.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.onetwo.dbm.jpa.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wayshall
 * <br/>
 */
@SuppressWarnings("serial")
@Entity
@Table(name="data_mq_send")
@Data
@EqualsAndHashCode(callSuper=false)
public class SendMessageEntity extends BaseEntity {
	
	@Id
	@Column(name="msgkey")
	private String key;
	private byte[] body;
	@Enumerated(EnumType.ORDINAL)
	private SendStates state;
	private String locker;
	private Date deliverAt;
	@Column(name="is_delay")
	private Boolean delay;
	
	public static enum SendStates {
		/***
		 * 未发送
		 */
		UNSEND,
		/***
		 * 已发送
		 */
		SENT,
		/****
		 * 半消息，已发送到broker暂存，未发送到消费者，基于rocketmq的事务消息
		 * 是一种特殊的消息类型，该状态的消息暂时不能被Consumer消费。
		 * 当一条事务消息被成功投递到Broker上，但是Broker并没有接收到Producer发出的二次确认时，该事务消息就处于"暂时不可被消费"状态，该状态的事务消息被称为半消息。
		 */
		HALF;
	}

}

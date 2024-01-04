package org.onetwo.boot.mq.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
		UNSEND,
		SENT;
	}

}

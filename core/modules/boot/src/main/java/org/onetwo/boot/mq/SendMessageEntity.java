package org.onetwo.boot.mq;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.onetwo.dbm.jpa.BaseEntity;

/**
 * @author wayshall
 * <br/>
 */
@SuppressWarnings("serial")
@Entity
@Table(name="data_mq_send")
@Data
@EqualsAndHashCode(callSuper=true)
public class SendMessageEntity extends BaseEntity {
	
	@Id
	@Column(name="msgkey")
	private String key;
	private byte[] body;
	@Enumerated(EnumType.ORDINAL)
	private SendStates state;
	private String locker;
	private Date deliverAt = new Date();
	
	public static enum SendStates {
		UNSEND,
		SENT;
	}

}

package org.onetwo.ext.ons.transaction;

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
@Table(name="data_rmq_send")
@Data
@EqualsAndHashCode(callSuper=true)
public class SendMessageEntity extends BaseEntity {
	
	@Id
	private String key;
	private byte[] body;
	@Enumerated(EnumType.ORDINAL)
	private SendStates state;
	
	public static enum SendStates {
		TO_SEND,
		SENT;
	}

}

package org.onetwo.ext.ons.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.onetwo.dbm.annotation.DbmIdGenerator;
import org.onetwo.dbm.id.SnowflakeGenerator;
import org.onetwo.dbm.jpa.BaseEntity;
import org.onetwo.dbm.mapping.DbmEnumIntMapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息消费记录表
 * @author wayshall
 * <br/>
 */
@SuppressWarnings("serial")
@Entity
@Table(name="data_mq_receive")
@Data
@EqualsAndHashCode(callSuper=true)
public class ReceiveMessageEntity extends BaseEntity {
	
	/***
	 * 自动生成id，消费端不能用msgkey作为唯一键，因为rmq不同的消费组可以接受同一个消息
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflake")
    @DbmIdGenerator(name = "snowflake", generatorClass = SnowflakeGenerator.class)
	Long id;
	private String msgkey;
	@Column(name="raw_msgid")
	private String rawMsgid;
	private String consumeGroup;
	@Enumerated(EnumType.ORDINAL)
	private ConsumeStates state;
	
	@AllArgsConstructor
	public static enum ConsumeStates implements DbmEnumIntMapping {
		/***
		 * 已消费
		 */
		CONSUMED(1);
		
		final private int value;
		@Override
		public int getMappingValue() {
			return value;
		}
	}

}

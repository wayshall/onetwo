package org.onetwo.ext.ons.transaction;

import java.time.LocalDateTime;
import java.util.List;

import org.onetwo.boot.mq.SendMessageEntity;
import org.onetwo.boot.mq.SendMessageEntity.SendStates;
import org.onetwo.boot.mq.SendMessageRepository;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.dialet.DBDialect.LockInfo;
import org.onetwo.ext.alimq.SimpleMessage;
import org.onetwo.ext.ons.ONSProperties;
import org.onetwo.ext.ons.ONSProperties.SendTaskProps;
import org.onetwo.ext.ons.ONSUtils.SendMessageFlags;
import org.onetwo.ext.ons.producer.ProducerService;
import org.onetwo.ext.ons.producer.SendMessageContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import com.aliyun.openservices.ons.api.Message;

/**
 * @author wayshall
 * <br/>
 */
@Transactional
public class CompensationSendMessageTask implements InitializingBean {
	
	protected Logger log = JFishLoggerFactory.getLogger(CompensationSendMessageTask.class);
	
	@Autowired
	private BaseEntityManager baseEntityManager;
	@Autowired
	private ProducerService producerService;
	@Autowired
	private MessageBodyStoreSerializer messageBodyStoreSerializer;
	@Autowired
	private ONSProperties onsProperties;
	@Autowired
	private SendMessageRepository<SendMessageContext> sendMessageRepository;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/***
	 * 定时器运行时间，默认60秒一次，最好比要检查的未发的消息稍长几秒
	 * @author wayshall
	 */
//	@Scheduled(cron="${"+ONSProperties.TRANSACTIONAL_TASK_CRON_KEY+":0 0/1 * * * *}")
	@Scheduled(fixedRateString="${"+ONSProperties.TRANSACTIONAL_SEND_TASK_FIXED_RATE_STRING_KEY+":60000}")
	public void scheduleCheckSendMessage(){
		log.info("start to check unsend message...");
		doCheckSendMessage();
		log.info("finish check unsend message...");
	}

	/***
	 * 扫描指定时间前的100条未发送消息，使用悲观锁
	 * 可扩展为其它类型锁
	 * @author wayshall
	 */
	protected void doCheckSendMessage(){
		SendTaskProps taskProps = this.onsProperties.getTransactional().getSendTask();
		long deleteBeforeAt = taskProps.getDeleteBeforeAtInSeconds();
		LocalDateTime createAt = LocalDateTime.now().minusSeconds(deleteBeforeAt);//默认当前时间减去50秒
		List<SendMessageEntity> messages = Querys.from(baseEntityManager, SendMessageEntity.class)
												.where()
													.field("state").equalTo(SendStates.TO_SEND.ordinal())
													.field("createAt").lessThan(createAt)
												.end()
												.asc("createAt")
												.limit(0, taskProps.getDeleteCountPerTask())
												.lock(LockInfo.write())
												.toQuery()
												.list();
		if(LangUtils.isEmpty(messages)){
			if(log.isInfoEnabled()){
				log.info("no unsend mesage found from database");
			}
			return ;
		}
		for(SendMessageEntity message : messages){
			Message rmqMessage = messageBodyStoreSerializer.deserialize(message.getBody());
			SimpleMessage simple = SimpleMessage.builder().body(rmqMessage).build();
			producerService.sendMessage(simple, SendMessageFlags.DisableDatabaseTransactional);
			sendMessageRepository.updateToSent(message);
			if(log.isInfoEnabled()){
				log.info("resend message and remove from database, id: {}, msgId: {}", message.getKey(), rmqMessage.getMsgID());
			}
		}
	}
}

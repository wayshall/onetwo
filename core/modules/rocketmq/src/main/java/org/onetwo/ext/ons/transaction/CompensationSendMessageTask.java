package org.onetwo.ext.ons.transaction;

import java.time.LocalDateTime;
import java.util.List;

import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.dialet.DBDialect.LockInfo;
import org.onetwo.ext.alimq.SimpleMessage;
import org.onetwo.ext.ons.ONSProperties;
import org.onetwo.ext.ons.ONSUtils.SendMessageFlags;
import org.onetwo.ext.ons.producer.ProducerService;
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

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Scheduled(cron="${"+ONSProperties.TRANSACTIONAL_TASK_CRON_KEY+":0 0/1 * * * *}")
	public void scheduleCheckSendMessage(){
		log.info("start to check unsend message...");
		doCheckSendMessage();
		log.info("finish check unsend message...");
	}
	
	/***
	 * 扫描一分钟前的100条未发送消息，使用悲观锁
	 * 可扩展为其它类型锁
	 * @author wayshall
	 */
	protected void doCheckSendMessage(){
		LocalDateTime createAt = LocalDateTime.now().minusMinutes(1);
		List<SendMessageEntity> messages = Querys.from(baseEntityManager, SendMessageEntity.class)
												.where()
													.field("createAt").lessThan(createAt)
												.end()
												.limit(0, 100)
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
			baseEntityManager.remove(message);
			if(log.isInfoEnabled()){
				log.info("resend message and remove from database, id: {}, msgId: {}", message.getKey(), rmqMessage.getMsgID());
			}
		}
	}
}

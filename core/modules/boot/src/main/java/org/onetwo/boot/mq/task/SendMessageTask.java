package org.onetwo.boot.mq.task;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.joda.time.LocalDateTime;
import org.onetwo.boot.module.redis.RedisLockRunner;
import org.onetwo.boot.mq.MQProperties;
import org.onetwo.boot.mq.MQProperties.SendTaskProps;
import org.onetwo.boot.mq.ProducerService;
import org.onetwo.boot.mq.SendMessageFlags;
import org.onetwo.boot.mq.entity.SendMessageEntity;
import org.onetwo.boot.mq.entity.SendMessageEntity.SendStates;
import org.onetwo.boot.mq.repository.SendMessageRepository;
import org.onetwo.boot.mq.serializer.MessageBodyStoreSerializer;
import org.onetwo.common.db.generator.meta.TableMeta;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangOps;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.core.spi.DbmSessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.Assert;

import lombok.Data;

/**
 * 每次发送消息的时候触发扫描（消息订阅（消息会成倍增加）？spring event（无法跨应用）？）
 * 同时通过定时器x分钟触发，避免若没有消息发送无法触发扫描，导致延迟消息无法发送
 * 
 * 每次扫描当前时间+x分钟，向前扫描x分钟是为了把将要发送的延迟消息放到延迟队列触发
 * @author wayshall
 * <br/>
 */
//@Transactional
public class SendMessageTask implements InitializingBean {
	public static final String LOCK_KEY = "mq:SendMessageTask";
	
	protected Logger log = JFishLoggerFactory.getLogger(getClass());
	
	@Autowired(required=false)
	private RedisLockRegistry redisLockRegistry;
	
	@Autowired
	private MessageBodyStoreSerializer messageBodyStoreSerializer;
	@Autowired
	private SendMessageRepository sendMessageRepository;
	@Autowired
	private ProducerService<?, ?> producerService;
	
	private boolean useReidsLock;
	private String redisLockTimeout;
	
	@Autowired
	private MQProperties mqProperties;
	@Autowired
	private DbmSessionFactory sessionFactory;
	private DelayQueue<DelayedMessage> delayedMessageQueue = new DelayQueue<>();
	@Autowired
	private SendMessageTask _this;

	@Override
	public void afterPropertiesSet() throws Exception {
		if(useReidsLock){
			Assert.notNull(redisLockRegistry, "redisLockRegistry not found!");
		}
		if (mqProperties.getTransactional().getSendTask().isCheckMessageTable()) {
			checkMessageTable();
		}
		_this.startDelayedTask();
	}
	
	private void checkMessageTable() {
		TableMeta tableMeta = sessionFactory.getDatabaseMetaDialet().getTableMeta("data_mq_send");
		if (!tableMeta.hasColumn("is_delay")) {
			throw new BaseException("the column[is_delay] not found on table[data_mq_send], please upgrade the lasted table schemal");
		}
	}

	public void setUseReidsLock(boolean useReidsLock) {
		this.useReidsLock = useReidsLock;
	}

	/***
	 * 定时器运行时间，默认30秒一次
	 * @author wayshall
	 */
//	@Scheduled(cron="${"+ONSProperties.TRANSACTIONAL_TASK_CRON_KEY+":0 0/1 * * * *}")
	@Scheduled(fixedDelayString="${"+MQProperties.TRANSACTIONAL_SEND_TASK_CONFIG_KEY+":30000}", initialDelay=30000)
	public void scheduleCheckSendMessage(){
		SendTaskProps taskProps = this.mqProperties.getTransactional().getSendTask();
		int scanForwardTime = (int)LangOps.timeToSeconds(taskProps.getScanForwardTime(), 60*30);
		doCheckSendMessage(taskProps.getSendCountPerTask(), scanForwardTime);
	}

	/***
	 * 扫描指定时间前的100条未发送消息，使用悲观锁
	 * 可扩展为其它类型锁
	 * @author wayshall
	 */
	protected void doCheckSendMessage(int sendCountPerTask, int scanForwardTime){
		log.info("start to check unsend message...");
		if(useReidsLock){
			getRedisLockRunner().tryLock(()->{
				findAndProcessUnsendMessage(sendCountPerTask, scanForwardTime);
				return null;
			});
		}else{
			this.findAndProcessUnsendMessage(sendCountPerTask, scanForwardTime);
		}
		log.info("finish check unsend message...");
	}
	
	protected void findAndProcessUnsendMessage(int sendCountPerTask, int scanForwardTime){
		//刚插入到db的消息会在事务提交后发送，所以这里扫描的时间要减去一定的时间，避免把刚插入的消息也发送了
//		LocalDateTime createAt = LocalDateTime.now().minusSeconds(ignoreCreateAtRecently);
		LocalDateTime scanAt = LocalDateTime.now().plusSeconds(scanForwardTime);
		
		//先上锁，避免发送的时候尝试锁住数据库
		String locker = mqProperties.getTransactional().getSendTask().getLocker();
		int lockCount = this.sendMessageRepository.lockSendMessage(locker, scanAt.toDate(), SendStates.UNSEND);
		if(log.isInfoEnabled()){
			log.info("lock [{}] mesage from database", lockCount);
		}
		
		//再扫描发送
		List<SendMessageEntity> messages = this.sendMessageRepository.findLockerMessage(locker, scanAt.toDate(), SendStates.UNSEND, sendCountPerTask);
		if(LangUtils.isEmpty(messages)){
			if(log.isInfoEnabled()){
				log.info("no unsend mesage found from database");
			}
			return ;
		}
		if(log.isInfoEnabled()){
			log.info("find [{}] mesage from database to be sending", messages.size());
		}
		for(SendMessageEntity message : messages){
			if (message.isDelay()) {
				// handle delay message
				DelayedMessage delayed = new DelayedMessage(message);
				this.delayedMessageQueue.offer(delayed);
			} else {
				sendMessage(message);
			}
		}
	}

	@Async
	public void startDelayedTask() throws InterruptedException {
		while(true) {
			DelayedMessage delayed = delayedMessageQueue.take();
			sendMessage(delayed.getMessage());
		}
	}
	
	public void sendMessage(SendMessageEntity message){
		Serializable mqMessage = messageBodyStoreSerializer.deserialize(message.getBody());
		producerService.send(mqMessage, SendMessageFlags.DisableDatabaseTransactional);
		sendMessageRepository.updateToSent(message);
		if(log.isInfoEnabled()){
			log.info("send message and remove from database, key: {}", message.getKey());
		}
	}

	private RedisLockRunner getRedisLockRunner(){
		RedisLockRunner redisLockRunner = RedisLockRunner.createLocker(redisLockRegistry, LOCK_KEY, redisLockTimeout);
		return redisLockRunner;
	}

	public void setRedisLockTimeout(String redisLockTimeout) {
		this.redisLockTimeout = redisLockTimeout;
	}

	@Data
	public static class DelayedMessage implements Delayed {
		SendMessageEntity message;

		public DelayedMessage(SendMessageEntity message) {
			super();
			this.message = message;
		}

		@Override
		public int compareTo(Delayed o) {
			DelayedMessage other = (DelayedMessage)o;
			long deliverAt1 = this.getMessage().getDeliverAt().getTime();
			long deliverAt2 = other.getMessage().getDeliverAt().getTime();
			if (deliverAt1 > deliverAt2) {
				// 放在队列尾部
				return 1;
			}
			if (deliverAt1 < deliverAt2) {
				// 放在头部
				return -1;
			}
			return 0;
		}

		@Override
		public long getDelay(TimeUnit unit) {
			long diffMilllis = message.getDeliverAt().getTime() - System.currentTimeMillis();
			return unit.convert(diffMilllis, TimeUnit.MILLISECONDS);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DelayedMessage other = (DelayedMessage) obj;
			if (message == null) {
				if (other.message != null)
					return false;
			} else if (!message.getKey().equals(other.message.getKey()))
				return false;
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((message == null) ? 0 : message.getKey().hashCode());
			return result;
		}
		
	}
	
}

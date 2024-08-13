package org.onetwo.ext.transactionmessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.onetwo.boot.mq.entity.SendMessageEntity;
import org.onetwo.boot.mq.entity.SendMessageEntity.SendStates;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.ds.DatasourceFactoryBean;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.mapping.DbmConfig;
import org.onetwo.dbm.mapping.DefaultDbmConfig;
import org.onetwo.dbm.spring.EnableDbm;
import org.onetwo.ext.ons.annotation.EnableONSClient;
import org.onetwo.ext.ons.annotation.ONSProducer;
import org.onetwo.ext.rocketmq.transaction.GenericRmqTransactionListener;
import org.onetwo.ext.transactionmessage.TransactionMessageProducerTest.TransactionMessageProducerTestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wayshall
 * <br/>
 */
@ContextConfiguration(classes=TransactionMessageProducerTestContext.class)
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@Transactional
public class TransactionMessageProducerTest {
	public static final String TOPIC = "${topic}";
	public static final String PRODUER_ID = "TransactionMessageProducerTestProducer";
	public static final String USER_SAVE = "${tags.orderPay}";

	@Autowired
	UserBizTestService userBizTestService;
	@Autowired
	private BaseEntityManager baseEntityManager;
	
	@Test
	public void test1SendMessage(){
		baseEntityManager.removeAll(UserTestEntity.class);
		baseEntityManager.removeAll(SendMessageEntity.class);
		int messageCount = baseEntityManager.countRecord(SendMessageEntity.class).intValue();
		assertThat(messageCount).isEqualTo(0);
		userBizTestService.saveUserAndSendMessage("test1SendMessage");
		List<SendMessageEntity> msgList = baseEntityManager.findAll(SendMessageEntity.class);
		assertThat(msgList.size()).isEqualTo(1);
		SendMessageEntity msg = LangUtils.getFirst(msgList);
		assertThat(msg.getState()).isEqualTo(SendStates.HALF);

		int userCount = baseEntityManager.countRecord(UserTestEntity.class).intValue();
		assertThat(userCount).isEqualTo(1);
		
		LangUtils.waitIf(6, () -> {
			SendMessageEntity newMsg = baseEntityManager.findById(SendMessageEntity.class, msg.getKey());
			return newMsg.getState()==SendStates.SENT;
		});
	}
	
	@Test
	public void test2sendMessageWithException(){
		baseEntityManager.removeAll(UserTestEntity.class);
		baseEntityManager.removeAll(SendMessageEntity.class);
		int messageCount = baseEntityManager.countRecord(SendMessageEntity.class).intValue();
		assertThat(messageCount).isEqualTo(0);
		
		try {
			userBizTestService.saveErrorAfterSaveLocalMessageLog("test2sendMessageWithException");
			fail();
		} catch (Exception e) {
			assertThat(e).isInstanceOf(ServiceException.class);
		}
		
		List<SendMessageEntity> msgList = baseEntityManager.findAll(SendMessageEntity.class);
		assertThat(msgList.size()).isEqualTo(0);
		int userCount = baseEntityManager.countRecord(UserTestEntity.class).intValue();
		assertThat(userCount).isEqualTo(0);
//		LangUtils.CONSOLE.exitIf("test");
	}
	

	@Test
	public void test3saveLocalMessageError(){
		baseEntityManager.removeAll(UserTestEntity.class);
		baseEntityManager.removeAll(SendMessageEntity.class);
		int messageCount = baseEntityManager.countRecord(SendMessageEntity.class).intValue();
		assertThat(messageCount).isEqualTo(0);
		
		try {
			userBizTestService.saveLocalMessageError("test3saveLocalMessageError");
			fail();
		} catch (Exception e) {
			e.printStackTrace();
			assertThat(e).isInstanceOf(UnexpectedRollbackException.class);
//			ServiceException cause = (ServiceException)LangUtils.getFinalCauseException(e);
//			assertThat(cause.getMessage()).isEqualTo("save local message log error");
		}
		
		List<SendMessageEntity> msgList = baseEntityManager.findAll(SendMessageEntity.class);
		assertThat(msgList.size()).isEqualTo(0);
		int userCount = baseEntityManager.countRecord(UserTestEntity.class).intValue();
		assertThat(userCount).isEqualTo(0);
//		LangUtils.CONSOLE.exitIf("test");
	}
	
	@EnableONSClient(
			producers= {
					@ONSProducer(producerId=PRODUER_ID, transactional = true)
			}
	)
	@Configuration
	@PropertySource("classpath:transaction-message-producer.properties")
	@EnableDbm
	@EnableTransactionManagement
	public static class TransactionMessageProducerTestContext {
		static AtomicBoolean throwWhenExecuteSendMessage = new AtomicBoolean(false);
		@Bean
		public DatasourceFactoryBean dataSource(){
			DatasourceFactoryBean ds = new DatasourceFactoryBean();
			ds.setImplementClass(org.apache.tomcat.jdbc.pool.DataSource.class);
			ds.setPrefix("jdbc.");
			return ds;
		}
		@Bean
		public DbmConfig dbmConfig(){
			DefaultDbmConfig dbmConfig = new DefaultDbmConfig();
			dbmConfig.setAutoProxySessionTransaction(true);
			return dbmConfig;
		}
		@Bean
		public PropertyPlaceholderConfigurer jfishPropertyPlaceholder(){
			return SpringUtils.newApplicationConf("transaction-message-producer.properties");
		}

		@Bean
		public TransactionListener rmqTransactionListener(){
			TestGenericRmqTransactionListener listener = new TestGenericRmqTransactionListener();
			return listener;
		}
		
		@Bean
		public UserBizTestService userBizTestService() {
			UserBizTestService u = new UserBizTestService();
			return u;
		}
	}
	
	public static class TestGenericRmqTransactionListener extends GenericRmqTransactionListener {
		@Override
		@Transactional(propagation = Propagation.MANDATORY) // 如果当前存在事务，就加入当前事务，如果当前不存在事务，就抛出异常
		public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
			LocalTransactionState state = super.executeLocalTransaction(msg, arg);
			if (TransactionMessageProducerTestContext.throwWhenExecuteSendMessage.get()) {
				throw new ServiceException("save local message log error");
			}
			return state;
		}
	}
	
}

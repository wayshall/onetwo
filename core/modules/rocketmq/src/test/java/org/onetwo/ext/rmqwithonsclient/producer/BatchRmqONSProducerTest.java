package org.onetwo.ext.rmqwithonsclient.producer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.onetwo.boot.mq.entity.SendMessageEntity;
import org.onetwo.boot.mq.repository.SendMessageRepository;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.ds.DatasourceFactoryBean;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.dbm.mapping.DbmConfig;
import org.onetwo.dbm.mapping.DefaultDbmConfig;
import org.onetwo.dbm.spring.EnableDbm;
import org.onetwo.ext.ons.annotation.EnableONSClient;
import org.onetwo.ext.ons.annotation.ONSProducer;
import org.onetwo.ext.rmqwithonsclient.producer.BatchRmqONSProducerTest.BatchProducerTestContext;
import org.onetwo.ext.rmqwithonsclient.producer.RmqONSProducerTest.TestDatabaseTransactionMessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author wayshall
 * <br/>
 */
@ContextConfiguration(classes=BatchProducerTestContext.class)
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@Transactional
public class BatchRmqONSProducerTest {
	public static final String TOPIC = "${topic}";
	public static final String PRODUER_ID = "${producerId}";
	public static final String ORDER_PAY = "${tags.orderPay}";
	public static final String ORDER_CANCEL = "${tags.orderCancel}";

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	BatchDataBaseProducerServiceImpl batchDataBaseProducerService;
	@Test
	public void testBatch1(){
		baseEntityManager.removeAll(SendMessageEntity.class);
		batchDataBaseProducerService.sendMessage();
		int messageCount = baseEntityManager.countRecord(SendMessageEntity.class).intValue();
		assertThat(messageCount).isEqualTo(BatchDataBaseProducerServiceImpl.batchCount);
	}

	@Test
	public void testBatch2Rollback(){
		baseEntityManager.removeAll(SendMessageEntity.class);
		assertThatExceptionOfType(ServiceException.class).isThrownBy(()->{
			batchDataBaseProducerService.sendMessageWithException();
		})
		.withMessage("发消息后出错！");
		int messageCount = baseEntityManager.countRecord(SendMessageEntity.class).intValue();
		assertThat(messageCount).isEqualTo(0);
	}
	
	@EnableONSClient(producers=@ONSProducer(producerId=PRODUER_ID))
	@Configuration
	@PropertySource("classpath:rmqwithonsclient-test.properties")
	@EnableDbm
	@EnableTransactionManagement
	public static class BatchProducerTestContext {
		@Bean
		public DatasourceFactoryBean datasourceFactoryBean(){
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
			return SpringUtils.newApplicationConf("rmqwithonsclient-test.properties");
		}
		@Bean
		public DataBaseProducerServiceImpl dataBaseProducerService(){
			return new DataBaseProducerServiceImpl();
		}
		@Bean
		public BatchDataBaseProducerServiceImpl batchDataBaseProducerService(){
			return new BatchDataBaseProducerServiceImpl();
		}

		@Bean
		public TestDatabaseTransactionMessageInterceptor databaseTransactionMessageInterceptor(SendMessageRepository sendMessageRepository){
			TestDatabaseTransactionMessageInterceptor interceptor = new TestDatabaseTransactionMessageInterceptor();
			interceptor.setSendMessageRepository(sendMessageRepository);
			return interceptor;
		}
	}
}

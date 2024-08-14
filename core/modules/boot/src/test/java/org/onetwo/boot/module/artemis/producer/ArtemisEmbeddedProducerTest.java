package org.onetwo.boot.module.artemis.producer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onetwo.boot.module.activemq.ActivemqProperties;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureRule;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wayshall
 * <br/>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=ArtemisProducerApplication.class,
//				properties={"spring.activemq.in-memory=false", "spring.activemq.pool.enabled=false", "spring.activemq.broker-url=tcp://localhost:61616"},
				properties={
//							"spring.activemq.in-memory=true", "spring.activemq.pool.enabled=false", 
							"debug=true",
							"spring.main.allow-bean-definition-overriding=true",
							"spring.artemis.mode=embedded", 
							"spring.artemis.host=localhost", 
							"spring.artemis.port=61616", 
							"spring.artemis.user=admin", 
							"spring.artemis.password=admin", 
							"spring.artemis.embedded.serverId=myEmbeddedBroker", 
							"spring.artemis.embedded.persistent=true", 
							"spring.artemis.embedded.queues=sample.queue", 
							"spring.artemis.embedded.topics=sample.topic", 
							"spring.artemis.embedded.dataDirectory=/Users/way/mydev/servers/artemis-embedded", 
//							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.embedded.enabled=true", org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.jdbcStore.enabled=true"
//							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".jms.converter=jackson2",
							ActivemqProperties.CONVERTER_KEY+ "=jackson2",
							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".mq.transactional.enabled=true",
							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".mq.transactional.sendTask.enabled=true",
							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".mq.transactional.deleteTask.enabled=true",
							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.enabled=true",
							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.type=artemis",
							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.topic.subscriptionDurable=true",
							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.topic.subscriptionShared=true"
							}
)
public class ArtemisEmbeddedProducerTest {
	@Rule
	public OutputCaptureRule outputCapture = new OutputCaptureRule();

	@Autowired
	private ArtemisProducerTestBean producer;

	@Test
	public void sendSimpleMessage() throws InterruptedException {
//		LangOps.ntimesRun(10, (i)->this.producer.send2Topic("Test message " + i));
		Thread.sleep(1000L);
//		assertThat(this.outputCapture.toString().contains("Test message")).isTrue();
		

//		LangOps.ntimesRun(10, (i)->this.producer.send("Test message " + i));
//		String receiveMessage = this.producer.sendReplyQueue("Test message");
//		Thread.sleep(1000L);
//		assertThat(receiveMessage).isEqualTo("I got it!");
		
		LangUtils.CONSOLE.executeIf("send", cmd -> {
			producer.send2Topic("test message");
		})
		.exitIf("exit");
	}

}

package org.onetwo.boot.module.activemq.producer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wayshall
 * <br/>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=ActivemqProducerApplication.class,
//				properties={"spring.activemq.in-memory=false", "spring.activemq.pool.enabled=false", "spring.activemq.broker-url=tcp://localhost:61616"},
				properties={
//							"spring.activemq.in-memory=true", "spring.activemq.pool.enabled=false", 
							"spring.activemq.in-memory=false", "spring.activemq.pool.enabled=false", "spring.activemq.broker-url=tcp://localhost:61616", 
//							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.embedded.enabled=true", org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.jdbcStore.enabled=true"
							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".mq.transactional.enabled=true",
							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".mq.transactional.sendTask.enabled=true",
							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".mq.transactional.deleteTask.enabled=true",
							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.enabled=true"
							}
)
public class ActivemqProducerTest {
	@Rule
	public OutputCapture outputCapture = new OutputCapture();

	@Autowired
	private ProducerTestBean producer;

	@Test
	public void sendSimpleMessage() throws InterruptedException {
//		LangOps.ntimesRun(10, (i)->this.producer.send2Topic("Test message " + i));
		Thread.sleep(1000L);
//		assertThat(this.outputCapture.toString().contains("Test message")).isTrue();
		

//		LangOps.ntimesRun(10, (i)->this.producer.send("Test message " + i));
//		String receiveMessage = this.producer.sendReplyQueue("Test message");
//		Thread.sleep(1000L);
//		assertThat(receiveMessage).isEqualTo("I got it!");
		
		LangUtils.CONSOLE.exitIf("exit");
	}

}

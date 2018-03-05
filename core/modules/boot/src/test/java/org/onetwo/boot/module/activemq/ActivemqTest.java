package org.onetwo.boot.module.activemq;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wayshall
 * <br/>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=ActivemqTestApplication.class,
//				properties={"spring.activemq.in-memory=false", "spring.activemq.pool.enabled=false", "spring.activemq.broker-url=tcp://localhost:61616"},
				properties={"spring.activemq.in-memory=true", "spring.activemq.pool.enabled=false"}
)
public class ActivemqTest {
	@Rule
	public OutputCapture outputCapture = new OutputCapture();

	@Autowired
	private ProducerTestBean producer;

	@Test
	public void sendSimpleMessage() throws InterruptedException {
		this.producer.send("Test message");
		Thread.sleep(1000L);
		assertThat(this.outputCapture.toString().contains("Test message")).isTrue();
		

		String receiveMessage = this.producer.sendReplyQueue("Test message");
		Thread.sleep(1000L);
		assertThat(receiveMessage).isEqualTo("I got it!");
	}

}

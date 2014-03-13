package org.onetwo.plugins.activemq.producer;

import javax.annotation.Resource;

import org.junit.Test;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.list.NoIndexIt;
import org.onetwo.plugins.activemq.MailInfo;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(loader=ActivemqProducerLoader.class )
public class ActivemqProducerManyMessageTest extends AbstractJUnit4SpringContextTests {
	
	@Resource
	private ProducerServiceImpl producerServiceImpl;
	
	@Test
	public void testSendMessage(){
		JFishList.intList(0, 100).each(new NoIndexIt<Integer>() {

			@Override
			protected void doIt(Integer element) throws Exception {
				MailInfo mainInfo = MailInfo.create("pistols@qq.com", "pistols1@qq.com", "pistols2@qq.com").subject("title"+element).content("测试内容"+element);
				producerServiceImpl.sendMessage("myqueue", mainInfo);
			}
			
		});
		
	}

}

package org.onetwo.plugins.email;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(value="classpath:/email-test.xml")
public class EmailPluginTest extends AbstractJUnit4SpringContextTests {

	@Resource
	private JavaMailService javaMailServiceImpl;
	
	@Test
	public void testSimpleTextMail() throws MessagingException{
		this.javaMailServiceImpl.send(MailInfo.create("gjdzd@qyscard.com", "pistols@qq.com").subject("test title").content("test content"));
	}
}

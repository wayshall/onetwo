package org.onetwo.plugins.email;

import javax.mail.MessagingException;

public interface JavaMailService {

	void send(MailInfo mailInfo) throws MessagingException;

}
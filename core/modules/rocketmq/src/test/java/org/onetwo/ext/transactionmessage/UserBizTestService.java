package org.onetwo.ext.transactionmessage;

import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.ext.ons.producer.TransactionProducerService;
import org.onetwo.ext.transactionmessage.TransactionMessageProducerTest.TransactionMessageProducerTestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

/***
 * for UserTestEntity
 * @author way
 *
 */
public class UserBizTestService {
	
	@Autowired
	private BaseEntityManager baseEntityManager;
	@Autowired
	@Qualifier(TransactionMessageProducerTest.PRODUER_ID)
	private TransactionProducerService producerService;
	
	
	@Transactional
	public void saveUserAndSendMessage(String userName) {
		UserTestEntity user = createUser(userName);
		baseEntityManager.save(user);
		producerService.sendMessageInTransaction(TransactionMessageProducerTest.TOPIC, TransactionMessageProducerTest.USER_SAVE, user);
	}
	

	@Transactional
	public void saveErrorAfterSaveLocalMessageLog(String userName) {
		UserTestEntity user = createUser(userName);
		producerService.sendMessageInTransaction(TransactionMessageProducerTest.TOPIC, TransactionMessageProducerTest.USER_SAVE, user);
		baseEntityManager.save(user);
		throw new ServiceException("save user error");
	}


	@Transactional
	public void saveLocalMessageError(String userName) {
		UserTestEntity user = createUser(userName);
		TransactionMessageProducerTestContext.throwWhenExecuteSendMessage.set(true);
		producerService.sendMessageInTransaction(TransactionMessageProducerTest.TOPIC, TransactionMessageProducerTest.USER_SAVE, user);
		baseEntityManager.save(user);
	}
	
	UserTestEntity createUser(String userName) {
		UserTestEntity user = new UserTestEntity();
		user.setUserName(userName);
		return user;
	}

}

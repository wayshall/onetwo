package org.onetwo.ext.transactionmessage;

import org.apache.rocketmq.client.producer.TransactionListener;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.ext.ons.producer.RmqProducerService;
import org.onetwo.ext.transactionmessage.TransactionMessageProducerTest.TestGenericRmqTransactionListener;
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
	private RmqProducerService producerService;
	
	
	@Transactional
	public void saveUserAndSendMessage(String userName) {
		UserTestEntity user = createUser(userName);
		baseEntityManager.save(user);
		producerService.sendMessage(TransactionMessageProducerTest.TOPIC, TransactionMessageProducerTest.USER_SAVE, user);
	}
	

	@Transactional
	public void saveErrorAfterSaveLocalMessageLog(String userName) {
		UserTestEntity user = createUser(userName);
		producerService.sendMessage(TransactionMessageProducerTest.TOPIC, TransactionMessageProducerTest.USER_SAVE, user);
		baseEntityManager.save(user);
		throw new ServiceException("save user error");
	}


	@Transactional
	public void saveLocalMessageError(String userName) {
		UserTestEntity user = createUser(userName);
		TransactionMessageProducerTestContext.throwWhenExecuteSendMessage.set(true);
		producerService.sendMessage(TransactionMessageProducerTest.TOPIC, TransactionMessageProducerTest.USER_SAVE, user);
		baseEntityManager.save(user);
	}
	
	UserTestEntity createUser(String userName) {
		UserTestEntity user = new UserTestEntity();
		user.setUserName(userName);
		return user;
	}

}

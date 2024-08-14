package org.onetwo.ext.rocketmq.transaction;

import java.net.UnknownHostException;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.onetwo.boot.mq.exception.MQException;

public class DefaultMQProducerImplExt extends DefaultMQProducerImpl {
	

    public DefaultMQProducerImplExt(final DefaultMQProducer defaultMQProducer) {
        super(defaultMQProducer, null);
    }

    public DefaultMQProducerImplExt(final DefaultMQProducer defaultMQProducer, RPCHook rpcHook) {
    	super(defaultMQProducer, rpcHook);
    }
    
    /****
     * rocketmq执行事务状态检查 {@link #sendMessageInTransaction()}，
     * 在回调transactionListener#executeLocalTransaction时catch掉了异常，
     * 这会导致当在业务事务上下文中执行executeLocalTransaction的插入事务日志失败时，整个业务的事务不会回滚。
     * 结果就是，插入事务日志失败了，业务却成功了，回查事务状态（transactionListener#checkLocalTransaction）会一直失败（unknow），最后回滚了消息，从而导致事务不一致
     * 
     * 这里修改为，当本地事务日志插入失败时，最终抛出异常，让整个业务事务跟着回滚
     * 
     * https://github.com/apache/rocketmq/blob/8167608334c901bd85482904ed203a26ac8950e4/broker/src/main/java/org/apache/rocketmq/broker/processor/EndTransactionProcessor.java#L49
     */
    public void endTransaction(
        final Message msg,
        final SendResult sendResult,
        final LocalTransactionState localTransactionState,
        final Throwable localException) throws RemotingException, MQBrokerException, InterruptedException, UnknownHostException {
    	super.endTransaction(msg, sendResult, localTransactionState, localException);
    	if (localException!=null) {
    		throw new MQException("executeLocalTransactionBranch exception: " + localException.getMessage(), localException);
    	}
    }

}

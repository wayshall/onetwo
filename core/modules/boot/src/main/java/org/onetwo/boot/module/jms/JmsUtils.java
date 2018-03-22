package org.onetwo.boot.module.jms;
/**
 * @author wayshall
 * <br/>
 */
final public class JmsUtils {
	
	/****
	 * 如果不显示配置，则使用JmsAnnotationDrivenConfiguration#jmsListenerContainerFactory定义的ContainerFactory
	 * 详见：JmsListenerEndpointRegistrar#resolveContainerFactory查找策略
	 * @author wayshall
	 *
	 */
	public static class ContainerFactorys {
		public static final String TOPIC = "topicListenerContainerFactory";
//		public static final String QUEUE = "queueListenerContainerFactory";
		public static final String QUEUE = "jmsListenerContainerFactory";
		private ContainerFactorys(){}
	}
	
	private JmsUtils(){
	}

}

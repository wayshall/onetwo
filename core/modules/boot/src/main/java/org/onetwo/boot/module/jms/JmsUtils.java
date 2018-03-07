package org.onetwo.boot.module.jms;
/**
 * @author wayshall
 * <br/>
 */
final public class JmsUtils {
	
	public static class ContainerFactorys {
		public static final String TOPIC = "topicListenerContainerFactory";
		public static final String QUEUE = "queueListenerContainerFactory";
		private ContainerFactorys(){}
	}
	
	private JmsUtils(){
	}

}

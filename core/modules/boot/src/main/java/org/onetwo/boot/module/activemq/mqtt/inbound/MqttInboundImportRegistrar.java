package org.onetwo.boot.module.activemq.mqtt.inbound;

import org.onetwo.boot.module.activemq.mqtt.ActiveMQTTConfiguration;
import org.onetwo.boot.module.activemq.mqtt.ActiveMQTTProperties.InBoundClientProps;
import org.onetwo.boot.module.activemq.mqtt.ActiveMQTTProperties.MessageConverters;
import org.onetwo.boot.module.activemq.mqtt.MqttPahoMessageDrivenChannel;
import org.onetwo.boot.module.activemq.mqtt.annotation.EnableMqttInbound;
import org.onetwo.boot.module.activemq.mqtt.annotation.MqttInboundHandler;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.context.AbstractImportRegistrar;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author weishao zeng
 * <br/>
 */
public class MqttInboundImportRegistrar extends AbstractImportRegistrar<EnableMqttInbound, MqttInboundHandler> {

	@SuppressWarnings("unchecked")
	@Override
	protected BeanDefinitionBuilder createComponentFactoryBeanBuilder(AnnotationMetadata annotationMetadata, AnnotationAttributes attributes) {
		String className = annotationMetadata.getClassName();
		Class<? extends MqttPahoMessageDrivenChannel> drivenChannelClass = (Class<? extends MqttPahoMessageDrivenChannel>)ReflectUtils.loadClass(className);
		
		BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(drivenChannelClass);

		InBoundClientProps clientConfig = new InBoundClientProps();
		clientConfig.setClientId(resolveAttributeAsString(attributes, "clientId"));
		String channelName = resolveAttributeAsString(attributes, "channelName");
		clientConfig.setChannelName(channelName);
		clientConfig.setConverter((MessageConverters)attributes.get("converter"));
		clientConfig.setCompletionTimeout((int)attributes.get("completionTimeout"));
		clientConfig.setQos((int[])attributes.get("qos"));
		
		String[] topics = resolveAttributeAsStringArray(attributes, "topics");
		clientConfig.setTopics(topics);
		
		definition.addConstructorArgValue(clientConfig);
		definition.addConstructorArgReference(ActiveMQTTConfiguration.BEAN_MQTT_PAHO_CLIENT_FACTORY);
//		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
		
		return definition;
	}
	
//	@Override
//	protected BeanDefinitionBuilder createComponentFactoryBeanBuilder(AnnotationMetadata annotationMetadata, AnnotationAttributes attributes) {
//		String className = annotationMetadata.getClassName();
//		BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(MqttInboundFactoryBean.class);
//
//		InBoundClientProps clientConfig = new InBoundClientProps();
//		clientConfig.setClientId(resolveAttributeAsString(attributes, "clientId"));
//		String channelName = resolveAttributeAsString(attributes, "channelName");
//		clientConfig.setChannelName(channelName);
//		clientConfig.setConverter((MessageConverters)attributes.get("converter"));
//		clientConfig.setCompletionTimeout((int)attributes.get("completionTimeout"));
//		clientConfig.setQos((int[])attributes.get("qos"));
//		
//		String[] topics = resolveAttributeAsStringArray(attributes, "topics");
//		clientConfig.setTopics(topics);
//		
//		definition.addPropertyValue("clientConfig", clientConfig);
//		definition.addPropertyValue("drivenChannelClass", className);
//		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
//		
//		return definition;
//	}

}

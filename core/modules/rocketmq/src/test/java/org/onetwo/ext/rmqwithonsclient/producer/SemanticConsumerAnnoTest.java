package org.onetwo.ext.rmqwithonsclient.producer;
/**
 * @author weishao zeng
 * <br/>
 */

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.ons.annotation.ONSSubscribe;
import org.onetwo.ext.ons.annotation.ONSSubscribe.IdempotentType;
import org.onetwo.ext.rmqwithonsclient.producer.SemanticONSConsumerTest.SemanticConsumer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

public class SemanticConsumerAnnoTest {

	@Test
	public void testSemantic() {
		Method m = ReflectUtils.findMethod(SemanticConsumer.class, "test", ConsumContext.class);
		ONSSubscribe meta = AnnotationUtils.findAnnotation(m, ONSSubscribe.class);
		assertThat(meta).isNotNull();
		assertThat(meta.tags()).contains("JuestTestTag");
		assertThat(meta.consumerId()).isEmpty();
		assertThat(meta.idempotent()).isEqualTo(IdempotentType.DATABASE);
		

		meta = AnnotatedElementUtils.findMergedAnnotation(m, ONSSubscribe.class);
		assertThat(meta.consumerId()).isEqualTo("SemanticConsumer2");
		assertThat(meta.idempotent()).isEqualTo(IdempotentType.DATABASE);
	}
}

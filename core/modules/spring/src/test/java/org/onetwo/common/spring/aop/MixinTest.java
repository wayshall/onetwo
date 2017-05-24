package org.onetwo.common.spring.aop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.Test;
import org.onetwo.common.spring.aop.Freezable;
import org.onetwo.common.spring.aop.Mixin;
import org.onetwo.common.spring.aop.Mixins;

/**
 * @author wayshall
 * <br/>
 */
public class MixinTest {

	class SimpleObject {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
	@Test
	public void testMixin(){
		SimpleObject obj = new SimpleObject();
		
		final SimpleObject fobj = Mixins.mixin(obj, Freezable.class);
		((Freezable)fobj).freeze();
		
		assertThatExceptionOfType(IllegalStateException.class)
				.isThrownBy(()->{
					fobj.setName("test");
				})
				.withMessage("locked");
		

		obj = Mixins.mixins(obj, Human.class, Bird.class);
		String talkWord = ((Human)obj).talk();
		assertThat(talkWord).isEqualTo("hello world");

		String fly = ((Bird)obj).fly();
		assertThat(fly).isEqualTo("fly high");
	}

	@Mixin(HumanImpl.class)
	static interface Human {
		String talk();
	}
	
	public static class HumanImpl implements Human {

		@Override
		public String talk() {
			return "hello world";
		}
		
	}

	@Mixin(BirdImpl.class)
	static interface Bird {
		String fly();
	}
	
	public static class BirdImpl implements Bird {

		@Override
		public String fly() {
			return "fly high";
		}
		
	}

}

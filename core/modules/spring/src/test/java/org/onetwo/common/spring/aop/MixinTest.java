package org.onetwo.common.spring.aop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.Test;

/**
 * @author wayshall
 * <br/>
 */
public class MixinTest {
	
	public static interface ParentInterface {
		String test();
	}
	
	/***
	 * 没有添加额外方法的子接口
	 * @author wayshall
	 *
	 */
	public static interface SubInterface extends ParentInterface {
	}
	public static class ParentInterfaceImpl implements ParentInterface {

		@Override
		public String test() {
			return getClass().getSimpleName();
		}
		
	}
	@Test
	public void testSameParentMixin(){
		ParentInterfaceImpl impl = new ParentInterfaceImpl();
		SubInterface sub = Proxys.delegateInterface(SubInterface.class, impl);
		String res = sub.test();
		assertThat(res).isEqualTo(ParentInterfaceImpl.class.getSimpleName());
	}
	
	@Test
	public void testMixin(){
		SimpleObject obj = new SimpleObject();
		
		//相当于把FreezableImpl植入到SimpleObject
		final SimpleObject fobj = Mixins.of(obj, Freezable.class);
		((Freezable)fobj).freeze();
		
		assertThatExceptionOfType(IllegalStateException.class)
				.isThrownBy(()->{
					fobj.setName("test");
				})
				.withMessage("locked");
		

		obj = Mixins.of(obj, Human.class, Bird.class);
		String talkWord = ((Human)obj).talk();
		assertThat(talkWord).isEqualTo("hello world");

		String fly = ((Bird)obj).fly();
		assertThat(fly).isEqualTo("fly high");
	}

	class SimpleObject {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
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

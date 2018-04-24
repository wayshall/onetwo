package org.onetwo.boot.module.redis;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wayshall
 * <br/>
 */
public class OnceTokenValidatorTest extends RedisBaseTest {
	
	@Autowired
	private OnceTokenValidator onceTokenValidator;
	
	@Test
	public void test(){
		String key = "addOrder";
		String value = "709394";
		this.onceTokenValidator.generate(key, ()->value);
		
		//value不正确
		Assertions.assertThatExceptionOfType(ServiceException.class)
					.isThrownBy(()->{
						this.onceTokenValidator.checkAndRun(key, "error code", ()->{
							Assert.fail("will not run...");
						});
					})
					.withMessage(RedisErrors.TOKEN_INVALID.getErrorMessage());

		//只能使用一次，再次使用，value正确也不行
		Assertions.assertThatExceptionOfType(ServiceException.class)
					.isThrownBy(()->{
						this.onceTokenValidator.checkAndRun(key, value, ()->{
							Assert.fail("will not run...");
						});
					})
					.withMessage(RedisErrors.TOKEN_INVALID.getErrorMessage());

		this.onceTokenValidator.generate(key, ()->value);
		this.onceTokenValidator.checkAndRun(key, value, ()->{
			System.out.println("run...");
		});
	}

}

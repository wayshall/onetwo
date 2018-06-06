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
public class TokenValidatorTest extends RedisBaseTest {
	
	@Autowired
	private TokenValidator tokenValidator;
	

	@Test
	public void testCheck(){
		String key = "addOrder";
		String value = "709394";
		this.tokenValidator.save(key, ()->value);
		
		//value不正确
		Assertions.assertThatExceptionOfType(ServiceException.class)
					.isThrownBy(()->{
						this.tokenValidator.check(key, "error code", ()->{
							Assert.fail("will not run...");
						});
					})
					.withMessage(TokenValidatorErrors.TOKEN_INVALID.getErrorMessage());

		this.tokenValidator.check(key, value, ()->{
			System.out.println("run...");
		});
	}
	
	@Test
	public void testCheckOnlyOnce(){
		String key = "addOrder";
		String value = "709394";
		this.tokenValidator.save(key, ()->value);
		
		//value不正确
		Assertions.assertThatExceptionOfType(ServiceException.class)
					.isThrownBy(()->{
						this.tokenValidator.checkOnlyOnce(key, "error code", ()->{
							Assert.fail("will not run...");
						});
					})
					.withMessage(TokenValidatorErrors.TOKEN_INVALID.getErrorMessage());

		//只能使用一次，再次使用，value正确也不行
		Assertions.assertThatExceptionOfType(ServiceException.class)
					.isThrownBy(()->{
						this.tokenValidator.checkOnlyOnce(key, value, ()->{
							Assert.fail("will not run...");
						});
					})
					.withMessage(TokenValidatorErrors.TOKEN_INVALID.getErrorMessage());

		this.tokenValidator.save(key, ()->value);
		this.tokenValidator.checkOnlyOnce(key, value, ()->{
			System.out.println("run...");
		});
	}

}

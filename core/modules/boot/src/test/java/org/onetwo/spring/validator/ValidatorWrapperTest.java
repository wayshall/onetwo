package org.onetwo.spring.validator;


import javax.validation.constraints.NotNull;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.spring.validator.ValidationBindingResult;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.onetwo.common.spring.validator.annotation.AnyOneNotBlank;
import org.onetwo.common.spring.validator.annotation.AnyOneNotBlanks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(classes=ValidatorTestContext.class)
public class ValidatorWrapperTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	private ValidatorWrapper validatorWrapper;
	
	@Test
	public void test(){
		ParamForValidatorTest param = new ParamForValidatorTest();
		ValidationBindingResult br = validatorWrapper.validate(param);
		Assert.assertEquals(4, br.getErrorCount());
		String msg = br.getErrorMessagesAsString();
		Assert.assertTrue(msg.contains("手机和用户名称不能都为空！"));
		Assert.assertTrue(msg.contains("手机和用户id不能都为空！"));
		
		param.setMobile("");
		param.setUserName("userName");
		br = validatorWrapper.validate(param);
		msg = br.getErrorMessagesAsString();
		Assert.assertTrue(msg.contains("手机和用户id不能都为空！"));

		param.setUserName(null);
		param.setMobile("1333333333");
		br = validatorWrapper.validate(param);
		msg = br.getErrorMessagesAsString();
		System.out.println("br:"+msg);
		Assert.assertFalse(msg.contains("手机和用户名称不能都为空！"));
		Assert.assertFalse(msg.contains("手机和用户id不能都为空！"));
	}
	
	@AnyOneNotBlanks({
		@AnyOneNotBlank(value={"mobile", "userId"}, message="手机和用户id不能都为空！"),
		@AnyOneNotBlank(value={"userName", "mobile"}, message="手机和用户名称不能都为空！")
	})
	static class ParamForValidatorTest {
		
		@NotNull
		private String mobile;
		private Long userId;

		@NotNull
		private String userName;
		
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public Long getUserId() {
			return userId;
		}
		public void setUserId(Long userId) {
			this.userId = userId;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		
	}

}

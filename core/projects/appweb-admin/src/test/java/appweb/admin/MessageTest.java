package appweb.admin;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.exception.SystemErrorCode.LoginErrorCode;
import org.onetwo.common.spring.web.mvc.CodeMessager;

public class MessageTest extends AppBaseTest {
	
	@Resource
	private CodeMessager codeMessager;
	@Test
	public void testMessage(){
		String msg = this.codeMessager.getMessage("typeMismatch");
		System.out.println("msg: " + msg);
		Assert.assertEquals("请输入正确的类型", msg);
		
		msg = this.codeMessager.getMessage(LoginErrorCode.USER_NOT_FOUND);
		System.out.println("msg: " + msg);
		Assert.assertEquals("找不到此用户", msg);
	}

}

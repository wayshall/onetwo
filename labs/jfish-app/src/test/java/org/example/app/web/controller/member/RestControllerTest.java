package org.example.app.web.controller.member;

import org.example.app.AppBaseTest;
import org.example.app.model.member.entity.UserEntity;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.fish.exception.JFishBusinessException;
import org.onetwo.plugins.rest.ErrorCode;
import org.onetwo.plugins.rest.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

/******
 * {@link RestController}测试类
 * @author wayshall
 *
 */
public class RestControllerTest extends AppBaseTest {
	
	@Autowired
	private RestController restController;
	
	@Autowired
	private HandlerAdapter ha;
	
	@Test
	public void testGetUser() throws Exception{
		restController = new RestController();
		ModelAndView mv = null;
		
		try {
			mv = ha.handle(request("/rest/getUser"), response(), restController);
			Assert.fail("must failed");//注意这一句
		} catch (Exception e) {
			Assert.assertTrue(JFishBusinessException.class.isInstance(e));
			Assert.assertEquals(ErrorCode.COM_ER_VALIDATION_REQUIRED, ((JFishBusinessException)e).getCode());
		}
		
		mv = ha.handle(request("/rest/getUser").param("id", "1"), response(), restController);

		Assert.assertEquals(Long.valueOf(1), mv.getModel().get("id"));
		Assert.assertEquals(Long.valueOf(1), ((UserEntity)mv.getModel().get("user")).getId());
	}
	

	@Test
	public void testGetUserForRestResult() throws Exception{
		restController = new RestController();
		ModelAndView mv = null;
		RestResult<UserEntity> restResult = null;

		mv = ha.handle(request("/rest/getUserForRestResult"), response(), restController);
		restResult = (RestResult<UserEntity>)mv.getModel().get("restResult");
		Assert.assertNotNull(restResult);
		Assert.assertTrue(restResult.isFailed());
		Assert.assertNull(restResult.getData());
		Assert.assertEquals(ErrorCode.COM_ER_VALIDATION_REQUIRED, restResult.getError_code());
		
		
		mv = ha.handle(request("/rest/getUserForRestResult").param("id", "1"), response(), restController);
		restResult = (RestResult<UserEntity>)mv.getModel().get("restResult");
		Assert.assertNotNull(restResult);
		Assert.assertTrue(restResult.isSucceed());
		Assert.assertNotNull(restResult.getData());
		Assert.assertEquals(Long.valueOf(1), restResult.getData().getId());
	}
	
	/****
	 * 不通过请求，直接实例化controller测试
	 * @throws Exception
	 */
	@Test
	public void testGetUserForRestResultDirect() throws Exception{
		restController = new RestController();
		RestResult<UserEntity> restResult = null;

		restResult = restController.getUserForRestResult(null);
		Assert.assertNotNull(restResult);
		Assert.assertTrue(restResult.isFailed());
		Assert.assertNull(restResult.getData());
		Assert.assertEquals(ErrorCode.COM_ER_VALIDATION_REQUIRED, restResult.getError_code());
		
		
		restResult = restController.getUserForRestResult(1L);
		Assert.assertNotNull(restResult);
		Assert.assertTrue(restResult.isSucceed());
		Assert.assertNotNull(restResult.getData());
		Assert.assertEquals(Long.valueOf(1), restResult.getData().getId());
	}
	
	@Test
	public void testGetUserForObject() throws Exception {
		restController = new RestController();
		ModelAndView mv = null;

		try {
			mv = ha.handle(request("/rest/getUserForObject"), response(), restController);
			Assert.fail("must failed");
		} catch (Exception e) {
			Assert.assertTrue(JFishBusinessException.class.isInstance(e));
			Assert.assertEquals(ErrorCode.COM_ER_VALIDATION_REQUIRED, ((JFishBusinessException)e).getCode());
		}

		mv = ha.handle(request("/rest/getUserForObject").param("id", "1"), response(), restController);
		
		UserEntity userEntity = (UserEntity)mv.getModel().get("userEntity");
		Assert.assertNotNull(userEntity);
		Assert.assertEquals(Long.valueOf(1), userEntity.getId());
	}

}

package org.example.app.web.controller.member;

import org.example.app.model.member.entity.UserEntity;
import org.onetwo.common.spring.web.BaseController;
import org.onetwo.plugins.rest.ErrorCode;
import org.onetwo.plugins.rest.RestResult;
import org.onetwo.plugins.rest.exception.JFishBusinessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/*****
 * 
 * @author wayshall
 *
 */
@Controller
@RequestMapping("/rest")
public class RestController extends BaseController<Object>{

	@RequestMapping(value="/getUser", method=RequestMethod.GET)
	public ModelAndView getUser(Long id) throws JFishBusinessException {
		if(id==null){
			throw JFishBusinessException.create(ErrorCode.COM_ER_VALIDATION_REQUIRED, "id");
		}
//		this.validateAndThrow(user);
		UserEntity user = new UserEntity();
		user.setId(id);
		
		return model("id", id, "user", user);
	}
	
	@RequestMapping(value="/getUserForRestResult", method=RequestMethod.GET)
	public RestResult<UserEntity> getUserForRestResult(Long id) {
		RestResult<UserEntity> rs = new RestResult<UserEntity>();
		if(id==null){
			rs.setError_code(ErrorCode.COM_ER_VALIDATION_REQUIRED, "id");
			return rs;
		}
//		this.validateAndThrow(user);
		try {
			UserEntity user = null;
//			user = new UserEntity();
			user.setId(id.longValue());
			rs.setData(user);
		} catch (Exception e) {
			rs.setError_code(ErrorCode.UN_KNOWN);
		}
		
		return rs;
	}
	
	@RequestMapping(value="/getUserForObject", method=RequestMethod.GET)
	public UserEntity getUserForObject(Long id) throws JFishBusinessException {
		if(id==null){
			throw JFishBusinessException.create(ErrorCode.COM_ER_VALIDATION_REQUIRED, "id");
		}
//		this.validateAndThrow(user);
		UserEntity user = new UserEntity();
		user.setId(id.longValue());
		
		return user;
	}
}

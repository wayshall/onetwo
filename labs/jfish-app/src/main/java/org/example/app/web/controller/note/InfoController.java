package org.example.app.web.controller.note;

import org.example.app.model.note.vo.UserParams;
import org.onetwo.plugins.rest.RestResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @class InfoController
 * 用户信息管理类
 * @author wayshall
 * 
 * 
 */
@RequestMapping("/inner")
@Controller
public class InfoController {

	/***
	 * @method save
	 * 
	 * 根据传入保存会员信息，如果id非0，先获取会员实体，把非空的字段赋值保存。
	 * 
	 * @httpUrl get - /inner/member_info_save
	 * 
	 * @param userInfo - UserParams -true - {@link UserParams}用户信息对象
	 * @param userId - Long -false - 用户id
	 * @return RestResult
	 * user_id - long
	 * 
	 * 
	 * @throws 
	 * biz_er_logincode_repeat - 账户重复
	 * biz_er_mobile_repeat - 手机重复 <br/>
	 * biz_er_email_repeat - 邮箱重复 <br/>
	 * 
	 */
	@RequestMapping(value={"/member_info_save"}, method={RequestMethod.POST})
	public RestResult<Long> save(UserParams userInfo, Long userId){
		return null;
	}
}

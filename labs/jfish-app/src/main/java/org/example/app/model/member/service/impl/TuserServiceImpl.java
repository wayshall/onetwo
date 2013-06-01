package org.example.app.model.member.service.impl;

import org.example.app.model.member.entity.UserEntity;
import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.exception.LoginException;
import org.onetwo.common.fish.JFishCrudServiceImpl;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.map.BaseMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

/*****
 * 假如不要dao，service可以直接继承JFishCrudServiceImpl
 * 以获得更多方便查询的接口
 * @author wayshall
 *
 */
@Service
public class TuserServiceImpl extends JFishCrudServiceImpl<UserEntity, Long> {

	@RequestMapping("/tservice/save")
	@Transactional()
	public UserEntity save(UserEntity user){
		super.save(user);
		System.out.println("userId:" + user.getId());
		return user;
	}

	@Transactional(readOnly=true)
	public Page<UserEntity> findPageByName(Page<UserEntity> page, String userName){
		return this.findPage(page, "userName:like", userName);
	}
	
	@Transactional(readOnly=true)
	public UserEntity findById(long id) throws BusinessException{
		UserEntity user = super.findById(id);
		if(user==null)
			throw new BusinessException("找不到用户：" + id);
		return user;
	}
	
	@Transactional(readOnly=true)
	public UserEntity login(String userName, String password){
		UserEntity user = this.findUnique("userName", userName);
		if(user==null || !user.getPassword().equals(password))
			throw new LoginException("登录错误，用户或者密码不匹配！");
		return user;
	}

	public Object fetchData() {
		return LangUtils.asMap("用户管理", "/member/tuser");
	}

	public Object fetchDataByMap(BaseMap map) {
		return LangUtils.asMap("用户管理"+(map==null?"map":map.getString("name")), "/member/tuser");
	}

	public Object fetchDataByParams(String name, int index) {
		return LangUtils.asMap(name+index, "/member/tuser");
	}
	
	
}

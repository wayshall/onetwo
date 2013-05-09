package org.onetwo.plugins.jdoc.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.onetwo.common.cache.Cacheable;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.jdoc.UserParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 用户信息管理类
 * @author wayshall
 * 
 * 
 */
@RequestMapping(value="/inner", method={RequestMethod.GET,RequestMethod.POST})
@Controller()
@Cacheable(expire=111)
public class InfoController {
	
	private final static URL SMSSENDWEBSERVICEBEANSERVICE_WSDL_LOCATION;
	private final static Logger logger = Logger
			.getLogger(org.onetwo.plugins.jdoc.test.InfoController.class
					.getName());
	static {
		int i = 1;
		for (int j = 0; j < i; j++) {
			System.out.println("test");
		}
		URL url = null;
		try {
			url = new URL("");
		} catch (MalformedURLException e) {
			logger.warning(e.getMessage());
		}
		SMSSENDWEBSERVICEBEANSERVICE_WSDL_LOCATION = url;
	}
	
	public InfoController(){
	}
	
	/***
	 * 
	 * 根据传入保存会员信息，如果id非0，先获取会员实体，把非空的字段赋值保存。
	 * 
	 * @httpUrl post - /inner/member_info_save.json
	 * 
	 * @param userInfo -true - {@link UserParams}用户信息对象
	 * @param userId -false - 用户id
	 * @return 返回结果
	 * 
	 * 
	 * 
	 * @throws
		biz_er_logincode_repeat	- 账号重复
		@throws biz_er_mobile_repeat - 手机重复
		@throws biz_er_email_repeat	- 邮箱重复
	 * 
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	@RequestMapping(value="/member_info_save", method=RequestMethod.POST)
	public Object save(UserParams userInfo, Long userId) throws ServiceException {
		if(true){
			System.out.println("test");
		}
		String[] array = LangUtils.newList("aa", " bb ").toArray(new String[2]);
		int number = 111;
		for (int i = 0; i < array.length; i++) {
			System.out.println(number+"array:" + array[i]);
		}
		return null;
	}
	
	/**/
	/*
	 * test
	 */
	/**aa*/
	/***
	 * bb
	 * @param userInfo
	 * @param userId
	 * @return
	 */
	@RequestMapping(value={"/member_info_save2"}, method={RequestMethod.POST})
	public Object save2(UserParams userInfo, Long userId){
		if(true){
			System.out.println("test");
		}
		String[] array = LangUtils.newList("aa", " bb ").toArray(new String[2]);
		int number = 111;
		for (int i = 0; i < array.length; i++) {
			System.out.println(number+"array:" + array[i]);
		}
		return null;
	}
}


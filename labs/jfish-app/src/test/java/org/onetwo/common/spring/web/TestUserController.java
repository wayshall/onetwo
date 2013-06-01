package org.onetwo.common.spring.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.example.app.model.member.entity.UserEntity;
import org.example.app.model.member.service.impl.TuserServiceImpl;
import org.example.app.web.LoginUserInfo;
import org.onetwo.common.db.ExtQueryUtils;
import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.utils.Page;
import org.onetwo.common.web.s2.security.config.annotation.Authentic;
import org.onetwo.common.web.utils.WebContextUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**********
 * 这是restful风格的url注射
 * 要注意很多不同的action其实对应着同一个url
 * 区别只是请求的方法不同
 * 遵循资源+动作的请求方式
 *
 * /user           => index()  
 * /user/new       => _new()  
 * /user/{id}      => show()  
 * /user/{id}/edit         => edit()  
 * /user   POST        => create()  
 * /user/{id}  PUT => update()  
 * /user/{id}  DELETE  => delete()  
 * /user   DELETE      => deleteBatch() 
 * 
 * @author wayshall
 */
@Controller
@RequestMapping("/member/test-user/")//这个路径最好按照controller后面的包名+controller的短名字映射
public class TestUserController extends BaseController {
	
	@Resource
	private TuserServiceImpl userService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(Model model, UserEntity user, Page<UserEntity> page){
		this.userService.findPage(page, ExtQueryUtils.field2Map(user));
		model.addAttribute("page", page);
		return listView();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") long id, Model model) throws BusinessException{
		model.addAttribute("user", this.userService.findById(id));
		return showView();
	}
	
	@RequestMapping(value="new", method=RequestMethod.GET)
	public ModelAndView _new(LoginUserInfo loginUser, Model model) throws BusinessException{
		if(loginUser==null)
			throw new BusinessException("没有登录，不能创建用户！");
		model.addAttribute("user", new UserEntity());
		return newView();
	}
	
	@Authentic
	@RequestMapping(value="/{id}/edit", method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable("id") Long id, Model model){
		UserEntity user = this.userService.findById(id);
		model.addAttribute("user", user);
//		return "/member/user/user-edit";
		return editView();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView create(UserEntity user, RedirectAttributes redirectAttributes) throws BusinessException{
		this.userService.save(user);
		this.addCreateMessage(redirectAttributes);
		return listAction();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ModelAndView update(UserEntity user, RedirectAttributes redirectAttributes){
		this.userService.save(user);
		this.addUpdateMessage(redirectAttributes);
		return showAction(user.getId());
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") Long id){
		return listAction();
	}
	
	@RequestMapping(method=RequestMethod.DELETE)
	public ModelAndView deleteBatch(@RequestParam(value="ids")long[] ids, RedirectAttributes redirectAttributes){
		for(long id : ids){
			this.userService.removeById(id);
		}
		this.addDeleteMessage(redirectAttributes);
		return listAction();
	}
	
	/****
	 * 可根据url后缀不同返回不同格式的数据
	 * @return
	 */
	@RequestMapping(value="auser", method=RequestMethod.GET)
	public UserEntity auser(){
		Page<UserEntity> page = Page.create();
		this.userService.findPage(page);
		UserEntity user = page.getResult().get(0);
		return user;
	}
	
	@RequestMapping(value="login", method=RequestMethod.POST)
	public ModelAndView login(@RequestParam(value="userName")String userName, @RequestParam(value="password")String password, HttpServletRequest request){
		UserEntity user = userService.login(userName, password);
		LoginUserInfo userDetail = new LoginUserInfo();
		userDetail.setUserName(user.getUserName());
		userDetail.setUserId(user.getId());
		WebContextUtils.setUserDetail(request.getSession(), userDetail);
		return view("login");
	}
	
	public static void main(String[] args){
	}
}

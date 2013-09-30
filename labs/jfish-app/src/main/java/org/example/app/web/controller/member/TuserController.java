package org.example.app.web.controller.member;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.example.app.model.member.entity.RoleEntity;
import org.example.app.model.member.entity.UserEntity;
import org.example.app.model.member.entity.UserEntity.PasswordOnly;
import org.example.app.model.member.service.impl.TestCacheServiceImpl;
import org.example.app.model.member.service.impl.TuserServiceImpl;
import org.example.app.web.LoginUserInfo;
import org.onetwo.common.db.ExtQueryUtils;
import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.exception.NotLoginException;
import org.onetwo.common.spring.web.BaseController;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.s2.security.config.annotation.Authentic;
import org.onetwo.common.web.utils.WebContextUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**********
 * 这是restful风格的url注射
 * 要注意很多不同的action其实对应着同一个url
 * 区别只是请求的方法不同
 * 遵循资源+动作的请求方式
 * 
 * /user			GET		=> index()	列表页面
 * /user/new		GET		=> _new()  	新建页面
 * /user/{id}		GET		=> show()  	单条显示页面
 * /user/{id}/edit	GET		=> edit()	编辑页面
 * /user			POST	=> create()	保存新增实体
 * /user/{id}		PUT		=> update()	更新新增实体
 * /user/{id}		DELETE	=> delete()	删除单条
 * /user			DELETE	=> deleteBatch()	批量删除
 * 
 * @author way
 */
@Controller
@RequestMapping("/member/tuser")//这个路径最好按照controller后面的包名+controller的短名字映射
public class TuserController extends BaseController {
	
	@Resource
	private TuserServiceImpl userService;
	
	@Resource
	private TestCacheServiceImpl testCacheServiceImpl;
	
//	@Resource
//	private Validator validator;
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
//		binder.setValidator(validator);
//		binder.registerCustomEditor(LoginUserInfo.class, new JFishWebArgumentResolver());
	}
	
	/*********
	 * 
	 * @param model
	 * @param user
	 * @param page
	 * @param export
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(Model model, UserEntity user, Page<UserEntity> page, @RequestParam(value="export", required=false) boolean export){
		this.userService.findPage(page, ExtQueryUtils.field2Map(user)); 
		if(export){
			return exportExcel("index", "测试", "page", page);
		}else{
			return indexView(page);
		}
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") long id, Model model, RoleEntity role) throws BusinessException{
		UserEntity user =  this.userService.findById(id);
//		model.addAttribute("user", user);
		user.setPassword(DateUtil.nowString());
//		return json(user);
		return showView("user", user);
	}
	
	@RequestMapping(value="new", method=RequestMethod.GET)
	public ModelAndView _new(LoginUserInfo loginUser, @ModelAttribute("user") UserEntity user, BindingResult bindResult) throws BusinessException{
		this.validate(user, bindResult, PasswordOnly.class);
		if(bindResult.hasErrors()){ 
			System.out.println("errors: " + bindResult.getAllErrors());
		}
		return newView("user", new UserEntity());
	}
	
	@Authentic(redirect="")
	@RequestMapping(value="/{id}/edit", method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable("id") Long id, Model model){
		UserEntity user = this.userService.findById(id);
		return editView("user", user);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView create(@Valid UserEntity user, BindingResult bind, RedirectAttributes redirectAttributes, Model model) throws BusinessException{
		if(bind.hasErrors()){
			return newView("user", user);
		}
			
		this.userService.save(user);
		this.addCreateMessage(redirectAttributes);
		return listAction();
	}
	
	/*********
	 * spring bug：@Valid验证对象的后面必须紧跟着BindingResult参数，3.2修复
	 * @param user
	 * @param binding
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ModelAndView update(@ModelAttribute("user") @Valid UserEntity user, BindingResult binding, RedirectAttributes redirectAttributes){
		if(binding.hasErrors()){
			return editView();
		}
		this.userService.save(user);
		this.addUpdateMessage(redirectAttributes);
		return showAction(user.getId());
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes){
		UserDetail user = JFishWebUtils.currentLoginUser();
		if(user==null)
			throw new NotLoginException();
		this.userService.removeById(id);
		this.addDeleteMessage(redirectAttributes);
		return indexAction();
	}
	
	@RequestMapping(method=RequestMethod.DELETE)
	public ModelAndView deleteBatch(@RequestParam(value="ids")long[] ids, RedirectAttributes redirectAttributes){
		for(long id : ids){
			this.userService.removeById(id);
		}
		this.addDeleteMessage(redirectAttributes);
		return indexAction();
	}
	
	/****
	 * 可根据url后缀不同返回不同格式的数据
	 * @return
	 */
	@RequestMapping(value="auser", method=RequestMethod.GET)
	public ModelAndView auser(){
		Page<UserEntity> page = Page.create();
		this.userService.findPage(page);
		UserEntity user = page.getResult().get(0);
		return view("auser", user);
	}
	
	@ResponseBody
	@RequestMapping(value="auser2", method=RequestMethod.GET)
	public Map auser2(){
		Page<UserEntity> page = Page.create();
		this.userService.findPage(page);
		UserEntity user = page.getResult().get(0);

		Map dataMap = LangUtils.asMap("test", "testValue", "test2", user);
		return dataMap;
	}
	
	@RequestMapping(value="login", method=RequestMethod.POST)
	public ModelAndView login(@RequestParam(value="userName")String userName, @RequestParam(value="password")String password, HttpServletRequest request, HttpServletResponse response){
		UserEntity user = userService.login(userName, password);
		LoginUserInfo userDetail = new LoginUserInfo();
		userDetail.setUserName(user.getUserName());
		userDetail.setUserId(user.getId());
		userDetail.setToken(user.getId().toString());
		
		WebContextUtils.setUserDetail(request.getSession(), userDetail);
		WebContextUtils.setCookieToken(response, user.getId().toString());
		
		return view("login");
	}
	
	public static void main(String[] args){
	}
}

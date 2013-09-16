package org.onetwo.common.spring.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;

import org.onetwo.apache.io.IOUtils;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.validator.ValidationBindingResult;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.onetwo.common.spring.web.mvc.CodeMessager;
import org.onetwo.common.spring.web.mvc.view.JFishExcelView;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.utils.WebContextUtils;
import org.slf4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

abstract public class AbstractBaseController {
//	public static final String SINGLE_MODEL_FLAG_KEY = "__SINGLE_MODEL_FLAG_KEY__";
	
	public static final String DEFAULT_CONTENT_TYPE = "application/download; charset=GBK";
	
	public static final String REDIRECT = "redirect:";
	public static final String MESSAGE = "message";
	public static final String MESSAGE_TYPE = "messageType";
	public static final String MESSAGE_TYPE_ERROR = "error";
	public static final String MESSAGE_TYPE_SUCCESS = "success";
	
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	@Resource
	private CodeMessager codeMessager;
	
	protected AbstractBaseController(){
	}
	
	public String getMessage(String code, Object...args){
		return codeMessager.getMessage(code, args);
	}

	protected String redirect(String path){
		return REDIRECT + path;
	}
	
	public void addFlashMessage(RedirectAttributes redirectAttributes, String msg){
		redirectAttributes.addFlashAttribute(MESSAGE, StringUtils.trimToEmpty(msg));
	}

	
	/*****
	 * 根据model返回一个ModelAndView
	 * @param models
	 * @return
	 */
	protected ModelAndView model(Object... models){
		return mv(null, models);
	}
	
	protected ModelAndView redirectTo(String path){
		return mv(redirect(path));
	}
	
	protected ModelAndView redirectTo(String path, String message){
		return mv(redirect(path), MESSAGE, message, MESSAGE_TYPE, MESSAGE_TYPE_SUCCESS);
	}
	
	/**********
	 * 根据view名称和model返回一个ModelAndView
	 * @param viewName
	 * @param models "key1", value1, "key2", value2 ...
	 * @return
	 */
	protected ModelAndView mv(String viewName, Object... models){
		return JFishWebUtils.mv(viewName, models);
	}
	
	protected ModelAndView messageMv(String message){
		return mv(MESSAGE, MESSAGE, message, MESSAGE_TYPE, MESSAGE_TYPE_SUCCESS);
	}
	
	public ModelAndView doInModelAndView(HttpServletRequest request, ModelAndView mv){
		return mv;
	}
	
	/*********
	 * 
	 * @param template 模板名称
	 * @param fileName 下载的文件名称
	 * @param models 生成excel的context
	 * @return
	 */
	protected ModelAndView exportExcel(String template, String fileName, Object... models){
		ModelAndView mv = mv(template, models);
		JFishExcelView view = new JFishExcelView();
		view.setUrl(template);
		view.setFileName(fileName);
		mv.setView(view);
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends UserDetail> T getCurrentUserLogin(HttpSession session){
		return (T)WebContextUtils.getUserDetail(session);
	}
	
	protected <T> void validate(T object, BindingResult bindResult, Class<?>... groups){
		this.getValidator().validate(object, bindResult, groups);
	}
	
	protected ValidatorWrapper getValidator(){
		 return SpringApplication.getInstance().getValidator();
	}
	
	protected <T> ValidationBindingResult validate(T object, Class<?>... groups){
		return getValidator().validate(object, groups);
	}
	

	protected void validateAndThrow(Object obj, Class<?>... groups){
		ValidationBindingResult validations = validate(obj, groups);
		if(validations.hasErrors()){
			throw new ValidationException(validations.getFieldErrorMessagesAsString());
		}
	}
	
	protected void download(HttpServletResponse response, String filePath){
		String filename = FileUtils.getFileName(filePath);
		try {
			download(response, new FileInputStream(filePath), filename);
		} catch (FileNotFoundException e) {
			String msg = "下载文件出错：";
			logger.error(msg + e.getMessage(), e);
		}
	}
	
	protected void download(HttpServletResponse response, InputStream input, String filename){
		try {
			response.setContentType(DEFAULT_CONTENT_TYPE); 
			String name = new String(filename.getBytes("GBK"), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + name);
			IOUtils.copy(input, response.getOutputStream());
		} catch (Exception e) {
			String msg = "下载文件出错：";
			logger.error(msg + e.getMessage(), e);
		} finally{
			IOUtils.closeQuietly(input);
		}
	}

	protected UserDetail getCurrentLoginUser(){
		return JFishWebUtils.getUserDetail();
	}
}

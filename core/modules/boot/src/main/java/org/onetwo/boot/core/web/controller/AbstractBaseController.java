package org.onetwo.boot.core.web.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.onetwo.apache.io.IOUtils;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.boot.core.web.utils.ResponseFlow;
import org.onetwo.common.data.AbstractDataResult;
import org.onetwo.common.data.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.data.DataResult;
import org.onetwo.common.exception.NotLoginException;
import org.onetwo.common.file.FileStoredMeta;
import org.onetwo.common.file.FileStorer;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.file.StoreFilePathStrategy;
import org.onetwo.common.file.StoringFileContext;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.spring.mvc.utils.DataResults;
import org.onetwo.common.spring.mvc.utils.DataWrapper;
import org.onetwo.common.spring.mvc.utils.ModelAttr;
import org.onetwo.common.spring.validator.ValidationBindingResult;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.func.ReturnableClosure;
import org.onetwo.common.web.userdetails.SessionUserManager;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseType;
import org.onetwo.common.web.utils.WebHolder;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

abstract public class AbstractBaseController {
//	public static final String SINGLE_MODEL_FLAG_KEY = "__SINGLE_MODEL_FLAG_KEY__";
	
	public static final String DEFAULT_CONTENT_TYPE = "application/download; charset=GBK";
	
	public static final String REDIRECT = "redirect:";
	public static final String MESSAGE = ModelAttr.MESSAGE;
	public static final String ERROR = ModelAttr.ERROR_MESSAGE;
	public static final String MESSAGE_TYPE = "messageType";
	public static final String MESSAGE_TYPE_ERROR = "error";
	public static final String MESSAGE_TYPE_SUCCESS = "success";
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	@Resource
	private BootSiteConfig bootSiteConfig;
	
	@Autowired(required=false)
	private FileStorer<?> fileStorer;
	
	@Autowired
	private SessionUserManager<UserDetail> sessionUserManager;
	
	
	protected AbstractBaseController(){
	}
	
	public SessionUserManager<UserDetail> getSessionUserManager() {
		return sessionUserManager;
	}

	protected FileStoredMeta uploadFile(String module, MultipartFile file){
		Assert.notNull(file, "file is required; it must not be null");
		Assert.notNull(fileStorer, "fileStorer is required; it must not be null");
		return uploadFile(module, file, null);
	}
	
	protected FileStoredMeta uploadFile(String module, MultipartFile file, StoreFilePathStrategy storeFilePathStrategy){
		Assert.notNull(file, "file is required; it must not be null");
		Assert.notNull(fileStorer, "fileStorer is required; it must not be null");
//		context = StoringFileContext.create(module, file.getInputStream(), file.getOriginalFilename());
//		Assert.notNull(storeFilePathStrategy);
		StoringFileContext context = BootWebUtils.createStoringFileContext(module, file);
		if(storeFilePathStrategy!=null){
			context.setStoreFilePathStrategy(storeFilePathStrategy);
		}
		return fileStorer.write(context);
	}

	protected String redirect(String path){
		return REDIRECT + path;
	}
	
	protected void addFlashMessage(RedirectAttributes redirectAttributes, String msg){
		redirectAttributes.addFlashAttribute(MESSAGE, StringUtils.trimToEmpty(msg));
		redirectAttributes.addFlashAttribute(MESSAGE_TYPE, MESSAGE_TYPE_SUCCESS);
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
	
	protected ModelAndView redirectToWithError(String path, String error){
		return mv(redirect(path), MESSAGE, error, MESSAGE_TYPE, MESSAGE_TYPE_ERROR);
	}
	
	protected ModelAndView putSuccessMessage(ModelAndView mv, String message){
		Assert.notNull(mv);
		mv.addObject(MESSAGE, message);
		mv.addObject(MESSAGE_TYPE, MESSAGE_TYPE_SUCCESS);
		return mv;
	}
	
	protected ModelAndView putErrorMessage(ModelAndView mv, String message){
		Assert.notNull(mv);
		mv.addObject(MESSAGE, message);
		mv.addObject(MESSAGE_TYPE, MESSAGE_TYPE_ERROR);
		return mv;
	}
	
	/**********
	 * 根据view名称和model返回一个ModelAndView
	 * @param viewName
	 * @param models "key1", value1, "key2", value2 ...
	 * @return
	 */
	protected ModelAndView mv(String viewName, Object... models){
		return BootWebUtils.createModelAndView(viewName, models);
	}
	protected ModelAndView redirectMv(String viewName, Object... models){
		return BootWebUtils.createModelAndView(redirect(viewName), models);
	}
	
	protected ModelAndView messageMv(String message){
		return mv(MESSAGE, MESSAGE, message, MESSAGE_TYPE, MESSAGE_TYPE_SUCCESS);
	}
	
	protected ModelAndView errorMv(String message){
		return mv(ERROR, ERROR, message, MESSAGE_TYPE, MESSAGE_TYPE_ERROR);
	}

	protected ModelAttr messageAttr(String message){
		return new ModelAttr(MESSAGE, message);
	}
	protected ModelAttr errorAttr(String message){
		return new ModelAttr(ModelAttr.ERROR_MESSAGE, message);
	}
	
	protected ModelAttr atrr(String name, Object value){
		return new ModelAttr(name, value);
	}
	
	/*********
	 * 
	 * @param template 模板名称
	 * @param fileName 下载的文件名称
	 * @param models 生成excel的context
	 * @return
	 *protected ModelAndView exportExcel(String template, String fileName, Object... models){
		ModelAndView mv = mv(template, models);
		JFishExcelView view = new JFishExcelView();
		String path = this.xmlTemplateExcelViewResolver.getPrefix()+template;
		view.setUrl(path);
		view.setFileName(fileName);
		view.setSuffix(this.xmlTemplateExcelViewResolver.getSuffix());
		mv.setView(view);
		return mv;
	}*/
	
	/*@SuppressWarnings("unchecked")
	protected <T extends UserDetail> T getCurrentUserLogin(HttpSession session){
		return (T)WebContextUtils.getUserDetail(session);
	}*/
	
	protected <T> void validate(T object, BindingResult bindResult, Class<?>... groups){
		this.getValidator().validate(object, bindResult, groups);
	}
	
	protected ValidatorWrapper getValidator(){
		 return Springs.getInstance().getValidator();
	}
	
	protected <T> ValidationBindingResult validate(T object, Class<?>... groups){
		return getValidator().validate(object, groups);
	}
	

	protected void validateAndThrow(Object obj, Class<?>... groups){
		ValidationBindingResult validations = validate(obj, groups);
		if(validations.hasErrors()){
			throw new ValidationException(validations.getErrorMessagesAsString());
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
			response.setContentLength(input.available());
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
	
	protected void exportText(HttpServletResponse response, List<?> datas, String filename){
		exportText(response, datas, filename, obj->obj.toString());
	}
	
	protected void exportText(HttpServletResponse response, List<?> datas, String filename, ReturnableClosure<Object, String> block){
		PrintWriter out = null;
		try {
			out = response.getWriter();
			response.setContentType(DEFAULT_CONTENT_TYPE); 
			String name = new String(filename.getBytes("GBK"), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + name);
			for(Object data : datas){
				out.println(block.execute(data));
			}
			out.flush();
		} catch (Exception e) {
			String msg = "下载文件出错：";
			logger.error(msg + e.getMessage(), e);
		} finally{
			IOUtils.closeQuietly(out);
		}
	}
	
	/*****
	 * 检查当前登录用户，如果没有找到则抛出异常，如果找到，返回登录信息
	 * @return
	 */
	protected UserDetail checkAndGetCurrentLoginUser(){
		return checkAndGetCurrentLoginUser(UserDetail.class, true);
	}
	protected UserDetail checkAndGetCurrentLoginUser(boolean throwIfNotFound){
		return checkAndGetCurrentLoginUser(UserDetail.class, throwIfNotFound);
	}
	/***
	 * 检查当前登录用户
	 * @param clazz
	 * @param throwIfNotFound true表示如果没有找到则抛出异常，false则忽略
	 * @return
	 */
	protected <T extends UserDetail> T checkAndGetCurrentLoginUser(Class<T> clazz, boolean throwIfNotFound){
		Assert.notNull(clazz, "class can not be null!");
		UserDetail user = null;
		try {
			user = sessionUserManager.getCurrentUser();
		} catch (RuntimeException e) {
			if(throwIfNotFound){
				throw e;
			}
			return null;
		}
		if(user==null && throwIfNotFound){
			throw new NotLoginException();
		}
		return clazz.cast(user);
	}
	
	@ModelAttribute(name=UserDetail.USER_DETAIL_KEY)
	public UserDetail getCurrentLoginUser(){
		return checkAndGetCurrentLoginUser(false);
	}
	public <T extends UserDetail> T getCurrentLoginUser(Class<T> clazz){
		return checkAndGetCurrentLoginUser(clazz, false);
	}

	protected BootSiteConfig getBootSiteConfig() {
		return bootSiteConfig;
	}

	/***
	 * 返回json，里面只包含 value 数据对象
	 * @param value
	 * @return
	 */
	protected ModelAndView responseData(Object value){
		return mv("", DataWrapper.wrap(value));
	}
	/***
	 * 返回json，里面只包含 value 数据对象
	 * @param value
	 * @return
	 */
	protected ModelAndView responseData(JsonResponseValue value){
		return mv("", getJsonResponseValue(value));
	}
	
	protected Object getJsonResponseValue(JsonResponseValue value){
		Object actualValue = null;//DataWrapper.lazy(value);
		if(isResponseJson() || isResponseXml()){
			actualValue = DataWrapper.wrap(value.get());
		}
		return actualValue;
	}
	
	protected DataResult<?> asSucceedResult(Object value){
//		return SimpleDataResult.createSucceed(null, value);
		return DataResults.simple(value).build();
	}
	
	protected AbstractDataResult<Object> asFailedResult(String msg){
		return SimpleDataResult.error(msg);
	}

	/***
	 * SimpleResultBuilder
	 * 把数据包装为{@linkplain org.onetwo.common.data.DataResult Result} ({@linkplain SimpleDataResult SimpleDataResult}) 类型返回
	 * 
	 * @return
	 */
	/*protected SimpleResultBuilder result(){
		return WebResultCreator.creator().simple(null);
	}*/
	
	protected ResponseType getResponseType(){
		return RequestUtils.getResponseType(WebHolder.getRequest().get());
	}
	
	/******
	 * return responseFlow().responsePage(()->result().simple().buildModelAndView())
							  .responseJson(()->result().simple().buildModelAndView())
							  .execute();
	 * @return
	 */
	protected ResponseFlow responseFlow(){
		return new ResponseFlow(getResponseType());
	}

	/***
	 * 如果正常请求，会返回viewName对应页面；
	 * 如果是json后缀请求，会以json的形式返回value执行的结果
	 * @param viewName
	 * @param value
	 * @return
	 */
	protected ModelAndView responsePageOrData(String viewName, JsonResponseValue value){
		return BootWebUtils.createModelAndView(getViewName(viewName), getJsonResponseValue(value));
	}
	protected String getViewName(String viewName){
		return viewName;
	}
	protected ModelAndView responsePageOrData(ModelAndView mv, JsonResponseValue value){
		Assert.notNull(mv);
		mv.addObject(getJsonResponseValue(value));
		return mv;
	}
	/***
	 * 如果请求的url是.json后缀
	 * @return
	 */
	protected boolean isResponseJson(){
		return getResponseType()==ResponseType.JSON;
	}
	
	/***
	 * 如果请求的url是.xml后缀
	 * @return
	 */
	protected boolean isResponseXml(){
		return getResponseType()==ResponseType.XML;
	}
	
	/***
	 * 如果请求的url没有添加后缀，返回true
	 * @return
	 */
	protected boolean isResponsePage(){
		return getResponseType()==ResponseType.PAGE;
	}
	
	protected boolean isResponseJFxls(){
		return getResponseType()==ResponseType.JFXLS;
	}
	
	protected boolean isResponseType(ResponseType ResponseType){
		return getResponseType()==ResponseType;
	}
	
	static public interface JsonResponseValue {
		Object get();
	}
	
}

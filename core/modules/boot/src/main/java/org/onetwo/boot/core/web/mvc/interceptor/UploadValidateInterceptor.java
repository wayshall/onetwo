package org.onetwo.boot.core.web.mvc.interceptor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.web.mvc.exception.UploadFileSizeLimitExceededException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.exception.SystemErrorCode.UplaodErrorCode;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

public class UploadValidateInterceptor extends WebInterceptorAdapter {

	public static final String[] DEFAULT_ALLOW_FILE_TYPES = {"jpg", "jpeg", "gif", "png", "bmp", "xls", "xlsx", "pdf", "doc", "txt"};

//	public static final String[] ALL = new String[]{""};
	
//	private List<String> allowedFileTypes = LangUtils.newArrayList(DEFAULT_ALLOW_FILE_TYPES);
	@Autowired
	private BootSiteConfig siteConfig;
	@Autowired
	private ApplicationContext applicationContext;

	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handlerMethod = getHandlerMethod(handler);
		if(isSupport(request, handlerMethod)){
			this.doValidate(request, response, handlerMethod);
		}
		return true;
	}
	
	//默认检查所有上传
	public boolean isSupport(HttpServletRequest request, HandlerMethod handler) {
		return MultipartRequest.class.isInstance(request);
//		return MultipartRequest.class.isInstance(request) && handler.getMethodAnnotation(UploadFileValidator.class)!=null;
	}

	public void doValidate(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		UploadFileValidator validator = handler.getMethodAnnotation(UploadFileValidator.class);
		
		MultipartRequest mrequest = (MultipartRequest) request;
//		Collection<List<MultipartFile>> files = mrequest.getMultiFileMap().values();
		/*for(List<MultipartFile> mfiles : files){
			checkFileTypes(mfiles, allowed, validator.errorPostfixMessage());
		}*/
		List<MultipartFile> files = mrequest.getMultiFileMap().values()
					.stream()
					.flatMap(list->list.stream())
					.filter(f->StringUtils.isNotBlank(f.getOriginalFilename()))
					.collect(Collectors.toList());
		
		if(files.isEmpty() && validator!=null && !validator.nullable()){
			throw new ServiceException(validator.nullableErrorMessage());
		}
		this.checkFileTypes(files, validator);
	}
	
	protected void checkFileTypes(List<MultipartFile> fileItems, UploadFileValidator validator){
		List<String> allowed = validator!=null?Arrays.asList(validator.allowedPostfix()):Arrays.asList(DEFAULT_ALLOW_FILE_TYPES);
		int configMaxUploadSize= this.siteConfig.getUpload().getMaxFileUploadSizeInBytes();
		int maxUploadSize = validator.maxUploadSize();
		String configKey = validator.maxUploadSizeConfigKey();
		if (StringUtils.isNotBlank(configKey)) {
			if (this.applicationContext.getEnvironment().containsProperty(configKey)) {
				configKey = this.applicationContext.getEnvironment().getProperty(configKey);
				maxUploadSize = FileUtils.parseSize(configKey);
			} else {
//				throw new BaseException(String.format("maxUploadSizeConfigKey[%s] not foud!", configKey));
				JFishLoggerFactory.getCommonLogger().warn("UploadValidateInterceptor check waring: maxUploadSizeConfigKey[{}] not foud!", configKey);
			}
		}
		
		for(MultipartFile item : fileItems) {
			String postfix = FileUtils.getExtendName(item.getOriginalFilename().toLowerCase().trim());//trim防止后缀加空格绕过检查
			
			if (validator==null) {
				if (!allowed.contains(postfix)) {
					throw new ServiceException("It's not allowed file type. file: " + item.getOriginalFilename(), UplaodErrorCode.NOT_ALLOW_FILE);
				}
			} else {
				if (!allowed.contains(postfix)) {
					throw new ServiceException(validator.allowedPostfixErrorMessage() + " file: " + item.getOriginalFilename(), UplaodErrorCode.NOT_ALLOW_FILE);
				}
			}
			
			// 优先检查配置文件的
			if (configMaxUploadSize!=-1) {
				if (item.getSize()>configMaxUploadSize) {
					throw new UploadFileSizeLimitExceededException(configMaxUploadSize, item.getSize());
				}
			}
			// 再检查注解的配置
			if (item.getSize()>maxUploadSize) {
				throw new UploadFileSizeLimitExceededException(maxUploadSize, item.getSize());
			}
//				throw new MaxUploadSizeExceededException(validator.maxUploadSize());
		}
	}

	/*public void setAllowedFileTypes(List<String> allowedFileTypes) {
		this.allowedFileTypes = allowedFileTypes;
	}*/


	@Override
	public int getOrder() {
		return after(ORDERED_LOG);
	}
	
	

}

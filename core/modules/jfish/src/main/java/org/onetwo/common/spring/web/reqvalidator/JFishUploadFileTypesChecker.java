package org.onetwo.common.spring.web.reqvalidator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

public class JFishUploadFileTypesChecker implements JFishRequestValidator {
	
	public static final String[] ALL = new String[]{""};
	
	private List<String> allowedFileTypes = LangUtils.newArrayList("jpg", "jpeg", "gif", "png", "bmp", "xls", "xlsx", "pdf", "doc", "txt");

	
	@Override
	public boolean isSupport(HttpServletRequest request, HandlerMethod handler) {
		return MultipartRequest.class.isInstance(request) && handler.getMethodAnnotation(UploadFileTypeValidator.class)!=null;
	}

	@Override
	public void doValidate(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		UploadFileTypeValidator validator = handler.getMethodAnnotation(UploadFileTypeValidator.class);
		List<String> allowed = validator.allowed().length==0?allowedFileTypes:Arrays.asList(validator.allowed());
		MultipartRequest mrequest = (MultipartRequest) request;
		Collection<List<MultipartFile>> files = mrequest.getMultiFileMap().values();
		for(List<MultipartFile> mfiles : files){
			checkFileTypes(mfiles, allowed, validator.message());
		}
	}
	
	protected void checkFileTypes(List<MultipartFile> fileItems, List<String> allowed, String msg){
		for(MultipartFile item : fileItems){
			String postfix = FileUtils.getExtendName(item.getOriginalFilename().toLowerCase());
			if(!allowed.contains(postfix))
				throw new ServiceException(msg + ": " + item.getOriginalFilename());
		}
	}

	public void setAllowedFileTypes(List<String> allowedFileTypes) {
		this.allowedFileTypes = allowedFileTypes;
	}

}

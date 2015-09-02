package org.onetwo.common.spring.validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.onetwo.common.annotation.AnnotationUtils;
import org.onetwo.common.annotation.JInfo;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public final class ValidatorUtils {
	
	private ValidatorUtils(){}

	
	public static <T> void addConstraintViolations(Set<ConstraintViolation<T>> constrains, BindingResult bind){
		if(LangUtils.isEmpty(constrains))
			return ;
		for(ConstraintViolation<T> c : constrains){
			bind.rejectValue(c.getPropertyPath().toString(), c.getMessageTemplate(), c.getMessage());
		}
	}
	
	public static JInfo findValidationInfo(Class<?> objClass, String fieldName){
		return AnnotationUtils.findFieldAnnotation(objClass, fieldName, JInfo.class);
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> asStringList(BindingResult br, boolean appendFieldname){
		if(br==null || !br.hasErrors())
			return Collections.EMPTY_LIST;
		List<String> msglist = new ArrayList<String>();
		String msg = null;
		for(ObjectError error : br.getAllErrors()){
			msg = "";
			if(appendFieldname && FieldError.class.isInstance(error)){
				msg = ((FieldError)error).getField();
			}
			msg += error.getDefaultMessage();
			msglist.add(msg);
		}
		return msglist;
	}
	
	public static String asString(BindingResult br, String op){
		return StringUtils.join(asStringList(br, true), op);
	}

	public static String asString(BindingResult br){
		return asString(br, ", ");
	}

	public static String asString(BindingResult br, boolean appendFieldname){
		return StringUtils.join(asStringList(br, appendFieldname), ", ");
	}
}

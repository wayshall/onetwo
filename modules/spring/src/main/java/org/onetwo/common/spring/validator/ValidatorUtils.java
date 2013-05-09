package org.onetwo.common.spring.validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.utils.LangUtils;
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
	
	public static ValidationInfo findValidationInfo(Class<?> objClass, String fieldName){
		return AnnotationUtils.findFieldAnnotation(objClass, fieldName, ValidationInfo.class);
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> asStringList(BindingResult br){
		if(br==null || !br.hasErrors())
			return Collections.EMPTY_LIST;
		List<String> msglist = new ArrayList<String>();
		String msg = null;
		for(ObjectError error : br.getAllErrors()){
			msg = "";
			if(FieldError.class.isInstance(error)){
				msg = ((FieldError)error).getField();
			}
			msg += error.getDefaultMessage();
			msglist.add(msg);
		}
		return msglist;
	}
	
	public static String asString(BindingResult br, String op){
		return StringUtils.join(asStringList(br), op);
	}

	public static String asString(BindingResult br){
		return asString(br, ", ");
	}
}

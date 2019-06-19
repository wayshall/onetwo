package org.onetwo.common.spring.validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;

import org.onetwo.common.annotation.AnnotationUtils;
import org.onetwo.common.annotation.FieldName;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.google.common.collect.ImmutableList;

public final class ValidatorUtils {
	
	private final static String MESSAGE_SEPERATOR = "; ";
	

	public static interface ValidGroup {
		public static interface ValidAnyTime {}
		public static interface ValidWhenNew {}
		public static interface ValidWhenEdit {}
	}
	
	private ValidatorUtils(){}

	
	public static <T> void addConstraintViolations(Set<ConstraintViolation<T>> constrains, BindingResult bind){
		if(LangUtils.isEmpty(constrains))
			return ;
		for(ConstraintViolation<T> c : constrains){
			bind.rejectValue(c.getPropertyPath().toString(), c.getMessageTemplate(), c.getMessage());
		}
	}
	
	public static FieldName findValidationInfo(Class<?> objClass, String fieldName){
		return AnnotationUtils.findFieldAnnotation(objClass, fieldName, FieldName.class);
	}

	public static void throwIfHasErrors(BindingResult br, boolean appendFieldname){
		List<String> errors = asStringList(br, appendFieldname);
		if(!errors.isEmpty()){
			String msg = StringUtils.join(asStringList(br, appendFieldname), MESSAGE_SEPERATOR);
			throw new ValidationException(msg);
		}
	}
	
	public static List<String> asStringList(BindingResult br, boolean appendFieldname){
		if(br==null || !br.hasErrors())
			return Collections.emptyList();
		List<String> msglist = new ArrayList<String>();
		String msg = null;
		for(ObjectError error : br.getAllErrors()){
			msg = "";
			if(appendFieldname && FieldError.class.isInstance(error)){
				FieldError fe = (FieldError) error;
				FieldName info = findValidationInfo(br.getTarget().getClass(), fe.getField());
//				msg = info==null?fe.getField():info.value();
				msg = info==null?"":info.value();
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
		return asString(br, MESSAGE_SEPERATOR);
	}

	public static String asString(BindingResult br, boolean appendFieldname){
		return StringUtils.join(asStringList(br, appendFieldname), MESSAGE_SEPERATOR);
	}
	
	public static String toMessages(Set<ConstraintViolation<?>> constrains){
		List<String> msgs = toMessageList(constrains);
		return StringUtils.join(msgs, MESSAGE_SEPERATOR);
	}
	public static List<String> toMessageList(Set<ConstraintViolation<?>> constrains){
		if(constrains==null || constrains.isEmpty())
			return ImmutableList.of();
		
		return constrains.stream().map(c->{
			return c.getPropertyPath().toString() + c.getMessage();
		})
		.collect(Collectors.toList());
	}
	

	public static void throwIfValidateFailed(Set<ConstraintViolation<?>> constrains){
		if(constrains==null || constrains.isEmpty())
			return;
		String msg = toMessages(constrains);
		throw new ValidationException(msg);
	}
	
}

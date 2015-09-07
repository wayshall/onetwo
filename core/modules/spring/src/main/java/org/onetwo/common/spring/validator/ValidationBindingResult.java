package org.onetwo.common.spring.validator;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.onetwo.common.annotation.JInfo;
import org.onetwo.common.utils.JFishProperty;
import org.onetwo.common.utils.JFishPropertyFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.func.MapClosure;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

@SuppressWarnings("serial")
public class ValidationBindingResult extends BeanPropertyBindingResult {

	public static ValidationBindingResult create(Object target){
		return new ValidationBindingResult(target);
	}
	public static <T> ValidationBindingResult create(Object target, Set<ConstraintViolation<T>> constrains){
		ValidationBindingResult binding = new ValidationBindingResult(target);
		ValidatorUtils.addConstraintViolations(constrains, binding);
		return binding;
	}
	
	private ValidationBindingResult(Object target) {
		super(target, StringUtils.uncapitalize(target.getClass().getSimpleName()));
	}
	

	public String getFieldErrorMessage(String fieldName){
		return getFieldErrorMessage(fieldName, true);
	}

	public String getFieldErrorMessage(String fieldName, boolean readFieldMeta){
		return getFieldErrorMessage(getFieldError(fieldName), readFieldMeta);
	}

	public String getFieldErrorMessage(FieldError fe, boolean readFieldMeta){
		if(fe==null){
//			throw new JFishException("can not find this field named : " + fieldName);
			return "";
		}

		String fmsg = null;
		if(readFieldMeta){
			JFishProperty jp = JFishPropertyFactory.create(getTarget().getClass(), fe.getField(), true);
			if(jp==null)
				jp = JFishPropertyFactory.create(getTarget().getClass(), fe.getField(), false);
			JInfo jfm = jp.getAnnotation(JInfo.class);
			if(jfm==null){
				fmsg = LangUtils.append(fe.getField(), fe.getDefaultMessage());
			}else{
				fmsg = LangUtils.append(jfm.label(), fe.getDefaultMessage());
			}
		}else{
			fmsg = fe.getDefaultMessage();
		}
		return fmsg;
	}

	public String getFieldErrorMessage(FieldError fe, MapClosure<FieldError, String> block){
		String fmsg = "";
		if(block!=null){
			fmsg = block.execute(fe);
		}else{
			if(fe!=null)
				fmsg = fe.getDefaultMessage();
		}
		return fmsg;
	}
	
	public List<String> getFieldErrorMessages(MapClosure<FieldError, String> block){
		List<FieldError> fieldErrors = getFieldErrors();
		List<String> msgs = LangUtils.newArrayList(fieldErrors.size());
		for(FieldError fe : fieldErrors){
			msgs.add(getFieldErrorMessage(fe, block));
		}
		return msgs;
	}
	
	public List<String> getFieldErrorMessages(final boolean readFieldMeta){
		/*List<FieldError> fieldErrors = getFieldErrors();
		List<String> msgs = LangUtils.newArrayList(fieldErrors.size());
		for(FieldError fe : fieldErrors){
			msgs.add(getFieldErrorMessage(fe, readFieldMeta));
		}
		return msgs;*/
		return getFieldErrorMessages(new MapClosure<FieldError, String>() {

			@Override
			public String execute(FieldError fe) {
				if(fe==null){
//					throw new JFishException("can not find this field named : " + fieldName);
					return "";
				}

				String fmsg = null;
				if(readFieldMeta){
					JFishProperty jp = JFishPropertyFactory.create(getTarget().getClass(), fe.getField(), true);
					if(jp==null)
						jp = JFishPropertyFactory.create(getTarget().getClass(), fe.getField(), false);
					JInfo jfm = jp.getAnnotation(JInfo.class);
					if(jfm==null){
						fmsg = LangUtils.append(fe.getField(), fe.getDefaultMessage());
					}else{
						fmsg = LangUtils.append(jfm.label(), fe.getDefaultMessage());
					}
				}else{
					fmsg = fe.getDefaultMessage();
				}
				return fmsg;
			}
			
		});
	}
	
	public String getFieldErrorMessagesAsString(){
		return getFieldErrorMessagesAsString(true, ", ");
	}
	
	public String getFieldErrorMessagesAsString(boolean readFieldMeta, String op){
		return StringUtils.join(getFieldErrorMessages(readFieldMeta), op);
	}

}

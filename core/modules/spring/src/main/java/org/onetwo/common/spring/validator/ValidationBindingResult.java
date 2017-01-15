package org.onetwo.common.spring.validator;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.onetwo.common.utils.FieldName;
import org.onetwo.common.utils.JFishProperty;
import org.onetwo.common.utils.JFishPropertyFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.func.ReturnableClosure;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

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
			FieldName jfm = jp.getAnnotation(FieldName.class);
			if(jfm==null){
				fmsg = LangUtils.append(fe.getField(), fe.getDefaultMessage());
			}else{
				fmsg = LangUtils.append(jfm.value(), fe.getDefaultMessage());
			}
		}else{
			fmsg = fe.getDefaultMessage();
		}
		return fmsg;
	}

	public String getErrorMessage(ObjectError fe, ReturnableClosure<ObjectError, String> block){
		String fmsg = "";
		if(block!=null){
			fmsg = block.execute(fe);
		}else{
			if(fe!=null)
				fmsg = fe.getDefaultMessage();
		}
		return fmsg;
	}
	
	public List<String> getErrorMessages(ReturnableClosure<ObjectError, String> block){
		List<ObjectError> fieldErrors = this.getAllErrors();
		List<String> msgs = LangUtils.newArrayList(fieldErrors.size());
		for(ObjectError fe : fieldErrors){
			msgs.add(getErrorMessage(fe, block));
		}
		return msgs;
	}
	
	public List<String> getErrorMessages(final boolean readFieldMeta){
		/*List<FieldError> fieldErrors = getFieldErrors();
		List<String> msgs = LangUtils.newArrayList(fieldErrors.size());
		for(FieldError fe : fieldErrors){
			msgs.add(getFieldErrorMessage(fe, readFieldMeta));
		}
		return msgs;*/
		return getErrorMessages(new ReturnableClosure<ObjectError, String>() {

			@Override
			public String execute(ObjectError fe) {
				if(fe==null){
//					throw new JFishException("can not find this field named : " + fieldName);
					return "";
				}

				String fmsg = null;
				if(readFieldMeta && FieldError.class.isInstance(fe)){
					FieldError fieldError = (FieldError) fe;
					JFishProperty jp = JFishPropertyFactory.create(getTarget().getClass(), fieldError.getField(), true);
					if(jp==null)
						jp = JFishPropertyFactory.create(getTarget().getClass(), fieldError.getField(), false);
					FieldName jfm = jp.getAnnotation(FieldName.class);
					if(jfm==null){
						fmsg = LangUtils.append(fieldError.getField(), fe.getDefaultMessage());
					}else{
						fmsg = LangUtils.append(jfm.value(), fe.getDefaultMessage());
					}
				}else{
					fmsg = fe.getDefaultMessage();
				}
				return fmsg;
			}
			
		});
	}
	
	public String getErrorMessagesAsString(){
		return getErrorMessagesAsString(true, ", ");
	}
	
	public String getErrorMessagesAsString(boolean readFieldMeta, String op){
		return StringUtils.join(getErrorMessages(readFieldMeta), op);
	}

}

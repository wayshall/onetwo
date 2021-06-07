package org.onetwo.common.spring.mvc.utils;

import org.onetwo.common.data.AbstractDataResult;
import org.onetwo.common.data.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.data.DataResult;
import org.onetwo.common.exception.ErrorType;
import org.onetwo.common.utils.StringUtils;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("unchecked")
abstract public class AbstractResultBuilder<T, B extends AbstractResultBuilder<T, B>> {
	
	protected String code = AbstractDataResult.SUCCESS;
	protected String message;
	private boolean extractableData = false;
	protected T data;
	private String view;
	private Boolean showMessage;
//	final protected Class<?> builderClass;
//	private T data;
	
	public AbstractResultBuilder() {
		super();
//		this.builderClass = builderClass;
	}

	public B success(){
		return success(null);
	}
	
	public B success(String message){
		return code(AbstractDataResult.SUCCESS, message);
	}


	public B error(){
		return error(AbstractDataResult.ERROR.toLowerCase());
	}
	public B error(String message){
		return code(AbstractDataResult.ERROR, message);
	}
	public B view(String view){
		this.view = view;
		return (B) this;
	}
	
	/****
	 * 如果表示错误，以 {@linkplain AbstractDataResult#ERROR ERROR} 为前缀
	 * {@linkplain AbstractDataResult#EXCEPTION_POSTFIX EXCEPTION_POSTFIX}  为后缀
	 * @param code
	 * @param message
	 * @return
	 */
	public B code(String code, String message){
		this.code = code;
		message(message);
//		return builderClass.cast(this);
		return (B) this;
	}
	public B code(Enum<?> code, String message){
		return code(code.name(), message);
	}
	
	/****
	 * 指示客户端是否只提取result里的data作为返回结果
	 * @param extractableData
	 * @return
	 */
	public B extractableData(boolean extractableData){
		this.extractableData = extractableData;
		return (B) this;
	}
	public B code(String code){
		this.code = code;
		return (B) this;
	}
	public B code(Enum<?> code){
		this.code = code.name();
		if(message==null) {
			message(code.toString());
		}
		return (B) this;
	}

	public B message(String message){
		this.message = message;
		if (StringUtils.isNotBlank(message)) {
			this.showMessage = true;
		}
//		return builderClass.cast(this);
		return (B) this;
	}
	
	public B error(ErrorType errorType){
		this.code = errorType.getErrorCode();
		this.message(errorType.getErrorMessage());
		return (B) this;
	}

	public B data(T data){
		this.data = data;
		return (B) this;
	}
	
	public B showMessage(boolean showMessage) {
		this.showMessage = showMessage;
		return (B) this;
	}

	protected SimpleDataResult<T> creeateResult(){
		SimpleDataResult<T> rs = SimpleDataResult.create(code, message, data);
		return rs;
	}

	public SimpleDataResult<T> build(){
		SimpleDataResult<T> rs = creeateResult();
		if (this.showMessage!=null) {
			rs.setShowMessage(showMessage);
		}
		rs.setExtractableData(extractableData);
		return rs;
	}

	public ModelAndView buildModelAndView(){
		DataResult<T> rs = build();
		return MvcUtils.createModelAndView(view, rs);
	}
	
	/*public SimpleDataResult<?> buildResult(){
		return SimpleDataResult.create(code, message, null);
	}*/
	
	/*public SimpleDataResult<?> buildSimpleResult(Object data){
		return SimpleDataResult.create(code, message, data);
	}
	
	public MapResult buildMapResult(Object...datas){
		return MapResult.create(code, message, datas);
	}
	
	public LazyResult buildLazyResult(LazyValue lazyValue){
		return LazyResult.create(code, message, lazyValue);
	}*/

}

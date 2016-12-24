package org.onetwo.common.commandline;

import org.onetwo.common.date.DateUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;


public class InputValidators {
	public static final InputValidator NOT_EMPTY = new InputValidator(){

		@Override
		public void validate(String input) {
			if(StringUtils.isBlank(input))
				throw new CommandLineException("输入不能为空！");
		}
		
	};
	public static final InputValidator DIGIT = new AbstractInputValidator(){

		@Override
		public void doValidate(String input) {
			if(!LangUtils.isDigitString(input))
				throw newError("输入必须是数字：" + input);
		}
		
	};
	public static final InputValidator DATE = new AbstractInputValidator(){

		@Override
		public void doValidate(String input) {
			String pattern = DateUtils.matchPattern(input);
			if(StringUtils.isBlank(pattern))
				throw newError("必须是日期格式，输入错误：" + input);
		}
		
	};
	private InputValidators(){
	}

}

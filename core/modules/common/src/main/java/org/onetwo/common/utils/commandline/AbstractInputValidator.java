package org.onetwo.common.utils.commandline;

import org.onetwo.common.utils.StringUtils;

abstract public class AbstractInputValidator implements InputValidator {

	protected CommandLineException newError(String msg){
		return new CommandLineException(msg);
	}
	@Override
	public void validate(String input) {
		if(StringUtils.isBlank(input))
			return ;
		doValidate(input);
	}
	
	abstract protected void doValidate(String input);

}

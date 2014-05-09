package org.onetwo.common.utils.commandline;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class SimpleCmdContext implements CmdContext {
	
	protected Map<String, Object> context;
	protected BufferedReader cmdInput;
	
	public SimpleCmdContext(BufferedReader cmdInput){
		context = new HashMap<String, Object>();
		this.cmdInput = cmdInput;
	}

	@Override
	public Object getVar(Object name) {
		return context.get(name.toString());
	}

	@Override
	public void setVar(Object key, Object value) {
		context.put(key.toString(), value);
	}

	@Override
	public BufferedReader getCmdInput() {
		return cmdInput;
	}

	@Override
	public String nextInput(String tips, String def, InputValidator...validators) throws IOException {
		System.out.print(tips);
		String input = cmdInput.readLine();
		if(!LangUtils.isEmpty(validators)){
			for(InputValidator validator : validators){
				validator.validate(input);
			}
		}
		if(StringUtils.isBlank(input) && StringUtils.isNotBlank(def))
			input = def;
		return input;
	}

	@Override
	public void setCmdInput(BufferedReader input) {
		this.cmdInput = input;
	}

	@Override
	public <T> T getVar(Class<?> clazz) {
		T res = null;
		for(Map.Entry<String, Object> entry : context.entrySet()){
			if(clazz.isAssignableFrom(entry.getValue().getClass())){
				res = (T)entry.getValue();
				break;
			}
		}
		return res;
	}

}

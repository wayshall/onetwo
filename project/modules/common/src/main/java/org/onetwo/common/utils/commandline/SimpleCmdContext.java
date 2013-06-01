package org.onetwo.common.utils.commandline;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

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

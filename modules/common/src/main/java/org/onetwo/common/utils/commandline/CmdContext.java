package org.onetwo.common.utils.commandline;

import java.io.BufferedReader;

public interface CmdContext {
	
	public Object getVar(Object name);
	
	public <T> T getVar(Class<?> clazz);
	
	public void setVar(Object key, Object value);
	
	public BufferedReader getCmdInput();
	
	public void setCmdInput(BufferedReader input);

}

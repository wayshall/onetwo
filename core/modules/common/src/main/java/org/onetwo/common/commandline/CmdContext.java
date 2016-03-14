package org.onetwo.common.commandline;

import java.io.BufferedReader;
import java.io.IOException;

public interface CmdContext {
	
	public Object getVar(Object name);
	
	public <T> T getVar(Class<?> clazz);
	
	public void setVar(Object key, Object value);
	
	public BufferedReader getCmdInput();
	public String nextInput(String tips, String def, InputValidator...validators) throws IOException;
	
	public void setCmdInput(BufferedReader input);

}

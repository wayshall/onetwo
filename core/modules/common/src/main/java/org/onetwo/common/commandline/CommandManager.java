package org.onetwo.common.commandline;

import java.util.Map;

public interface CommandManager {

	public CommandManager addCommand(Command cmd);

	public Command getCommand(String cmdKey);

//	public String helpDoc();
	public Map<String, Command> getCommands();

}
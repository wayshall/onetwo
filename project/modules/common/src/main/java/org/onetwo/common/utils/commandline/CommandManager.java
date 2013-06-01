package org.onetwo.common.utils.commandline;

public interface CommandManager {

	public CommandManager addCommand(Command cmd);

	public Command getCommand(String cmdKey);

	public String helpDoc();

}
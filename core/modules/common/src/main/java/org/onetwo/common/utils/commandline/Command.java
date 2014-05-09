package org.onetwo.common.utils.commandline;

public interface Command {
	
	public String comdKey();
	
	public String helpDoc();
	
	public void execute(CmdContext context) throws Exception ;
	
	public void setCommandManager(CommandManager commandManager);
	
	public CommandManager getCommandManager();

}

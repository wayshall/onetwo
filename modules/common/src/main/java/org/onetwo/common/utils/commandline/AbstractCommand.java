package org.onetwo.common.utils.commandline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class AbstractCommand implements Command{
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	protected CommandManager commandManager;
	protected String key;
	protected String doc;
	
	
	public AbstractCommand(String key) {
		this.key = key;
	}
	
	
	public AbstractCommand(String key, String doc) {
		super();
		this.key = key;
		this.doc = doc;
	}

	@Override
	public String comdKey() {
		return key;
	}

	@Override
	public void execute(CmdContext context) {
		System.out.println("");
		System.out.println("===========================================");
		doExecute(context);
		System.out.println("===========================================");
		System.out.println("");
	}

	abstract public void doExecute(CmdContext context) ;

	@Override
	public String helpDoc() {
		return doc;
	}

	@Override
	public void setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

}

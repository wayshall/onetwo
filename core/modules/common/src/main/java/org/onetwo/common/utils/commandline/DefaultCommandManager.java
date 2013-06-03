package org.onetwo.common.utils.commandline;

import java.util.LinkedHashMap;
import java.util.Map;

import org.onetwo.common.utils.Assert;


public class DefaultCommandManager implements CommandManager {
	
	public static final String UNKONW_COMMAND = "unknow";
	
	public static class UnknowCommand extends AbstractCommand {
		
		public UnknowCommand(){
			super(UNKONW_COMMAND, "unknow command!");
		}

		@Override
		public void doExecute(CmdContext context) {
			System.out.println("ignore command : " + doc);
		}
		
	};
	
	protected Map<String, Command> commands;
	
	public DefaultCommandManager(){
		commands = new LinkedHashMap<String, Command>();
		addCommand(new UnknowCommand());
	}
	
	public CommandManager addCommand(Command cmd){
		Assert.notNull(cmd);
		Assert.hasLength(cmd.comdKey());
		cmd.setCommandManager(this);
		this.commands.put(cmd.comdKey(), cmd);
		return this;
	}
	
	public Command getCommand(String cmdKey){
		Command cmd = null;
		cmd = this.commands.get(cmdKey);
		if(cmd==null){
//			throw new ServiceException("do not support command: " + cmdKey);
			cmd = commands.get(UNKONW_COMMAND);
		}
		return cmd;
	}
	
	public String helpDoc(){
		StringBuilder help = new StringBuilder("help document : \n");
		if(this.commands==null || this.commands.isEmpty()){
			help.append("no commands!");
			return help.toString();
		}
		for(Map.Entry<String, Command> cmdEntry : this.commands.entrySet()){
			help.append(cmdEntry.getKey()).append(" : ").append(cmdEntry.getValue().helpDoc()).append("\n");
		}
		return help.toString();
	}

	public Map<String, Command> getCommands() {
		return commands;
	}

	public void setCommands(Map<String, Command> commands) {
		this.commands = commands;
	}

}

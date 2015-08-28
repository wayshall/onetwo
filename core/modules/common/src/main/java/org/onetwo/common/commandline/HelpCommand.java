package org.onetwo.common.commandline;

import java.util.Map;


public class HelpCommand extends AbstractCommand{
	
	public static final String NAME = "help";
	
	public HelpCommand(){
		super("help", "");
	}

	@Override
	public void doExecute(CmdContext context) {
		CommandManager mng = this.getCommandManager();
		StringBuilder help = new StringBuilder();
		Map<String, Command> commands = mng.getCommands();
		if(commands==null || commands.isEmpty()){
			help.append("no commands!");
			System.out.println(help.toString());
			return ;
		}
		for(Map.Entry<String, Command> cmdEntry : commands.entrySet()){
			if(cmdEntry.getValue()==this)
				continue;
			help.append(cmdEntry.getKey()).append(" : ").append(cmdEntry.getValue().helpDoc()).append("\n");
		}
		System.out.println(help);
		this.doc = help.toString();
	}

}

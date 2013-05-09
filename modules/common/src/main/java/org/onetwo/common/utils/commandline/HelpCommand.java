package org.onetwo.common.utils.commandline;

public class HelpCommand extends AbstractCommand{
	
	public HelpCommand(){
		super("help", "help document !");
	}

	@Override
	public void doExecute(CmdContext context) {
		CommandManager mng = this.getCommandManager();
		if(mng!=null){
			this.doc = mng.helpDoc();
		}
		System.out.println(doc);
	}

}

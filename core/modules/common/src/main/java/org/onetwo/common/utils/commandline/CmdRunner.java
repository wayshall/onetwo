package org.onetwo.common.utils.commandline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;

public class CmdRunner {

	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	protected CommandManager cmdManager;
	
	public CmdRunner() {
	}

	/**
	 * main template
	 * @param args
	 * @throws IOException
	 */
	public void run(String[] args) {
		try {
			startAppContext(args);
		} catch (Exception e) {
			logger.error("startAppContext error : " + e.getMessage(), e);
		}
		loadCommand(args);
		initAfterLoadCommand(args);
		onRuning();
	}
	

	protected void startAppContext(String[] args) {
	}
	protected void initAfterLoadCommand(String[] args) {
	}

	protected void loadCommand(String[] args) {
		cmdManager = new DefaultCommandManager();
		cmdManager.addCommand(new ExitCommand());
		cmdManager.addCommand(new HelpCommand());
	}
	
	protected CmdContext createCmdContext(BufferedReader br){
		return new SimpleCmdContext(br);
	}


	protected void onRuning() {
		this.waitForCommand();
	}
	
	protected void waitForCommand() {
		InputStreamReader reader = null;
		try {
			System.out.print("please input > ");
			reader = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(reader);
			String str = null;
			CmdContext cmdContext = createCmdContext(br);
			while ((str = br.readLine()) != null) {
				Command cmd = cmdManager.getCommand(str);
				if(cmd!=null){
					try {
						cmd.execute(cmdContext);
					} catch (Exception e) {
						logger.error("execute command error: " + e.getMessage(), e);
						System.out.println("execute command error : " + e.getMessage()+", see detail in log file!");
					}
					System.out.print("please input > ");
				}
			}

		} catch (IOException e) {
			logger.error("input error : " + e.getMessage(), e);
			System.out.println("input error : " + e.getMessage()+", see detail in log file!");
		} finally{
			System.out.println("goodbye!");
			LangUtils.closeIO(reader);
		}
	}

}

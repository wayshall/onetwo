package org.onetwo.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import com.google.common.collect.Maps;

public class Consoler {
	
	public static Consoler create(BufferedReader consoleReader){
		return new Consoler(consoleReader);
	}
	
	public static interface ConsoleAction {
		public void execute(String in);
	}
	
	public static class ExitAction implements ConsoleAction {
		@Override
		public void execute(String in) {
			System.out.println("system will be exit, goodby!");
			System.exit(0);
		}
	}

	private BufferedReader consoleReader;
	private Map<String, ConsoleAction> cmdMap = Maps.newHashMap();
	private String cmdSplitor = ":";

	public Consoler(BufferedReader consoleReader) {
		super();
		this.consoleReader = consoleReader;
	}
	
	public Consoler executeIf(String in, ConsoleAction action){
		this.cmdMap.put(in, action);
		return this;
	}
	
	public Consoler waitIf(String in, ConsoleAction action){
		this.executeIf(in, action);
		return awaitInput();
//		return waitIf(in, ":", action);
	}
	
	public Consoler awaitInput(){
		try {
			String input = null;
			while((input = consoleReader.readLine())!=null){
				String[] cmds = GuavaUtils.split(input, cmdSplitor);
				ConsoleAction action = cmdMap.get(cmds[0]);
				if(action!=null){
					System.out.println("execute command: " + cmds[0]);
					String value = cmds.length==1?cmds[0]:cmds[1];
					action.execute(value);
				}else{
					System.out.println("no match command: " + input);
				}
			}
		} catch (IOException e) {
			LangUtils.throwBaseException("console error: " + e.getMessage());
		}
		return this;
	}
	
	public Consoler exitIf(String in){
		waitIf(in, new ExitAction());
		return this;
	}

}

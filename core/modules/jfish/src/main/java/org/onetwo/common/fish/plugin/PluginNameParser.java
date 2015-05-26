package org.onetwo.common.fish.plugin;

public class PluginNameParser {
	private final String start;
	private final String end;
	private final int length;
	

	public PluginNameParser() {
		this("[", "]");
	}
	public PluginNameParser(String start, String end) {
		super();
		this.start = start;
		this.end = end;
		this.length = start.length()+end.length();
	}
	
	public boolean isPluginAccess(String name){
		return name.startsWith(start);
	}
	public String getPluginName(String name){
		int startIndex = name.indexOf(start);
		if(startIndex==-1)
			return "";
		int endIndex = name.indexOf(end, startIndex+1);
		if(endIndex==-1)
			return "";
		String pname = name.substring(startIndex+1, endIndex);
		return pname;
	}
	/***
	 * [pluginName]viewpath1/viewpath2
	 * @param name
	 * @param viewName
	 * @return
	 */
	public String wrapViewPath(String name, String viewName){
		return start + name + end + viewName;
	}
	
	public String getPluginBasePath(String name){
		return start+name+end;
	}
	
	public int getLength(){
		return length;
	}

	public String getStart() {
		return start;
	}

	public String getEnd() {
		return end;
	}
}

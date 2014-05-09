package org.onetwo.common.ds;

public class SwitcherInfo {

	public static enum Type {
		Datasource,
		TransactionManager
	}
	public static final String CURRENT_SWITCHER_INFO = "CURRENT_SWITCHER_INFO";
	public static final String DEFAULT_SWITCHER_NAME = "master";
	
	public static final SwitcherInfo DEFAULT_INFO = new SwitcherInfo(DEFAULT_SWITCHER_NAME);

	private final String currentSwitcherName;
	private final Type type;
	
	public SwitcherInfo(String currentSwitcherName) {
		this(currentSwitcherName, Type.Datasource);
	}
	
	public SwitcherInfo(String currentDatasourceName, Type type) {
		super();
		this.currentSwitcherName = currentDatasourceName;
		this.type = type;
	}

	public String getCurrentSwitcherName() {
		return currentSwitcherName;
	}

	public Type getType() {
		return type;
	}
	
	
}

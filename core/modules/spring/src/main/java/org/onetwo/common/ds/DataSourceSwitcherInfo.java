package org.onetwo.common.ds;

public class DataSourceSwitcherInfo {

	public static final String CURRENT_DATASOURCE_KEY = "CURRENT_DATASOURCE";
	
	public static final DataSourceSwitcherInfo DEFAULT_INFO = new DataSourceSwitcherInfo("master");

	private final String currentDatasourceName;
	
	public DataSourceSwitcherInfo(String currentDatasourceName) {
		super();
		this.currentDatasourceName = currentDatasourceName;
	}

	public String getCurrentDatasourceName() {
		return currentDatasourceName;
	}
	
	
}

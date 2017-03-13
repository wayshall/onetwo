package org.onetwo.dbm.spring;

public class EnableDbmAttributes {
	
	private String dataSourceName;
	private String[] packagesToScan;
	public EnableDbmAttributes(String dataSourceName, String[] packagesToScan) {
		super();
		this.dataSourceName = dataSourceName;
		this.packagesToScan = packagesToScan;
	}
	public String getDataSourceName() {
		return dataSourceName;
	}
	public String[] getPackagesToScan() {
		return packagesToScan;
	}
	
	

}

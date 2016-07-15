package org.onetwo.plugins.admin.vo;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class DictionaryList extends ArrayList<DictTypeInfo> {

	private String scanEnumPackages;

	public String getScanEnumPackages() {
		return scanEnumPackages;
	}

	public void setScanEnumPackages(String scanEnumPackages) {
		this.scanEnumPackages = scanEnumPackages;
	}
	
	
}

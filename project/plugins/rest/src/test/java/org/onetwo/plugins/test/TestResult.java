package org.onetwo.plugins.test;

import org.onetwo.plugins.rest.BaseRestResult;
import org.onetwo.plugins.rest.TestData;

public class TestResult extends BaseRestResult {

	private TestData data;

	public TestData getData() {
		return data;
	}

	public void setData(TestData data) {
		this.data = data;
	}
	
}

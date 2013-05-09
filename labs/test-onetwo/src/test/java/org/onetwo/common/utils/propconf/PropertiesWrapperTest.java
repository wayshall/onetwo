package org.onetwo.common.utils.propconf;

import java.util.Properties;

import org.junit.Test;

public class PropertiesWrapperTest {

	
	@Test
	public void testPropertiesWrapper(){
		Properties prop = PropUtils.loadProperties("sql/query.sql");
		PropertiesWraper wrapper = new PropertiesWraper(prop);
		for(String str : wrapper.sortedKeys()){
			System.out.println(str);
		}
	}

}

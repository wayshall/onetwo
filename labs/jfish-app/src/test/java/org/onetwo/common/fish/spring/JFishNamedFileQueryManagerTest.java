package org.onetwo.common.fish.spring;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.LangUtils;

public class JFishNamedFileQueryManagerTest {
	
	@Test
	public void testFileQueryImpl() throws Exception{
		JFishNamedFileQueryManagerImpl fq = new JFishNamedFileQueryManagerImpl("mysql", false);
		fq.build();
		JFishNamedFileQueryInfo info = fq.getNamedQueryInfo("searchPageListSupplier_json");
		LangUtils.println("info: "+info);
		Assert.assertNotNull(info);
		
		info = fq.getNamedQueryInfo("searchPageListSupplier_json2");
		LangUtils.println("info2: "+info);
		Assert.assertNotNull(info);
	}

}

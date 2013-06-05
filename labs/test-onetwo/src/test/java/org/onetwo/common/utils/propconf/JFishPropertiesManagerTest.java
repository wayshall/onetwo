package org.onetwo.common.utils.propconf;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.propconf.AbstractPropertiesManager.JFishPropertyConf;
import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;
import org.onetwo.common.utils.propconf.NamespacePropertiesManagerImpl.NamespaceProperties;

public class JFishPropertiesManagerTest{
	
	static class SubJFishPropertiesManager extends NamespacePropertiesManagerImpl<JFishPropertyInfo> {

		public SubJFishPropertiesManager() {
			super(new JFishPropertyConf(){
				{
					setDir("sql");
					setOverrideDir("mysql");
					setPostfix(".sql");
				}
			});
		}
		
	}
	private NamespacePropertiesManagerImpl<JFishPropertyInfo> pm = null;
	
	@Test
	public void testFile(){
		pm = new NamespacePropertiesManagerImpl<JFishPropertyInfo>(new JFishPropertyConf(){
			{
				setDir("sql");
				setOverrideDir("mysql");
				setPropertyBeanClass(JFishPropertyInfo.class);
				setPostfix(".sql");
			}
		});
		pm.setDebug(true);
		pm.build();
		
		String name = "UcUserDao.find.all";
		JFishPropertyInfo jpi = pm.getJFishProperty(name);
		Assert.assertNotNull(jpi);
		Assert.assertEquals(name, jpi.getName());
		Assert.assertEquals("select t.*, t.rowid from uc_user t order by t.create_time desc", jpi.getValue());
		
		String ns = "org.onetwo.test";
		NamespaceProperties<JFishPropertyInfo> nsproperties = pm.getNamespaceProperties(ns);
		Assert.assertNotNull(nsproperties);
		Assert.assertEquals(ns, nsproperties.getNamespace());
		Assert.assertEquals(ns+".findAll", nsproperties.getNamedProperty("findAll").getFullName());
		Assert.assertEquals(2, nsproperties.getNamedProperties().size());
	}
	
	@Test
	public void testFile2(){
		pm = new SubJFishPropertiesManager();
		pm.setDebug(true);
		pm.build();
		
		String name = "UcUserDao.find.10";
		JFishPropertyInfo jpi = pm.getJFishProperty(name);
		Assert.assertNotNull(jpi);
		Assert.assertEquals(name, jpi.getName());
		Assert.assertEquals("select * from ( select t.*, t.rowid from uc_user t ) where rownum <= 10", jpi.getValue());
	}
	

}

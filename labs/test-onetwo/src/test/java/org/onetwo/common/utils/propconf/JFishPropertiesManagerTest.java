package org.onetwo.common.utils.propconf;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.propconf.JFishPropertiesManagerImpl.JFishPropertyConf;

public class JFishPropertiesManagerTest{
	
	static class SubJFishPropertiesManager extends JFishPropertiesManagerImpl<JFishPropertyInfo> {

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
	private JFishPropertiesManager<JFishPropertyInfo> pm = null;
	
	@Test
	public void testFile(){
		pm = new JFishPropertiesManagerImpl<JFishPropertyInfo>(new JFishPropertyConf(){
			{
				setDir("sql");
				setOverrideDir("mysql");
				setPropertyBeanClass(JFishPropertyInfo.class);
				setPostfix(".sql");
			}
		});
		
		pm.build();
		
		String name = "UcUserDao.find.all";
		JFishPropertyInfo jpi = pm.getJFishProperty(name);
		Assert.assertNotNull(jpi);
		Assert.assertEquals(name, jpi.getName());
		Assert.assertEquals("select t.*, t.rowid from uc_user t order by t.create_time desc", jpi.getValue());
	}
	
	@Test
	public void testFile2(){
		pm = new SubJFishPropertiesManager();
		pm.build();
		
		String name = "UcUserDao.find.10";
		JFishPropertyInfo jpi = pm.getJFishProperty(name);
		Assert.assertNotNull(jpi);
		Assert.assertEquals(name, jpi.getName());
		Assert.assertEquals("select * from ( select t.*, t.rowid from uc_user t ) where rownum <= 10", jpi.getValue());
	}

}

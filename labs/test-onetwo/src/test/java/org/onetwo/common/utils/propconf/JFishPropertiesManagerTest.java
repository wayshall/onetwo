package org.onetwo.common.utils.propconf;

import java.io.FileOutputStream;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.propconf.AbstractPropertiesManager.JFishPropertyConf;

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
	
	@Test
	public void testReloadFile() throws Exception{

		pm = new NamespacePropertiesManagerImpl<JFishPropertyInfo>(new JFishPropertyConf(){
			{
				setDir("sql");
				setOverrideDir("mysql");
				setPropertyBeanClass(JFishPropertyInfo.class);
				setPostfix(".sql");
				setWatchSqlFile(true);
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
		String findAllSql = "select t.*, t.rowid from uc_user t order by t.create_time desc";
		String sqlKey = "findAll";
		String file = "sql/"+ns+".jfish.sql";
		
		NamespaceProperties<JFishPropertyInfo> nsproperties = pm.getNamespaceProperties(ns);
		Assert.assertNotNull(nsproperties);
		Assert.assertEquals(ns, nsproperties.getNamespace());
		Assert.assertEquals(ns+"."+sqlKey, nsproperties.getNamedProperty("findAll").getFullName());
		Assert.assertEquals(findAllSql, nsproperties.getNamedProperty(sqlKey).getSql());
		Assert.assertEquals(2, nsproperties.getNamedProperties().size());
		
		String addSql = " group by t.id";
		Properties prop = PropUtils.loadProperties(file);
//		System.out.println(prop);
		prop.setProperty("@"+sqlKey, findAllSql+addSql);
		String pfile = FileUtils.getResourcePath(file);
		System.out.println("pfile: " + pfile);
		prop.store(new FileOutputStream(FileUtils.getResourcePath(file)), "");
		
		LangUtils.await(3);

		nsproperties = pm.getNamespaceProperties(ns);
		Assert.assertNotNull(nsproperties);
		Assert.assertEquals(ns, nsproperties.getNamespace());
		Assert.assertEquals(ns+"."+sqlKey, nsproperties.getNamedProperty("findAll").getFullName());
		Assert.assertEquals(findAllSql+addSql, nsproperties.getNamedProperty(sqlKey).getSql());
		Assert.assertEquals(2, nsproperties.getNamedProperties().size());

		//为了能重复测试，把文件修改为原来
		prop.setProperty("@"+sqlKey, findAllSql);
		prop.store(new FileOutputStream(FileUtils.getResourcePath(file)), "");
		
	}
	

}

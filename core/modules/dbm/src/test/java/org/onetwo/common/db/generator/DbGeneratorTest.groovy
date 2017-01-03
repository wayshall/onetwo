package org.onetwo.common.db.generator

import javax.sql.DataSource

import org.junit.Assert
import org.junit.Test
import org.onetwo.common.db.generator.dialet.DatabaseMetaDialet
import org.onetwo.common.db.generator.dialet.MysqlMetaDialet
import org.onetwo.common.db.generator.ftl.FtlDbGenerator
import org.onetwo.common.db.generator.meta.TableMeta
import org.onetwo.common.file.FileUtils
import org.onetwo.common.utils.LangUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests

@ContextConfiguration(value="classpath:/db/generator/db-generator-test.xml")
class DbGeneratorTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	private DataSource dataSource;
	
	@Test
	def void test(){
		println "hello world"
		Assert.assertNotNull(dataSource)
	}
	
	@Test
	def void testDatabaseMeta(){
		DatabaseMetaDialet dialet = new MysqlMetaDialet(dataSource)
		List<String> tables = dialet.getTableNames()
		println "tables:${tables}"
		Assert.assertNotNull(tables)
		Assert.assertTrue(!tables.isEmpty())
		
		String tablename = "admin_user"
		TableMeta tableMeta = dialet.getTableMeta(tablename);
		println "table: ${tableMeta}"
		Assert.assertTrue(tableMeta.getColumns().size()>3);
	}
	
	@Test
	def void testGenerator2(){
		def basePath = FileUtils.getResourcePath("");
		
		List<GeneratedResult<String>> gr = FtlDbGenerator.newGenerator(dataSource)
				//										.templateEngine(new FtlEngine())
														.mysql()
														.stripTablePrefix("admin_")
														//.stripTablePrefix("zyt_estate_")
														.globalConfig()
															.pageFileBaseDir($/D:\mydev\java\workspace\bitbucket\onetwo\core\plugins\web-admin\src\main\resources\META-INF\resources\webftls\web-admin/$)
															.resourceDir($/D:\mydev\java\workspace\bitbucket\onetwo\core\plugins\web-admin\src\main\resources\META-INF\resources/$)
															.javaSrcDir($/D:\mydev\java\workspace\bitbucket\onetwo\core\plugins\web-admin\src\main\java/$)
															.javaBasePackage("org.onetwo.plugins")
															.moduleName("admin")
															.defaultTableContexts()
//																.stripTablePrefix("zyt_estate_")
															.end()
														.end()
														.table("admin_application")
//														.table("")
															.pageTemplate("${basePath}/db/generator/product/index.html.ftl")
															.pageTemplate("${basePath}/db/generator/product/edit-form.html.ftl")
															.controllerTemplate("controller", "${basePath}/db/generator/product/Controller.java.ftl")
															.serviceImplTemplate("${basePath}/db/generator/product/ServiceImpl.java.ftl")
															.entityTemplate("entity", "${basePath}/db/generator/product/Entity.java.ftl", ".java")
															/*.daoTemplate("${basePath}/db/generator/datagrid/Dao.java.ftl")
															.entityTemplate("${basePath}/db/generator/datagrid/ExtEntity.java.ftl")
															.mybatisDaoXmlTemplate("${basePath}/db/generator/datagrid/Dao.xml.ftl")*/
														.end()
														.generate(LangUtils.asMap());
		println "gr:${gr}"
	}

}

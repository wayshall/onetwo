package org.onetwo.common.db.generator.emall

import java.sql.Types;

import javax.sql.DataSource

import org.junit.Assert
import org.junit.Test
import org.onetwo.common.db.generator.GeneratedResult
import org.onetwo.common.db.generator.dialet.DatabaseMetaDialet
import org.onetwo.common.db.generator.dialet.OracleMetaDialet
import org.onetwo.common.db.generator.ftl.FtlDbGenerator
import org.onetwo.common.db.generator.meta.TableMeta
import org.onetwo.common.file.FileUtils
import org.onetwo.common.utils.LangUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests

@ContextConfiguration(value="classpath:/db/generator/emall/emall-generator-test.xml")
class EmallGeneratorTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	private DataSource dataSource;
	
	
//	@Test
	def void testDatabaseMeta(){
		DatabaseMetaDialet dialet = new OracleMetaDialet(dataSource)
		List<String> tables = dialet.getTableNames()
		println "tables:${tables}"
		Assert.assertNotNull(tables)
		Assert.assertTrue(!tables.isEmpty())
		
		String tablename = "EM_ACTIVITY"
		TableMeta tableMeta = dialet.getTableMeta(tablename);
		println "table: ${tableMeta}"
		Assert.assertTrue(tableMeta.getColumns().size()>3);
	}
	
	@Test
	def void testGenerator2(){
		def basePath = FileUtils.getResourcePath("");
		
		List<GeneratedResult<String>> gr = FtlDbGenerator.newGenerator(dataSource)
				//										.templateEngine(new FtlEngine())
														.oracle()
//														.columnMapping(Types.NUMERIC).javaType(Long.class).endColumMapping()
//														.stripTablePrefix("em_")
														//.stripTablePrefix("zyt_estate_")
														.globalConfig()
															.pageFileBaseDir($/D:\mydev\java\yooyo-workspace\v5\emall-activity-manager\src\main\webapp\WEB-INF\pages/$)
															.resourceDir($/D:\mydev\java\yooyo-workspace\v5\emall-activity-manager\src\main\resources/$)
															.javaSrcDir($/D:\mydev\java\yooyo-workspace\v5\emall-activity-manager\src\main\java/$)
															.javaBasePackage("com.yooyo.emall")
//															.moduleName("admin")
															.moduleName("activity")
															.defaultTableContexts()
//																.stripTablePrefix("zyt_estate_")
															.end()
														.end()
//														.table("zyt_estate_rental_house")
														.table("EM_ACTIVITY_TAGS")
															.meta()
															.column("id").javaType(Long.class).end()
														.end()
//														.table("")
//															.pageTemplate("${basePath}/db/generator/emall/index.jsp.ftl")
//															.pageTemplate("${basePath}/db/generator/emall/edit.jsp.ftl")
//															.pageTemplate("${basePath}/db/generator/emall/new.jsp.ftl")
//															.pageTemplate("${basePath}/db/generator/emall/edit-form.jsp.ftl")
//															.controllerTemplate("controller", "${basePath}/db/generator/emall/Controller.java.ftl")
//															.serviceImplTemplate("${basePath}/db/generator/emall/ServiceImpl.java.ftl")
															/*.daoTemplate("${basePath}/db/generator/datagrid/Dao.java.ftl")
															.entityTemplate("${basePath}/db/generator/datagrid/ExtEntity.java.ftl")
															.mybatisDaoXmlTemplate("${basePath}/db/generator/datagrid/Dao.xml.ftl")*/
														.end()
														.generate(LangUtils.asMap());
		println "gr:${gr}"
	}

}

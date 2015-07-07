package org.onetwo.common.db.generator

import java.sql.Types

import javax.sql.DataSource

import org.junit.Assert
import org.junit.Test
import org.onetwo.common.db.generator.dialet.DatabaseMetaDialet
import org.onetwo.common.db.generator.dialet.MysqlMetaDialet
import org.onetwo.common.db.generator.ftl.FtlDbGenerator
import org.onetwo.common.db.generator.meta.TableMeta
import org.onetwo.common.utils.FileUtils
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
	def void testGenerator(){
		def basePath = FileUtils.getResourcePath("");
		
		def table = "zyt_estate";
		GenerateContext context = new GenerateContext();
		List<GeneratedResult<String>> gr = FtlDbGenerator.newGenerator(dataSource)
				//										.templateEngine(new FtlEngine())
														.mysql()
														.columnMapping(Types.DATE)
															.javaType(Date.class)
															.attr("cssClass", "datebox")
														.endColumMapping()
														.columnMapping(Types.TIMESTAMP)
															.javaType(Date.class)
															.attr("cssClass", "datetimebox")
														.endColumMapping()
														.tables(table)
														.generateConfig("${basePath}/db/generator/html-template.html.ftl",
															$/D:\mydev\java\yooyo-workspace\zhiyetong-manager\src\main\resources\templates\resourcemgr/$)
														.generate(context);
		println "gr:${gr}"
	}

}

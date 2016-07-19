package org.onetwo.common.db.generator.product

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

@ContextConfiguration(value="classpath:/db/generator/product/product-generator-test.xml")
class ProductGeneratorTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	private DataSource dataSource;
	
	
//	@Test
	def void testDatabaseMeta(){
		DatabaseMetaDialet dialet = new OracleMetaDialet(dataSource)
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
//														.columnMapping(Types.NUMERIC).javaType(Long.class).endColumMapping()
//														.stripTablePrefix("em_")
														//.stripTablePrefix("zyt_estate_")
														.globalConfig()
															.pageFileBaseDir($/D:\mydev\java\workspace\bitbucket\product-manager\src\main\resources\templates/$)
															.resourceDir($/D:\mydev\java\workspace\bitbucket\product-manager\src\main\resources/$)
															.javaSrcDir($/D:\mydev\java\workspace\bitbucket\product-manager\src\main\java/$)
															.javaBasePackage("projects")
//															.moduleName("admin")
															.moduleName("manager")
															.defaultTableContexts()
//																.stripTablePrefix("zyt_estate_")
															.end()
														.end()
//														.table("zyt_estate_rental_house")
														.table("product_income")
															.meta()
															.column("id").javaType(Long.class).end()
														.end()
//														.table("")
															.pageTemplate("${basePath}/db/generator/product/index.html.ftl")
															.pageTemplate("${basePath}/db/generator/product/edit-form.html.ftl")
															.controllerTemplate("controller", "${basePath}/db/generator/product/Controller.java.ftl")
															.entityTemplate("entity", "${basePath}/db/generator/product/Entity.java.ftl", ".java")
															.serviceImplTemplate("${basePath}/db/generator/product/ServiceImpl.java.ftl")
														.end()
														.generate(LangUtils.asMap());
		println "gr:${gr}"
	}

}

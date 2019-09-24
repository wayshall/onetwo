package org.onetwo.boot.module.swagger;

import org.junit.Test;
import org.onetwo.common.db.generator.DbmGenerator;

/**
 * @author wayshall
 * <br/>
 */
public class SwaggerEntityDbmGeneratorTest {
	
	@Test
	public void generateCode(){
		String jdbcUrl = "jdbc:mysql://596f0c823c306.gz.cdb.myqcloud.com:5034/neo?&useSSL=false&characterEncoding=UTF-8";
		String username = "neodb";
		String password = "db@neo";
		
		DbmGenerator.createWithDburl(jdbcUrl, username, password)
					.javaBasePackage("org.onetwo.boot.module.swagger")//基础包名
					.stripTablePrefix("api_")
					.mavenProjectDir()
					.webadminGenerator("api_swagger")//要生成的表名
						.generateEntity()
						.generateServiceImpl()
					.end()
					.webadminGenerator("api_swagger_model")//要生成的表名
						.generateEntity()
						.generateServiceImpl()
					.end()
					.webadminGenerator("api_swagger_operation")//要生成的表名
						.generateEntity()
						.generateServiceImpl()
					.end()
					.webadminGenerator("api_swagger_parameter")//要生成的表名
						.generateEntity()
						.generateServiceImpl()
					.end()
					.webadminGenerator("api_swagger_response")//要生成的表名
						.generateEntity()
						.generateServiceImpl()
					.end()
					.generate();//生成文件
	}
	
}

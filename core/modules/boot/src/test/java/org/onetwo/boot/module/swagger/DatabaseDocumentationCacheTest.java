package org.onetwo.boot.module.swagger;

import static org.assertj.core.api.Assertions.assertThat;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.boot.module.swagger.entity.SwaggerFileEntity;
import org.onetwo.common.spring.SpringUtils;

import springfox.documentation.service.Documentation;

/**
 * @author wayshall
 * <br/>
 */
public class DatabaseDocumentationCacheTest {
	DatabaseDocumentationCache databaseDocumentationCache;
	String content = "";
	
	@Before
	public void setup(){
		content = SpringUtils.readClassPathFile("data/swagger.json");
	}
	
	@Test
	public void testReadJson(){
		SwaggerParser parser = new SwaggerParser();
		Swagger swagger = parser.read("data/swagger.json");
		System.out.println("swagger:"+swagger);
	}
	
	
	@Test
	public void testParseDocumentation(){
		System.out.println("content:"+content);
		assertThat(content).isNotNull();
		
		databaseDocumentationCache = new DatabaseDocumentationCache();
		SwaggerFileEntity fileEntity = new SwaggerFileEntity();
		fileEntity.setContent(content);
		Documentation doc = databaseDocumentationCache.parseDocumentation(fileEntity);
		System.out.println("doc:"+doc);
		assertThat(doc).isNotNull();
	}

}

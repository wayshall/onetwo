package org.onetwo.boot.module.swagger;

import java.util.List;

import org.onetwo.boot.module.swagger.entity.SwaggerFileEntity;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;

import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;

/**
 * @author wayshall <br/>
 */
public class DatabaseDocumentationCache extends DocumentationCache {
	
	@Autowired
	private BaseEntityManager baseEntityManager;
	private JsonMapper jsonMapper = JsonMapper.ignoreNull();

	public void loadFromDatasource(){
		List<SwaggerFileEntity> files = Querys.from(baseEntityManager, SwaggerFileEntity.class)
											  .where()
											  	.field("status").equalTo(SwaggerFileEntity.Status.ENABLED)
											  .end()
											  .toQuery()
											  .list();
		if(LangUtils.isEmpty(files)){
			return ;
		}
		for(SwaggerFileEntity file : files){
			Documentation doc = parseDocumentation(file);
			this.addDocumentation(doc);
		}
	}
	
	protected Documentation parseDocumentation(SwaggerFileEntity fileEntity){
		Documentation doc = jsonMapper.fromJson(fileEntity.getContent(), Documentation.class);
		return doc;
	}
}

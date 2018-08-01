package org.onetwo.boot.module.swagger.service.impl;

import io.swagger.models.Swagger;
import io.swagger.parser.Swagger20Parser;

import java.io.IOException;
import java.util.List;

import org.onetwo.boot.module.swagger.entity.SwaggerFileEntity;
import org.onetwo.boot.module.swagger.entity.SwaggerFileEntity.Status;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wayshall
 * <br/>
 */
@Transactional
public class DatabaseSwaggerResourceService {
	
	@Autowired
	private BaseEntityManager baseEntityManager;
	@Autowired
	private SwaggerServiceImpl swaggerService;
	
	/***
	 * 通过分组名称查找
	 * @author wayshall
	 * @param groupName
	 * @return
	 */
	public SwaggerFileEntity findByGroupName(String groupName){
		SwaggerFileEntity file = baseEntityManager.findOne(SwaggerFileEntity.class, "fileName", groupName);
		return file;
	}

	public List<SwaggerFileEntity> findAllEnabled(){
		List<SwaggerFileEntity> files = Querys.from(baseEntityManager, SwaggerFileEntity.class)
											  .where()
											  	.field("status").equalTo(SwaggerFileEntity.Status.ENABLED)
											  .end()
											  .toQuery()
											  .list();
		return files;
	}
	
	public SwaggerFileEntity saveSwaggerFile(MultipartFile swaggerFile, Swagger swagger, String content){
		SwaggerFileEntity swaggerFileEntity = new SwaggerFileEntity();
		swaggerFileEntity.setFileName(swaggerFile.getOriginalFilename());
		swaggerFileEntity.setApplicationName(swagger.getInfo().getTitle());
		swaggerFileEntity.setStatus(Status.ENABLED);
		swaggerFileEntity.setContent(content);
		baseEntityManager.save(swaggerFileEntity);
		return swaggerFileEntity;
	}
	
	public void importSwagger(MultipartFile swaggerFile){
		String content = SpringUtils.readMultipartFile(swaggerFile);
		Swagger swagger;
		try {
			swagger = new Swagger20Parser().parse(content);
		} catch (IOException e) {
			throw new BaseException("parse Swagger file error: " + swaggerFile.getOriginalFilename());
		}
		SwaggerFileEntity file = saveSwaggerFile(swaggerFile, swagger, content);
		
		this.swaggerService.save(file.getId(), swagger);
	}

}

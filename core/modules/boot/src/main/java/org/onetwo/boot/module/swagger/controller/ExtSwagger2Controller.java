package org.onetwo.boot.module.swagger.controller;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.boot.module.swagger.entity.SwaggerFileEntity;
import org.onetwo.boot.module.swagger.service.DatabaseSwaggerResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;
import springfox.documentation.swagger2.web.Swagger2Controller;

/**
 * @author wayshall <br/>
 */
public class ExtSwagger2Controller extends Swagger2Controller {
//	private static final String HAL_MEDIA_TYPE = "application/hal+json";
	
	@Autowired
	private DatabaseSwaggerResourceService swaggerResourceService;

	public ExtSwagger2Controller(Environment environment,
			DocumentationCache documentationCache,
			ServiceModelToSwagger2Mapper mapper, JsonSerializer jsonSerializer) {
		super(environment, documentationCache, mapper, jsonSerializer);
	}

	@Override
	public ResponseEntity<Json> getDocumentation(@RequestParam(value = "group", required = false) String swaggerGroup, HttpServletRequest servletRequest) {
		ResponseEntity<Json> res = super.getDocumentation(swaggerGroup, servletRequest);
		if(res.getStatusCode()!=HttpStatus.NOT_FOUND){
			return res;
		}
		SwaggerFileEntity fileEntity = swaggerResourceService.findByGroupName(swaggerGroup);
		if(fileEntity==null){
			return res;
		}
		res = new ResponseEntity<Json>(new Json(fileEntity.getContent()), HttpStatus.OK);
		return res;
	}

}

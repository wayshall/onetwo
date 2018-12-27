package org.onetwo.boot.plugins.swagger.controller;

import java.util.List;

import org.onetwo.boot.plugins.swagger.service.impl.DatabaseSwaggerResourceService;
import org.onetwo.boot.plugins.swagger.vo.ModuleListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import springfox.documentation.annotations.ApiIgnore;

/**
 * @author weishao zeng <br/>
 */
@Controller
@ApiIgnore
public class ExtApiResourceController {

	@Autowired
	private DatabaseSwaggerResourceService swaggerResourceService;
	  
	public ExtApiResourceController() {
	}

	@ResponseBody
	@RequestMapping("/module-resources")
	public ResponseEntity<List<ModuleListResponse>> moduleResources() {
		return new ResponseEntity<List<ModuleListResponse>>(swaggerResourceService.findModuleSwaggerResource(true), HttpStatus.OK);
	}
}

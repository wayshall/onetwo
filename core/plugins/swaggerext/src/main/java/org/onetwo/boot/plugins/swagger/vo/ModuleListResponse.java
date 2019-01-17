package org.onetwo.boot.plugins.swagger.vo;

import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.swagger.web.SwaggerResource;

/**
 * @author wayshall
 * <br/>
 */
public class ModuleListResponse extends SwaggerResource {
	
	@ApiModelProperty(value="模块id")
	String moduleId;

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

}

package org.onetwo.common.apiclient.api.simple;

import java.util.List;

import org.onetwo.common.apiclient.annotation.RestApiClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@RestApiClient(url="https://test.micro-cloud-tech.cn")
public interface SwaggerClient {
	
	@RequestMapping(value="/service/neo/swagger-resources.json", method=RequestMethod.GET)
	List<SwaggerResponse> getGroups();

	@Data
	static public class SwaggerResponse {

		private String name;
		private String location;
		private String swaggerVersion;
	}

}


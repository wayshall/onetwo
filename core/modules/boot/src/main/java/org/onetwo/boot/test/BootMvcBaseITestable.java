package org.onetwo.boot.test;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public interface BootMvcBaseITestable {

	default MockMvc buildMockMvc(WebApplicationContext webApplicationContext){
		return MockMvcBuilders.webAppContextSetup(webApplicationContext)
								.build();
	}

	default ResultActions perform(RequestBuilder requestBuilder){
		try {
			return mockMvc().perform(requestBuilder);
		} catch (Exception e) {
			throw new RuntimeException("mockMvc perform error: "+e.getMessage(), e);
		}
	}
	
	MockMvc mockMvc();

}

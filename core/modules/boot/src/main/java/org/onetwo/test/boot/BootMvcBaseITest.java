package org.onetwo.test.boot;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class BootMvcBaseITest {

	@Autowired
	protected WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	
	@Before
	public void initMockMvc(){
		this.mockMvc = buildMockMvc();
	}
	protected MockMvc buildMockMvc(){
		return MockMvcBuilders.webAppContextSetup(webApplicationContext)
								.build();
	}

	protected ResultActions perform(RequestBuilder requestBuilder){
		try {
			return mockMvc().perform(requestBuilder);
		} catch (Exception e) {
			throw new RuntimeException("mockMvc perform error: "+e.getMessage(), e);
		}
	}
	protected MockMvc mockMvc() {
		return mockMvc;
	}

}

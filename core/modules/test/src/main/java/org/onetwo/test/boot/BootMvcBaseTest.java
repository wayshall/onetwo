package org.onetwo.test.boot;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class BootMvcBaseTest {

	@Autowired
	protected WebApplicationContext webApplicationContext;
	protected MockMvc mockMvcs;
	
	@Before
	public void initMockMvc(){
		this.mockMvcs = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.build();
	}
	
}

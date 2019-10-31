package org.onetwo.boot.test;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

public class BootMvcBaseITest implements BootMvcBaseITestable {

	@Autowired
	protected WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	
	@Before
	public void initMockMvc(){
		this.mockMvc = buildMockMvc(webApplicationContext);
	}

	public MockMvc mockMvc() {
		return mockMvc;
	}

}

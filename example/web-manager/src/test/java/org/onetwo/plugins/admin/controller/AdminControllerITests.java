package org.onetwo.plugins.admin.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.onetwo.webapp.manager.ManagerApplicationITests;
import org.springframework.http.MediaType;

//@WithMockUser
public class AdminControllerITests extends ManagerApplicationITests {
	
	@Test
	public void testList() throws Exception{
		mockMvcs.perform(get("/web-admin/user")
							.accept(MediaType.APPLICATION_JSON_UTF8)
							.with(user(login()))
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.total").value(Matchers.greaterThan(0)))
				.andExpect(jsonPath("$.rows").isArray());
	}

}

package org.onetwo.plugins.admin.dao;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.onetwo.webapp.manager.ManagerApplicationTests;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminPermissionDaoTest extends ManagerApplicationTests {
	
	@Autowired
	private AdminPermissionDao adminPermissionDao;
	
	@Test
	public void testFindPermissions(){
		List<String> codes = Arrays.asList("test1", "test2");
		adminPermissionDao.findPermissions(codes);
	}

}

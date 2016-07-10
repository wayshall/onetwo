package org.onetwo.boot.permission.parser;


import org.junit.Assert;
import org.junit.Test;
import org.onetwo.boot.permission.parser.AdminModule.AppRole;
import org.onetwo.ext.permission.AbstractPermissionConfig;
import org.onetwo.ext.permission.parser.DefaultMenuInfoParser;
import org.onetwo.plugins.admin.entity.AdminPermission;

public class DefaultMenuInfoParserTest {
	
	public static class MenuTest extends AbstractPermissionConfig<AdminPermission> {

		@Override
        public Class<?> getRootMenuClass() {
	        return AppwebMenuTest.class;
        }

		@Override
        public String[] getChildMenuPackages() {
	        return null;
        }

		@Override
        public Class<AdminPermission> getIPermissionClass() {
	        return AdminPermission.class;
        }
		
	}

	private DefaultMenuInfoParser<AdminPermission> parser = new DefaultMenuInfoParser<>(new MenuTest());
	
	@Test
	public void testParser(){
		AdminPermission p = this.parser.parseTree();
		Assert.assertEquals(AppwebMenuTest.name, p.getName());
		Assert.assertEquals(AppwebMenuTest.appCode, p.getAppCode());
		

		p = p.getChildrenMenu().get(0);
		Assert.assertEquals(AppwebMenuTest.System.name, p.getName());
		Assert.assertEquals(AppwebMenuTest.System.sort, p.getSort().intValue());
		
		p = p.getChildrenMenu().get(0);
		Assert.assertEquals("AppwebMenuTest_System_AdminModule", p.getCode());
		Assert.assertEquals(AdminModule.name, p.getName());
		
		p = p.getChildrenMenu().get(0);
		Assert.assertEquals(AppRole.name, p.getName());
		Assert.assertEquals(AppRole.sort, p.getSort().intValue());
		
	}
}

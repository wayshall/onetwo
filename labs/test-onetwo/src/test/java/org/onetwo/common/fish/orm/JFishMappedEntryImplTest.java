package org.onetwo.common.fish.orm;

import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.fish.jpa.JPARelatedMappedEntryBuilder;
import org.onetwo.common.fish.orm.AbstractDBDialect.DBMeta;
import org.onetwo.common.utils.LangUtils;

import test.entity.RoleEntity;
import test.entity.UserEntity;

public class JFishMappedEntryImplTest {
	
	private JFishMappedEntryBuilder builder = new JPARelatedMappedEntryBuilder();
	private AbstractJFishMappedEntryImpl userEntry;
	private AbstractJFishMappedEntryImpl roleEntry;
	
	@Before
	public void setup(){
		MySQLDialect dialet = new MySQLDialect(new DefaultDataBaseConfig()){

			@Override
			public DBMeta getDbmeta() {
				DBMeta dm = new DBMeta();
				dm.setDbName("mysql");
				return dm;
			}
			
		};
		dialet.registerIdStrategy();
		dialet.setAutoDetectIdStrategy(true);
		dialet.initOtherComponents();
		
		builder.setDialect(dialet);
		builder.setListenerManager(new MappedEntryBuilderListenerManager(Collections.EMPTY_LIST));
		userEntry = (AbstractJFishMappedEntryImpl) builder.buildMappedEntry(UserEntity.class);
		userEntry.buildEntry();

		roleEntry = (AbstractJFishMappedEntryImpl) builder.buildMappedEntry(RoleEntity.class);
		roleEntry.buildEntry();
	}
	
	@Test
	public void testEntrySql(){
		EntrySQLBuilder updateBuilder = userEntry.getStaticUpdateSqlBuilder();
		String sql = updateBuilder.getSql();
		String exepted = "update T_USER set id = ?, USER_NAME = ?, last_update_time = ?, HEIGHT = ?, status = ?, EMAIL = ?, roles = ?, AGE = ?, create_time = ?, BIRTH_DAY = ? where id = ?";
		Assert.assertEquals(exepted, sql);
		
		sql = userEntry.getStaticInsertSqlBuilder().getSql();
		exepted = "insert into T_USER ( id, USER_NAME, last_update_time, HEIGHT, status, EMAIL, roles, AGE, create_time, BIRTH_DAY ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
		Assert.assertEquals(exepted, sql);
		
		sql = userEntry.getStaticDeleteSqlBuilder().getSql();
		exepted = "delete from T_USER where id = ?";
		Assert.assertEquals(exepted, sql);
		
		sql = userEntry.getStaticFetchSqlBuilder().getSql();
		exepted = "select this_.id, this_.USER_NAME, this_.last_update_time, this_.HEIGHT, this_.status, this_.EMAIL, this_.roles, this_.AGE, this_.create_time, this_.BIRTH_DAY from T_USER this_ where this_.id = ?";
		Assert.assertEquals(exepted, sql);
		
		sql = userEntry.getStaticFetchAllSqlBuilder().getSql();
		exepted = "select this_.id, this_.USER_NAME, this_.last_update_time, this_.HEIGHT, this_.status, this_.EMAIL, this_.roles, this_.AGE, this_.create_time, this_.BIRTH_DAY from T_USER this_";
		Assert.assertEquals(exepted, sql);
		
		sql = userEntry.getStaticDeleteAllSqlBuilder().getSql();
		exepted = "delete from T_USER";
		Assert.assertEquals(exepted, sql);
		

		
		sql = roleEntry.getStaticInsertSqlBuilder().getSql();
		exepted = "insert into t_role ( id, LAST_UPDATE_TIME, name, CREATE_TIME, version ) values ( ?, ?, ?, ?, ? )";
		Assert.assertEquals(exepted, sql);
		
		sql = roleEntry.getStaticUpdateSqlBuilder().getSql();
		exepted = "update t_role set id = ?, LAST_UPDATE_TIME = ?, name = ?, CREATE_TIME = ?, version = ? where id = ? and version = ?";
		Assert.assertEquals(exepted, sql);
		
		/*sql = roleEntry.getStaticDeleteSqlBuilder().getSql();
		System.out.println("sql: " + sql);
		exepted = "delete from t_role where id = ? and version = ?";
		Assert.assertEquals(exepted, sql);*/
	}
	
	@Test
	public void testDynamicEntrySql(){
		String exepted = "";
		String sql = "";
		
		UserEntity user = new UserEntity();
		user.setId(11L);
		user.setUserName("test");
		user.setAge(11);
		JdbcStatementContext<List<Object[]>> context = userEntry.makeDymanicUpdate(user);
		sql = context.getSql();
		exepted = "update T_USER set USER_NAME = ?, last_update_time = ?, AGE = ? where id = ?";
		Assert.assertEquals(exepted, sql);
		

		
		RoleEntity role = new RoleEntity();
		role.setId(11L);
		role.setName("test");
		role.setVersion(11L);
		context = roleEntry.makeDymanicUpdate(role);
		sql = context.getSql();
		Object[] values = context.getValue().get(0);
		exepted = "update t_role set LAST_UPDATE_TIME = ?, name = ?, version = ? where id = ? and version = ?";
		Assert.assertEquals(exepted, sql);
		Assert.assertEquals(role.getName(), values[1]);
		Assert.assertEquals(role.getVersion()+1, values[2]);
		Assert.assertEquals(role.getId(), values[3]);
		Assert.assertEquals(role.getVersion(), values[4]);
	}

}

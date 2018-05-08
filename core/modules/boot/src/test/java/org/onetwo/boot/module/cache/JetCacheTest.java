package org.onetwo.boot.module.cache;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wayshall
 * <br/>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=JetCacheApplication.class,
				properties={
							"key=value"
							}
)
@ActiveProfiles("jetcache-test")
@Rollback
//@Transactional
public class JetCacheTest {
	@Autowired
	DataSource dataSource;
	
	@Autowired
	UserCacheTestService userCacheTestService;
	@Autowired
	BaseEntityManager baseEntityManager;
	
	@Test
	public void testUserCacheTestService() throws Exception{
		String jdbcUrl = dataSource.getConnection().getMetaData().getURL();
		assertThat(jdbcUrl).contains("jdbc:mysql://localhost:3306/jormtest");
		assertThat(userCacheTestService).isNotNull();
	}
	
	@Test
	public void test(){
		userCacheTestService.clearCache(1L);
		//clear
		baseEntityManager.removeById(UserEntity.class, 1L);
		
		UserEntity dbUser = userCacheTestService.findByIdWithCache(1L);
		assertThat(dbUser).isNull();
		
		//save and not update cache
		UserEntity user = new UserEntity();
		user.setId(1L);
		user.setUserName("test-jetcache");
		this.userCacheTestService.save(user);
		
		//find null from cache
		dbUser = userCacheTestService.findByIdWithCache(1L);
		assertThat(dbUser).isNull();
		
		//update cache
		userCacheTestService.updateWithCache(user);
		
		dbUser = userCacheTestService.findByIdWithCache(1L);
		assertThat(dbUser).isEqualTo(user);
		
		//modify and not update cache
		UserEntity upldateUser = new UserEntity();
		upldateUser.setId(1L);
		upldateUser.setUserName("test-jetcache-modify");
		this.userCacheTestService.save(upldateUser);
		
		dbUser = userCacheTestService.findByIdWithCache(1L);
		assertThat(dbUser).isEqualTo(user);
		
		LangUtils.await(5);
		dbUser = userCacheTestService.findByIdWithCache(1L);
		assertThat(dbUser).isEqualTo(upldateUser);
		
		
	}
}

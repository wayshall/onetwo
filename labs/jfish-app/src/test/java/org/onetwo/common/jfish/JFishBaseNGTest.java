package org.onetwo.common.jfish;

import javax.annotation.Resource;

import org.example.app.model.member.entity.AddressEntity;
import org.example.app.model.member.entity.ArticleEntity;
import org.example.app.model.member.entity.UserEntity;
import org.junit.Before;
import org.onetwo.common.fish.spring.JFishDaoImplementor;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.SpringTestNGTestCase;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.propconf.Environment;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;


@ActiveProfiles({ Environment.TEST_LOCAL })
//@ActiveProfiles({ Environment.TEST_LOCAL })
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
@TransactionConfiguration(defaultRollback = false)
public class JFishBaseNGTest extends SpringTestNGTestCase {

	@Resource
	protected ApplicationContext applicationContext;
	
	@Resource
	protected JFishDaoImplementor jdao;
	
	@Before
	public void initSpringApplication(){
		SpringApplication.initApplication(applicationContext);
	}
	
	protected boolean isOracle(){
		return jdao.getDialect().getDbmeta().isOracle();
	}
	
	protected boolean isMySQL(){
		return jdao.getDialect().getDbmeta().isMySQL();
	}
	protected UserEntity createUserEntity(int i, String userName){
		UserEntity user = new UserEntity();
		user.setUserName(userName+i);
		user.setBirthDay(DateUtil.now());
		user.setEmail(i+"username@qq.com");
		user.setHeight(3.3f);
		
		return user;
	}
	
	protected ArticleEntity createArticle(int i, String title){
		ArticleEntity newArticle = new ArticleEntity();
		newArticle.setTitle(title+" title " + i);
		newArticle.setContent(title + " content " + i);
		
		return newArticle;
	}
	
	protected AddressEntity createAddress(int i, String detail){
		AddressEntity address = new AddressEntity();
		address.setDetail(detail+"-"+i);
		address.setPostCode("PostCode-"+i);
		
		return address;
	}
}

package org.onetwo.common.jfish;

import org.example.app.model.member.entity.AddressEntity;
import org.example.app.model.member.entity.ArticleEntity;
import org.example.app.model.member.entity.UserEntity;
import org.junit.Before;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.test.spring.SpringTxJUnitTestCase;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.propconf.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;


@ActiveProfiles(Environment.TEST)
//@ContextConfiguration(locations = { "/applicationContext-test.xml" })
@ContextConfiguration(loader=JFishAppContextLoaderForTest.class)
@TransactionConfiguration(defaultRollback = false)
public class JFishBaseJUnitTest extends SpringTxJUnitTestCase {
	
	@Before
	public void initSpringApplication(){
		SpringApplication.initApplication(applicationContext);
	}


	protected UserEntity createUserEntity(int i, String userName){
		UserEntity user = new UserEntity();
		user.setUserName(userName+i);
		user.setBirthDay(DateUtil.now());
		user.setEmail(i+"username@qq.com");
		user.setHeight(3.3f);
		user.setAge(11);
		
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

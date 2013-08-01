package org.onetwo.common.fish.richmodel;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.fish.JFishEntityManager;
import org.onetwo.common.fish.JFishQueryBuilder;
import org.onetwo.common.jfish.JFishBaseJUnitTest;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;

public class RichModelTest extends JFishBaseJUnitTest  {
	
	private static Date now = new Date();
	private static String dateTag = DateUtil.formatDateTime(now);
	
	@Autowired
	private JFishEntityManager jfishEntityManager;
	
	@Before
	public void setup(){
		int deletecount = jfishEntityManager.getJfishDao().executeUpdate("delete from t_user_role");
		System.out.println("t_user_role deletecount:"+deletecount);
		deletecount = jfishEntityManager.getJfishDao().executeUpdate("delete from t_order_item");
		System.out.println("t_order_item deletecount:"+deletecount);
		UserModel.removeAll();
		ArticleModel.removeAll();
		RoleModel.removeAll();
	}
	

	@Test
//	@Ignore
	public void testCRUD(){
		Assert.assertEquals(UserModel.class, UserModel.getEntityClass());
		
		String userName = "jdbcTestName"+dateTag;
		UserModel newUser = new UserModel();
		newUser.setUserName(userName);
		newUser.setAge(11);
		newUser.setBirthDay(now);
		newUser.save();
		
		JFishQueryBuilder qb = UserModel.where();
		UserModel qu = qb.field("userName").equalTo(userName).one();
		Assert.assertEquals(newUser.getAge(), qu.getAge());
		Assert.assertEquals(newUser.getUserName(), qu.getUserName());
		Assert.assertEquals(DateUtil.formatDateTime(newUser.getBirthDay()), DateUtil.formatDateTime(qu.getBirthDay()));

		UserModel fone = UserModel.findOne("userName", userName);
		Assert.assertNotNull(fone);
		Assert.assertEquals(qu.getId(), fone.getId());

		Assert.assertTrue(11==qu.getAge());
		qu.setAge(28);
		qu.setEmail("update@qq.com");
		qu.save();
		Assert.assertTrue(28==qu.getAge());
		Assert.assertEquals("update@qq.com", qu.getEmail());
		
		qu.remove();
		qu = UserModel.where().field("userName").equalTo("jdbcTestName"+dateTag).one();
		Assert.assertNull(qu);
		
		
		Collection<UserModel> users = RichModelTestUtils.createUserModels(10, "save batch");
		UserModel.batchInsert(users);
		int count = UserModel.count().intValue();
		Assert.assertTrue(count==10);
		
		List<UserModel> userlist = UserModel.findList();
		Assert.assertEquals(count, userlist.size());
		
		Page<UserModel> page = Page.create();
		UserModel.findPage(page);
		Assert.assertEquals(count, page.getSize());
	}
	
	@Test
	public void testRelated(){
		System.out.println("=============testRelated start ");
		Class<?> cls = UserModel.class;
		String userName = "jdbcTestName"+dateTag;
		UserModel newUser = new UserModel();
		newUser.setUserName(userName);
		newUser.setAge(11);
		newUser.setBirthDay(now);
		newUser.save();
		
		Collection<ArticleModel> artlist = RichModelTestUtils.createArticles(10, "related article");
		newUser.setArticles(artlist);
		
		newUser.saveWith("articles");
		
		UserModel quser = UserModel.findOne("userName", userName);
		Assert.assertNotNull(quser);
		List<ArticleModel> cascadeArticles = quser.cascade("articles").list();
		Assert.assertNotNull(cascadeArticles);
		Assert.assertEquals(artlist.size(), cascadeArticles.size());
		

		cascadeArticles = quser.cascade("articles").where().field("title").equalTo("1related article").list();
		Assert.assertNotNull(cascadeArticles);
		Assert.assertEquals(1, cascadeArticles.size());
		
		ArticleModel article = cascadeArticles.get(0);
		Assert.assertNotNull(article.getAuthor());
		Assert.assertNotNull(article.getAuthor().getId());
		Assert.assertNull(article.getAuthor().getUserName());
		
		UserModel cascadeUser = article.cascade("author").one();
		Assert.assertNotNull(cascadeUser);
		Assert.assertEquals(userName, cascadeUser.getUserName());
	}
	
	@Test
	public void testUserRoles(){
		UserModel newUser = new UserModel();
		newUser.setUserName("testUserName");
		newUser.setBirthDay(now);
		
		newUser.getRoles().add(new RoleModel("user"));
		newUser.getRoles().add(new RoleModel("admin"));
		
		newUser.saveWith("roles");
		
		UserModel newUser2 = new UserModel();
		newUser2.setUserName("new user2");
		newUser2.getRoles().add(new RoleModel("admin2"));
		newUser2.saveWith("roles");
		
		UserModel queryUser = UserModel.findById(newUser.getId());
		Collection<RoleModel> queryRoles = queryUser.getCascadeRoles();
		Assert.assertNotNull(queryUser);
		Assert.assertEquals(newUser.getUserName(), queryUser.getUserName());
		Assert.assertEquals(newUser.getRoles().size(), queryRoles.size());
	}
}

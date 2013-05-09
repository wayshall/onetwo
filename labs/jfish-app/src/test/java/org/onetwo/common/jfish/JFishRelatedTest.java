package org.onetwo.common.jfish;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.example.app.model.member.entity.AddressEntity;
import org.example.app.model.member.entity.ArticleEntity;
import org.example.app.model.member.entity.ItemEntity;
import org.example.app.model.member.entity.OrderEntity;
import org.example.app.model.member.entity.UserEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.fish.JFishEntityManager;
import org.onetwo.common.fish.exception.JFishEntityNotSavedException;
import org.onetwo.common.fish.spring.JFishDaoImpl;

public class JFishRelatedTest extends JFishBaseJUnitTest {
	public static final String JFISH_RELATED_CRUD = "JFishRelatedTest.crud";
	public static final String JFISH_RELATED_QUERY = "JFishRelatedTest.query";

	@Resource
	private JFishDaoImpl jdao;
	@Resource
	private JFishEntityManager jem;
	
//	ArticleEntity article;
//	UserEntity user;

	@Before
	public void setup(){
		
		jdao.insert(createAddress(100, "扰乱插入"));
		jdao.insert(createArticle(100, "扰乱插入"));
		jdao.insert(createUserEntity(100, "扰乱插入"));
	}
	
	protected void clearTables(){
		jdao.deleteAll(UserEntity.class);
		jdao.deleteAll(ArticleEntity.class);
		jdao.deleteAll(AddressEntity.class);
		jdao.deleteAll(OrderEntity.class);
		jdao.deleteAll(ItemEntity.class);
	}
	
	@Test
	public void testInsertArticleManyToOneUser(){
		this.clearTables();
		
		jdao.createJFishQueryByQName("jfishRelatedTest.delete.orderitems").executeUpdate();
		
		ArticleEntity newArticle = createArticle(0, "new artile");
		UserEntity user = createUserEntity(0, "articleUser");
		newArticle.setAuthor(user);

		jdao.insert(newArticle);//非关联插入
		ArticleEntity article = jdao.findById(ArticleEntity.class, newArticle.getId());
		Assert.assertNull(article.getAuthor());
		
		this.insertArticleCasecade();
	}
	
	protected ArticleEntity insertArticleCasecade(){
		ArticleEntity newArticle = createArticle(1, "related artile");
		UserEntity user = createUserEntity(1, "related articleUser");
		
		List<AddressEntity> addressList = new ArrayList<AddressEntity>();
		AddressEntity address = createAddress(0, "insert address detail");
		addressList.add(address);
		address = createAddress(1, "insert address detail");
		addressList.add(address);
		address = createAddress(2, "insert address detail");
		addressList.add(address);
		user.setAddress(addressList);

		jdao.insert(createAddress(111, "扰乱插入"));
		jdao.insert(createUserEntity(111, "扰乱插入"));
		
		newArticle.setAuthor(user);
		jdao.insert(newArticle, "author", "author.address");

		Assert.assertNotNull(newArticle.getAuthor().getId());
		Assert.assertNotNull(newArticle.getAuthor().getAddress().get(0).getId());
		
		ArticleEntity article = jdao.findById(ArticleEntity.class, newArticle.getId());
		Assert.assertNotNull(article.getAuthor());
		Assert.assertNotNull(article.getAuthor().getId());
		article.setAuthor(jdao.findById(UserEntity.class, article.getAuthor().getId()));
		List<AddressEntity> addrList = jem.findByProperties(AddressEntity.class, "userId", article.getAuthor().getId());
		article.getAuthor().setAddress(addrList);
		
		Assert.assertNotNull(article.getAuthor());
		Assert.assertNotNull(article.getAuthor().getId());
		Assert.assertEquals(article.getAuthor().getEmail(), user.getEmail());
		Assert.assertEquals(article.getAuthor().getHeight(), user.getHeight());
		Assert.assertEquals(article.getAuthor().getId(), addrList.get(0).getUserId());
		Assert.assertEquals(article.getAuthor().getId(), addrList.get(1).getUserId());
		
		return newArticle;
	}
	
	@Test
	public void testDynamicUpdateArticleManyToOneUser(){
		ArticleEntity article = this.insertArticleCasecade();
		article.setTitle("Dynamic artitle title update");
		article.setContent(" Dynamic article content update");
		
		article.getAuthor().setUserName("Dynamic artilce author update");
		for(AddressEntity address : article.getAuthor().getAddress()){
			address.setDetail("DynamicUpdate address");
			address.setPostCode(null);
		}
		
		jdao.dymanicUpdate(article, "author", "author.address");

		ArticleEntity art = jdao.findById(ArticleEntity.class, article.getId());
		UserEntity user = jdao.findById(UserEntity.class, article.getAuthor().getId());
		List<AddressEntity> addrList = jem.findByProperties(AddressEntity.class, "userId", article.getAuthor().getId());
		user.setAddress(addrList);
		art.setAuthor(user);
		
		Assert.assertEquals(article.getTitle(), art.getTitle());
		Assert.assertEquals(article.getContent(), art.getContent());
		Assert.assertEquals(article.getAuthor().getUserName(), user.getUserName());
		Assert.assertNotNull(user.getEmail());//validate dynamic update
		Assert.assertNotNull(user.getBirthDay());
		Assert.assertEquals(article.getAuthor().getEmail(), user.getEmail());
		Assert.assertEquals(article.getAuthor().getHeight(), user.getHeight());
		Assert.assertEquals(article.getAuthor().getAddress().get(0).getDetail(), art.getAuthor().getAddress().get(0).getDetail());
		Assert.assertNull(article.getAuthor().getAddress().get(0).getPostCode());
		Assert.assertNotNull(art.getAuthor().getAddress().get(0).getPostCode());
	}
	
	@Test
	public void testUpdateArticleManyToOneUser(){
		ArticleEntity article = this.insertArticleCasecade();
		article.setTitle("artitle title update");
		article.setContent("article content update");

		article.getAuthor().setUserName("artilce author update");
		article.getAuthor().setEmail(null);
		article.getAuthor().setBirthDay(null);
		for(AddressEntity address : article.getAuthor().getAddress()){
			address.setDetail("DynamicUpdate address");
			address.setPostCode(null);
		}
		
		jdao.update(article, "author", "author.address");

		ArticleEntity queryArt = jdao.findById(ArticleEntity.class, article.getId());
		UserEntity queryUser = jdao.findById(UserEntity.class, article.getAuthor().getId());
		List<AddressEntity> addrList = jem.findByProperties(AddressEntity.class, "userId", article.getAuthor().getId());
		queryUser.setAddress(addrList);
		queryArt.setAuthor(queryUser);
		
		Assert.assertEquals(article.getTitle(), queryArt.getTitle());
		Assert.assertEquals(article.getContent(), queryArt.getContent());
		Assert.assertEquals(article.getAuthor().getUserName(), queryUser.getUserName());
		Assert.assertNull(queryUser.getEmail());//validate update null
		Assert.assertNull(queryUser.getBirthDay());
		Assert.assertEquals(article.getAuthor().getAddress().get(0).getDetail(), queryArt.getAuthor().getAddress().get(0).getDetail());
		Assert.assertNull(article.getAuthor().getAddress().get(0).getPostCode());
		Assert.assertNull(queryArt.getAuthor().getAddress().get(0).getPostCode());
	}
	
	@Test
	public void testDeleteArticleManyToOneUser(){
		ArticleEntity article = this.insertArticleCasecade();
		jdao.delete(article, "author");

		ArticleEntity art = jdao.findById(ArticleEntity.class, article.getId());
		UserEntity user = jdao.findById(UserEntity.class, article.getAuthor().getId());
		Assert.assertNull(art);
		Assert.assertNull(user);
	}
	
	@Test
	public void testInsertUserOneToManyArticle(){
		UserEntity user = createUserEntity(1, "testInsertUserOneToManyArticle user");
		List<ArticleEntity> articles = new ArrayList<ArticleEntity>();
		articles.add(createArticle(0, "testInsertUserOneToManyArticle 0"));
		articles.add(createArticle(1, "testInsertUserOneToManyArticle 1"));
		articles.add(createArticle(2, "testInsertUserOneToManyArticle 2"));
		user.setArticles(articles);//article保存不用设置对user的导航，即可以不用调用：article.setAuthor(user)
		
		jdao.insert(user, "articles");
		
		UserEntity quser = jdao.findById(UserEntity.class, user.getId());
		List<ArticleEntity> qarticles = jem.findByProperties(ArticleEntity.class, "user_id", user.getId(), K.ASC, "id");

		Assert.assertEquals(user.getUserName(), quser.getUserName());
		Assert.assertEquals(user.getArticles().size(), qarticles.size());
		Assert.assertNotNull(qarticles.get(0).getAuthor());//验证 article不用设置对user的导航，即可以不用调用：article.setAuthor(user)
		Assert.assertNotNull(qarticles.get(0).getAuthor().getId());
		Assert.assertEquals(user.getArticles().get(0).getTitle(), qarticles.get(0).getTitle());
		Assert.assertEquals(user.getArticles().get(1).getTitle(), qarticles.get(1).getTitle());
	}
	
	/********
	 * 测试一对多更新操作
	 */
	@Test
	public void testUpdateUserOneToManyArticle(){
		UserEntity user = createUserEntity(1, "testUpdateUserOneToManyArticle user");
		List<ArticleEntity> articles = new ArrayList<ArticleEntity>();
		articles.add(createArticle(0, "testUpdateUserOneToManyArticle 0"));
		articles.add(createArticle(1, "testUpdateUserOneToManyArticle 1"));
		articles.add(createArticle(2, "testUpdateUserOneToManyArticle 2"));
		user.setArticles(articles);
		
		jdao.insert(user, "articles");
		
		user.setUserName("update testUpdateUserWithManyArticle");
		Assert.assertEquals(3, user.getArticles().size());
		
		user.getArticles().get(0).setTitle("testUpdateUserWithManyArticle title 1");
		user.getArticles().get(0).setContent(null);
		user.getArticles().get(1).setTitle("testUpdateUserWithManyArticle title 2");
		user.getArticles().remove(2);
		
		jdao.update(user, "articles");
		UserEntity quser = jdao.findById(UserEntity.class, user.getId());
		List<ArticleEntity> qarticles = jem.findByProperties(ArticleEntity.class, "user_id", user.getId());

		Assert.assertEquals(user.getUserName(), quser.getUserName());
		Assert.assertEquals(user.getArticles().size(), qarticles.size());
		Assert.assertEquals(user.getArticles().get(0).getTitle(), qarticles.get(0).getTitle());
		Assert.assertNull(qarticles.get(0).getContent());
		Assert.assertEquals(user.getArticles().get(1).getTitle(), qarticles.get(1).getTitle());
		
		user.getArticles().add(createArticle(3, "testUpdateUserWithManyArticle new insert title 3"));
		user.getArticles().add(createArticle(4, "testUpdateUserWithManyArticle new insert title 4"));
		try {
			jdao.update(user, "articles");
			Assert.fail("must be failed");
		} catch (Exception e) {
			Assert.assertTrue(JFishEntityNotSavedException.class.isInstance(e));
		}
	}

	
	@Test
	public void testSaveUserOneToManyArticle(){
		UserEntity user = createUserEntity(1, "testUpdateUserOneToManyArticle user");
		List<ArticleEntity> articles = new ArrayList<ArticleEntity>();
		articles.add(createArticle(0, "testUpdateUserOneToManyArticle 0"));
		articles.add(createArticle(1, "testUpdateUserOneToManyArticle 1"));
		articles.add(createArticle(2, "testUpdateUserOneToManyArticle 2"));
		user.setArticles(articles);
		
		jdao.insert(user, "articles");
		
		user.setUserName("update testUpdateUserWithManyArticle");
		Assert.assertEquals(3, user.getArticles().size());
		int artCount = 3;
		ArticleEntity article0 = user.getArticles().get(0);
		user.getArticles().remove(0);
//		artCount--;//remove不会移除数据库记录
		user.getArticles().get(0).setTitle("testUpdateUserWithManyArticle title 1");
		String content1 = user.getArticles().get(0).getContent();
		user.getArticles().get(0).setContent(null);//save 方法是动态更新，null值将不会更新
		user.getArticles().get(1).setTitle("testUpdateUserWithManyArticle title 2");
		user.getArticles().add(createArticle(3, "testUpdateUserWithManyArticle new insert title 3"));
		artCount++;
		user.getArticles().add(createArticle(4, "testUpdateUserWithManyArticle new insert title 4"));
		artCount++;
		jdao.save(user, "articles");
		
		UserEntity quser = jdao.findById(UserEntity.class, user.getId());
		List<ArticleEntity> qarticles = jem.findByProperties(ArticleEntity.class, "user_id", user.getId());

		Assert.assertEquals(user.getUserName(), quser.getUserName());
		Assert.assertEquals(artCount, qarticles.size());//验证数量
		
		Assert.assertEquals(article0.getTitle(), qarticles.get(0).getTitle());
		Assert.assertEquals(content1, qarticles.get(1).getContent());
		Assert.assertEquals(user.getArticles().get(1).getTitle(), qarticles.get(2).getTitle());
	}

	@Test
	public void testDynamicUpdateUserOneToManyArticle(){
		UserEntity user = createUserEntity(1, "save testDynamicUpdateUserOneToManyArticle user");
		List<ArticleEntity> articles = new ArrayList<ArticleEntity>();
		articles.add(createArticle(0, "save testDynamicUpdateUserOneToManyArticle 0"));
		articles.add(createArticle(1, "save testDynamicUpdateUserOneToManyArticle 1"));
		articles.add(createArticle(2, "save testDynamicUpdateUserOneToManyArticle 2"));
		user.setArticles(articles);//article不用设置对user的导航，即可以不用调用：article.setAuthor(user)
		
		jdao.insert(user, "articles");
		
		user.setUserName("update testDynamicUpdateUserOneToManyArticle");
		user.getArticles().remove(articles.size()-1);
		user.getArticles().get(0).setTitle("testDynamicUpdateUserOneToManyArticle title 1");
		String content0 = user.getArticles().get(0).getContent();
		user.getArticles().get(0).setContent(null);
		user.getArticles().get(1).setTitle("testDynamicUpdateUserOneToManyArticle title 2");
		jdao.dymanicUpdate(user, "articles");
		
		UserEntity quser = jdao.findById(UserEntity.class, user.getId());
		List<ArticleEntity> qarticles = jem.findByProperties(ArticleEntity.class, "user_id", user.getId());

		Assert.assertEquals(user.getUserName(), quser.getUserName());
		Assert.assertEquals(user.getArticles().size(), qarticles.size());
		Assert.assertEquals(user.getArticles().get(0).getTitle(), qarticles.get(0).getTitle());
		Assert.assertNotNull(qarticles.get(0).getContent());
		Assert.assertEquals(content0, qarticles.get(0).getContent());
		Assert.assertEquals(user.getArticles().get(1).getTitle(), qarticles.get(1).getTitle());
	}
	
	protected ItemEntity createItem(Long productId){
		ItemEntity item = new ItemEntity();
		item.setProductId(productId);
		item.setCount(2);
		item.setPrice(new BigDecimal("11.11"));
		return item;
	}
	
	@Test
	public void testJoinTableInsert(){
		OrderEntity order = new OrderEntity();
		order.setAddress("order address");
		
		List<ItemEntity> items = new ArrayList<ItemEntity>();
		items.add(createItem(1L));
		items.add(createItem(2L));
		items.add(createItem(3L));
		
		order.setItems(items);
		jdao.insert(order, "items");

		OrderEntity qorder = jdao.findById(OrderEntity.class, order.getId());
		List<ItemEntity> qitems = jem.createJFishQueryByQName("jfishRelatedTest.find.orderitems", "order_id", qorder.getId()).getResultList();
		qorder.setItems(qitems);
		
		Assert.assertEquals(order.getAddress(), qorder.getAddress());
		Assert.assertEquals(items.size(), qitems.size());
		Assert.assertEquals(items.get(0).getProductId(), qitems.get(0).getProductId());
		Assert.assertEquals(items.get(1).getProductId(), qitems.get(1).getProductId());
	}
	
}

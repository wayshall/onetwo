package org.onetwo.common.fish.richmodel;

import java.util.Collection;
import java.util.HashSet;

import org.onetwo.common.utils.DateUtil;

public class RichModelTestUtils {
	public static UserModel createUserModel(int i, String userName){
		UserModel user = new UserModel();
		user.setUserName(userName+i);
		user.setBirthDay(DateUtil.now());
		user.setEmail(i+"username@qq.com");
		user.setHeight(3.3f);
		
		return user;
	}
	public static ArticleModel createArticleModel(int i, String title){
		ArticleModel art = new ArticleModel();
		art.setTitle(i+title);
		art.setContent(art.getTitle()+" content");
		
		return art;
	}
	
	public static Collection<ArticleModel> createArticles(int count, String title){
		Collection<ArticleModel> artlist = new HashSet<ArticleModel>();
		for (int i = 0; i < 10; i++) {
			ArticleModel u = createArticleModel(i, title);
//			u.setAuthor(user);
			artlist.add(u);
		}
		return artlist;
	}
	public static Collection<UserModel> createUserModels(int count, String userName){
		Collection<UserModel> users = new HashSet<UserModel>();
		for (int i = 0; i < 10; i++) {
			UserModel u = createUserModel(i, userName);
			users.add(u);
		}
		return users;
	}
}

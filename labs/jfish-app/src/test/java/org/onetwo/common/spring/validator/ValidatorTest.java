package org.onetwo.common.spring.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.example.app.model.member.entity.ArticleEntity;
import org.example.app.model.member.entity.UserEntity;
import org.example.app.model.member.entity.UserEntity.PasswordOnly;
import org.junit.Test;
import org.onetwo.common.jfish.JFishBaseJUnitTest;
import org.onetwo.common.profiling.UtilTimerStack;

public class ValidatorTest extends JFishBaseJUnitTest {

	@Resource
	private Validator validator;
	
	@Resource
	private org.springframework.validation.Validator springValidator;

	@Test
	public void testUser(){
		UtilTimerStack.setActive(true);
		UserEntity user = new UserEntity();
		List<ArticleEntity> articles = new ArrayList<ArticleEntity>();
		articles.add(new ArticleEntity());
		user.setArticles(articles);
//		user.setUserName("123456789012345");
		Set<ConstraintViolation<UserEntity>> set = validator.validate(user);
		System.out.println("size: " + set.size()+", content: " + set);
		
		ValidationBindingResult errors = ValidationBindingResult.create(user, validator.validate(user, PasswordOnly.class));
		System.out.println("hasErrors: " + errors.hasErrors()+", errors: \n" + errors.getFieldErrorMessagesAsString(true, "\n"));
		
		System.out.println("userName: " + errors.getFieldErrorMessage("userName"));

	}
}

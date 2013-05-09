package org.onetwo.common.ejb.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.onetwo.common.test.mockito.BaseMockitoTest;

import test.entity.ArticleEntity;
import test.entity.ColumnEntity;
import test.entity.UserEntity;


public class JPAEntityManagerTest extends BaseMockitoTest {
	
	private AppEntityManagerImplForTest baseEntityManager;
	
	@Mock
	private EntityManager entityManager;
	
	private UserEntity user;
	
	public ColumnEntity createColumn(int index){
		ColumnEntity col = new ColumnEntity();
		col.setColumnName("columnName"+index);
		return col;
	}
	
	public ArticleEntity createArticle(int index){
		ArticleEntity art = new ArticleEntity();
		art.setRoleName("roleName"+index);
		
		List<ColumnEntity> cols = times("createColumn", 2);
		art.setColumns(cols);
		
		return art;
	}
	
	@Before
	public void setup(){
		this.baseEntityManager = new AppEntityManagerImplForTest();
		
		this.baseEntityManager.setEntityManager(entityManager);
		
		this.user = new UserEntity();
		List<ArticleEntity> roles = this.times("createArticle", 2);
		user.setArticles(roles);
	}
	
	@Test
	public void testRemoveCollection(){
		Mockito.doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object entity = invocation.getArguments()[0];
				return entity;
			}
			
		}).when(entityManager).merge(Mockito.anyObject());
		
		Mockito.doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
			
		}).when(entityManager).remove(Mockito.anyObject());
		this.baseEntityManager.remove(user);

		Assert.assertNull(user.getArticles());
	}

}

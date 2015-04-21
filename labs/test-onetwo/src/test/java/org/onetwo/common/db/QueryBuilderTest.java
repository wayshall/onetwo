package org.onetwo.common.db;

import org.junit.Assert;
import org.junit.Test;

import test.entity.UserEntity;

public class QueryBuilderTest {
	
	@Test
	public void testSimple(){
		QueryBuilder qb = QueryBuilderImpl.from(UserEntity.class);
		qb.select("id", "userName").field("age").greaterThan(25).asc("id");
		qb.build();
		String expected = "select userEntity.id, userEntity.userName from test.entity.UserEntity userEntity where userEntity.age > ? order by userEntity.id asc";
		Assert.assertEquals(expected, qb.getSql());
	}
	
	@Test
	public void testLeftJoin(){
		QueryBuilder qb = QueryBuilderImpl.from(UserEntity.class);
		qb.select("id", "userName")
		.leftJoin("t_user_roles", "tur")
		.on("id", "tur.user_id")
		.field("age").greaterThan(25).asc("id");
		qb.build();
//		System.out.println("sql :" + qv.getSql());
		String expected = "select userEntity.id, userEntity.userName from test.entity.UserEntity userEntity left join t_user_roles tur on (userEntity.id = tur.user_id) where userEntity.age > ? order by userEntity.id asc";
		Assert.assertEquals(expected, qb.getSql());
		

		qb = QueryBuilderImpl.from(UserEntity.class);
		qb.select("id", "userName")
		.leftJoin("t_user_roles", "tur")
		.on("id", "tur.user_id")
		.leftJoin("t_role", "tr")
		.on("tr.id", "tur.role_id")
		.field("age").greaterThan(25).asc("id");
		qb.build();
		System.out.println("sql :" + qb.getSql());
		expected = "select userEntity.id, userEntity.userName from test.entity.UserEntity userEntity " +
				"left join t_user_roles tur on (userEntity.id = tur.user_id) " +
				"left join t_role tr on (tr.id = tur.role_id) " +
				"where userEntity.age > ? order by userEntity.id asc";
		Assert.assertEquals(expected, qb.getSql());
	}

}

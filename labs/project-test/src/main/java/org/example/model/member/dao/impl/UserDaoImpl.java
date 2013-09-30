package org.example.model.member.dao.impl;

import java.util.List;

import org.example.model.member.entity.UserEntity;
import org.hibernate.SQLQuery;
import org.onetwo.common.base.BaseDao;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unchecked")
@Repository
public class UserDaoImpl extends BaseDao<UserEntity, Long> {

	
	public List<UserEntity> findUsers(String name){
		SQLQuery query = getSession().createSQLQuery("select * from t_user t where t.user_name like :userName");
		query.addEntity(UserEntity.class);
		query.setParameter("userName", name);
		return query.list();
	}
}

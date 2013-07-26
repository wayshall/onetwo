package qy.iccard.service;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qy.iccard.dao.UserDao;
import qy.iccard.entity.UserEntity;

@Transactional
@Service
public class UserServiceImpl {
	
	@Resource
	private UserDao userDao;
	
	public UserEntity findById(Long id){
		return userDao.findById(id);
	}
	
	public List<UserEntity> findAll(){
		return userDao.findAll();
	}

}
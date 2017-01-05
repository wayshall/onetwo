
package projects.manager.service.impl;

import java.util.Date;
import java.util.Optional;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import projects.manager.entity.User;

@Service
@Transactional
public class UserServiceImpl {

    @Autowired
    private BaseEntityManager baseEntityManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    
    public Optional<User> findById(Long id) {
		return Optional.ofNullable(baseEntityManager.findById(User.class, id));
	}

    public User loadById(Long id){
    	User user = baseEntityManager.load(User.class, id);
    	return user;
    }
    
    public void update(User adminUser){
        Assert.notNull(adminUser.getId(), "参数不能为null");
        User dbUser = loadById(adminUser.getId());
        if(dbUser==null){
            throw new ServiceException("找不到数据：" + adminUser.getId());
        }
        

        String newPwd = adminUser.getPassword();
        //不允许修改
        adminUser.setPassword(null);
        adminUser.setId(null);
        adminUser.setUserName(null);
        ReflectUtils.copyIgnoreBlank(adminUser, dbUser);
        
        //如果密码不为空，修改密码
    	if(StringUtils.isNotBlank(newPwd)){
    		dbUser.setPassword(passwordEncoder.encode(newPwd));
    	}
    	
        dbUser.setUpdateAt(new Date());
        baseEntityManager.update(dbUser);
    }
    
}
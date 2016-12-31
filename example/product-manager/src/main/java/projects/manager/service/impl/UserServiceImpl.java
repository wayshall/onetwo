
package projects.manager.service.impl;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.ext.security.utils.LoginUserDetails;
import org.onetwo.plugins.admin.dao.AdminRoleDao;
import org.onetwo.plugins.admin.entity.AdminRole;
import org.onetwo.plugins.admin.service.impl.AdminRoleServiceImpl;
import org.onetwo.plugins.admin.utils.Enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import projects.manager.entity.User;
import projects.manager.utils.Enums.UserTypes;
import projects.manager.vo.LoginUserInfo;

@Service
@Transactional
public class UserServiceImpl {

    @Autowired
    private BaseEntityManager baseEntityManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminRoleServiceImpl adminRoleServiceImpl;
    @Autowired
    private AdminRoleDao adminRoleDao;

    
    public Optional<User> findById(Long id) {
		return Optional.ofNullable(baseEntityManager.findById(User.class, id));
	}

	public void findPage(LoginUserInfo loginUser, Page<User> page, User adminUser){
        Querys.from(baseEntityManager, User.class)
        		.unselect("password")
        		.where()
	        		.field("id").notEqualTo(LoginUserDetails.ROOT_USER_ID)
	        		.field("userType").greaterThan(loginUser.getUserType())
	        		.field("belongToUserId")
	        			.when(()->loginUser.getUserType()!=null && loginUser.getUserType().intValue()>UserTypes.MANAGER.getValue())
	        			.equalTo(loginUser.getUserId())
	        		.addFields(adminUser)
	        		.ignoreIfNull()
        		.end()
        		.toQuery()
        		.page(page);
    }
    
    public void save(LoginUserInfo loginUser, User adminUser){
    	if(StringUtils.isBlank(adminUser.getPassword())){
    		throw new ServiceException("密码不能为空！");
    	}
    	adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
    	
    	adminUser.setStatus(UserStatus.NORMAL.name());
        Date now = new Date();
        adminUser.setCreateAt(now);
        adminUser.setUpdateAt(now);

        this.setUserType(loginUser, adminUser);
        baseEntityManager.save(adminUser);
        this.saveRoleByUserType(adminUser, null);
    }
    
    private void setUserType(LoginUserInfo loginUser, User adminUser){
    	UserTypes userType = UserTypes.of(loginUser.getUserType());
    	if(userType==UserTypes.FIRST){
    		adminUser.setUserType(UserTypes.SECOND.getValue());
    		adminUser.setBelongToUserId(loginUser.getUserId());
    	}
    }
    
    private void saveRoleByUserType(User adminUser, User dbUser){
    	if(adminUser.getUserType()!=null){
        	UserTypes userType = UserTypes.of(adminUser.getUserType());
        	if(dbUser!=null && adminUser.getUserType().equals(dbUser.getUserType()))
        		return ;
        	AdminRole role = adminRoleServiceImpl.findByName(userType.name());
        	Assert.notNull(role);
        	this.adminRoleServiceImpl.saveUserRoles(adminUser.getId(), role.getId());
        }
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
        

        this.saveRoleByUserType(adminUser, dbUser);
        
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
    
    public void deleteByIds(Long...ids){
        if(ArrayUtils.isEmpty(ids))
            throw new ServiceException("请先选择数据！");
        Stream.of(ids).forEach(id->deleteById(id));
    }
    
    public void deleteById(Long id){
        User adminUser = loadById(id);
        if(adminUser==null){
            throw new ServiceException("找不到数据:" + id);
        }
        adminRoleDao.deleteUserRoles(id);
        baseEntityManager.removeById(User.class, id);
    }
}
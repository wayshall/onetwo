package org.example.app.model.member.dao;

import org.example.app.model.member.entity.UserEntity;
import org.onetwo.common.fish.JFishCrudDaoImpl;
import org.springframework.stereotype.Repository;

/********
 * dao可以不要，一切看业务复杂度
 * @author wayshall
 *
 */
@Repository
public class UserDaoImpl extends JFishCrudDaoImpl<UserEntity, Long>{
 
}

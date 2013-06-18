package org.example.app.model.member;

import org.example.app.model.member.entity.UserEntity;
import org.onetwo.common.fish.JFishCrudServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends JFishCrudServiceImpl<UserEntity, Long>{

}

package org.example.app.model.member;

import org.example.app.model.member.entity.UserVersionEntity;
import org.onetwo.common.fish.JFishCrudServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserVersionServiceImpl extends JFishCrudServiceImpl<UserVersionEntity, Long>{

}

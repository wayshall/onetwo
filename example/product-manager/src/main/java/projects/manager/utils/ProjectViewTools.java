package projects.manager.utils;

import org.onetwo.boot.plugin.ftl.FreeMarkerViewTools;
import org.onetwo.ext.security.utils.SecurityUtils;
import org.springframework.stereotype.Component;

import projects.manager.utils.Enums.UserTypes;
import projects.manager.vo.LoginUserInfo;

@FreeMarkerViewTools("viewHelper")
@Component
public class ProjectViewTools {

	public boolean canShowBelongToUserField(){
		LoginUserInfo user = SecurityUtils.getCurrentLoginUser(LoginUserInfo.class);
		if(user.isSystemRootUser())
			return true;
		return user.getUserType()!=null && user.getUserType()<=UserTypes.MANAGER.getValue();
	}
}

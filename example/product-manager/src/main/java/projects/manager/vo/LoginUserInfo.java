package projects.manager.vo;

import java.util.Collection;

import lombok.Setter;

import org.onetwo.ext.security.utils.LoginUserDetails;
import org.springframework.security.core.GrantedAuthority;

import projects.manager.utils.Enums.UserTypes;

@SuppressWarnings("serial")
public class LoginUserInfo extends LoginUserDetails {

	@Setter
	private Integer userType;
	
	public LoginUserInfo(long userId, String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(userId, username, password, authorities);
	}

	public Integer getUserType() {
		if(isSystemRootUser())
			return null;
		return userType;
	}
	
	public UserTypes getUserTypes(){
		return UserTypes.of(userType);
	}

}

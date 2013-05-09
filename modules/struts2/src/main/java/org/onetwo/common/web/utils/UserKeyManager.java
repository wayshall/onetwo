package org.onetwo.common.web.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.onetwo.common.web.config.SiteConfig;
import org.onetwo.common.web.s2.security.StrutsAuthentication;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings("unchecked")
public class UserKeyManager {
	
	public static final UserKeyManager defaultUserKeyManager;
	public static final UserKeyManager emptyUserKeyManager;
	private static final List<UserKeyManager> USERKEYS;
	
	static {
		USERKEYS = new ArrayList<UserKeyManager>();
		defaultUserKeyManager = create(SiteConfig.getInstance().getUserDetailKey(), SiteConfig.getInstance().getTokenName(), StrutsAuthentication.LONGIN_VIEW);
		emptyUserKeyManager = create("", "", "");
	}
	
	public static final String USER_KEY_MANAGER = "__user_key_manager";
	
//	public final String CURRENT_USER_KEY = "current_user_key";
//	public final String CURRENT_TOKEN_KEY = "current_token_key";
	

	public final static UserKeyManager create(String currentUserKey, String currentTokenKey, String securityResult){
		UserKeyManager key = new UserKeyManager(currentUserKey, currentTokenKey, securityResult);
		return addUserKeyManager(key);
	}
	
	public final static UserKeyManager addUserKeyManager(UserKeyManager key){
		if(!USERKEYS.contains(key))
			USERKEYS.add(key);
		return key;
	}
	
	public static List<UserKeyManager> getUserKeys(){
		return USERKEYS;
	}
	
	public static UserKeyManager getCurrentUserKeyManager(){
		UserKeyManager key = get(USER_KEY_MANAGER);
		
		return key;
	}

	public static void setCurrentUserKeyManager(UserKeyManager value){
		getCurentContext().put(USER_KEY_MANAGER, value);
	}
	
	private static ActionContext getCurentContext(){
		return ServletActionContext.getContext();
	}
	
	public static <T> T get(String key){
		return (T)getCurentContext().get(key);
	}
	
	protected String currentUserKey;
	protected String currentTokenKey;
	protected String securityResult;
	
	protected String authenticationName;
	
	public UserKeyManager(){
		this(SiteConfig.getInstance().getUserDetailKey(), SiteConfig.getInstance().getTokenName(), StrutsAuthentication.LONGIN_VIEW);
	}

	public UserKeyManager(String currentUserKey, String currentTokenKey, String securityResult) {
		super();
		this.currentUserKey = currentUserKey;
		this.currentTokenKey = currentTokenKey;
		this.securityResult = securityResult;
		this.init();
	}
	
	protected void init(){
	}

	public String getCurrentUserKey() {
		return currentUserKey;
	}

	public String getCurrentTokenKey() {
		return currentTokenKey;
	}

	public String getSecurityResult() {
		return securityResult;
	}

	public void setSecurityResult(String securityResult) {
		this.securityResult = securityResult;
	}

	public void setCurrentUserKey(String currentUserKey) {
		this.currentUserKey = currentUserKey;
	}

	public void setCurrentTokenKey(String currentTokenKey) {
		this.currentTokenKey = currentTokenKey;
	}

	public String getAuthenticationName() {
		return authenticationName;
	}

	public void setAuthenticationName(String authenticationName) {
		this.authenticationName = authenticationName;
	}
}

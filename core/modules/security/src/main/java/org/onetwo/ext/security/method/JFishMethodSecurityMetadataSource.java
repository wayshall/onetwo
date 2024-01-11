package org.onetwo.ext.security.method;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.permission.MenuInfoParserFactory;
import org.onetwo.ext.permission.api.IPermission;
import org.onetwo.ext.permission.api.annotation.ByPermissionClass;
import org.onetwo.ext.security.config.SecurityConfigUtils;
import org.onetwo.ext.security.metadata.CodeSecurityConfig;
import org.onetwo.ext.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.ExpressionAuthorizationDecision;
import org.springframework.security.core.Authentication;

import com.google.common.collect.Lists;

/****
 * 参考：SecuredAnnotationSecurityMetadataSource
 * 
 * 
 * 
 * 读取方法所需权限
 * @author way
 *
 */
public class JFishMethodSecurityMetadataSource 
//extends AbstractFallbackMethodSecurityMetadataSource 
implements AuthorizationManager<MethodInvocation> {
	
	@Autowired
	private MenuInfoParserFactory<? extends IPermission> menuFactory;
//	private DefaultWebSecurityExpressionHandler securityExpressionHandler = new DefaultWebSecurityExpressionHandler();
	private DefaultMethodSecurityExpressionHandler securityExpressionHandler = new DefaultMethodSecurityExpressionHandler();

	public JFishMethodSecurityMetadataSource() {
	}

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, MethodInvocation mi) {
		Collection<CodeSecurityConfig> permlist = findAttributes(mi.getMethod(), mi.getThis().getClass());
		if (permlist.isEmpty()) {
			permlist = findAttributes(mi.getThis().getClass());
		}
		if (permlist.isEmpty()) {
			return SecurityConfigUtils.ALLOW;
		}
		
		EvaluationContext ctx = securityExpressionHandler.createEvaluationContext(authentication.get(), mi);
		for (CodeSecurityConfig perm : permlist) {
			boolean granted = ExpressionUtils.evaluateAsBoolean(perm.getAuthorizeExpression(), ctx);
			if (granted) {
				return new ExpressionAuthorizationDecision(granted, perm.getAuthorizeExpression());
			}
		}
		return SecurityConfigUtils.DENY;
	}
	
	protected Collection<CodeSecurityConfig> findAttributes(Method method, Class<?> targetClass) {
		List<CodeSecurityConfig> permlist = Lists.newArrayList();
		ByPermissionClass byMenu = AnnotationUtils.findAnnotation(method, ByPermissionClass.class);
		if(byMenu!=null){
			permlist.addAll(extractByPermissionClassAttributes(byMenu.value()));
		}
		return permlist;
	}

	protected Collection<CodeSecurityConfig> findAttributes(Class<?> clazz) {
		List<CodeSecurityConfig> permlist = Lists.newArrayList();
		ByPermissionClass byFunc = AnnotationUtils.findAnnotation(clazz, ByPermissionClass.class);
		if(byFunc!=null){
			permlist.addAll(extractByPermissionClassAttributes(byFunc.value()));
		}
		return permlist;
	}
	
	/***
	 * DatabaseSecurityMetadataSource#createSecurityConfig
	 * @author weishao zeng
	 * @param codeClasses
	 * @return
	 */
	private List<CodeSecurityConfig> extractByPermissionClassAttributes(Class<?>...codeClasses){
		if (LangUtils.isEmpty(codeClasses)) {
			return Collections.emptyList();
		}
		
		List<CodeSecurityConfig> perms = Stream.of(codeClasses)
				.map(cls->{
					Optional<String> permOpt = menuFactory.getPermissionCode(cls);
					if (!permOpt.isPresent()) {
						throw new BaseException("can not parse permission class: " + cls);
					}
					String code = SecurityUtils.createSecurityExpression(permOpt.get());
					Expression exp = securityExpressionHandler.getExpressionParser().parseExpression(code);
//					WebExpressionConfigAttribute config = new WebExpressionConfigAttribute(exp);
					CodeSecurityConfig config = new CodeSecurityConfig(code, code, exp);
					return config;
				})
				.collect(Collectors.toList());
		return perms;
	}

	
}

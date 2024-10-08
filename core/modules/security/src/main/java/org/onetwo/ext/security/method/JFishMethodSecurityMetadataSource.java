package org.onetwo.ext.security.method;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.permission.MenuInfoParserFactory;
import org.onetwo.ext.permission.api.IPermission;
import org.onetwo.ext.permission.api.annotation.ByPermissionClass;
import org.onetwo.ext.security.metadata.CodeSecurityConfig;
import org.onetwo.ext.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.Expression;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.method.AbstractFallbackMethodSecurityMetadataSource;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

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
public class JFishMethodSecurityMetadataSource extends AbstractFallbackMethodSecurityMetadataSource {
	@Autowired
	private MenuInfoParserFactory<? extends IPermission> menuFactory;
	private DefaultWebSecurityExpressionHandler securityExpressionHandler = new DefaultWebSecurityExpressionHandler();

	public JFishMethodSecurityMetadataSource() {
//		super();
//		this.menuFactory = menuFactory;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	protected Collection<ConfigAttribute> findAttributes(Method method, Class<?> targetClass) {
		List<ConfigAttribute> permlist = Lists.newArrayList();
		ByPermissionClass byMenu = AnnotationUtils.findAnnotation(method, ByPermissionClass.class);
		if(byMenu!=null){
			permlist.addAll(extractByPermissionClassAttributes(byMenu.value()));
		}
		return permlist;
	}

	@Override
	protected Collection<ConfigAttribute> findAttributes(Class<?> clazz) {
		List<ConfigAttribute> permlist = Lists.newArrayList();
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
	private List<ConfigAttribute> extractByPermissionClassAttributes(Class<?>...codeClasses){
		if (LangUtils.isEmpty(codeClasses)) {
			return Collections.emptyList();
		}
		
		List<ConfigAttribute> perms = Stream.of(codeClasses)
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

package org.onetwo.ext.security.method;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.onetwo.ext.permission.api.annotation.ByPermissionClass;
import org.onetwo.ext.permission.entity.DefaultIPermission;
import org.onetwo.ext.permission.parser.MenuInfoParser;
import org.onetwo.ext.security.method.MethodWebExpressionVoter.WebExpressionConfigAttribute;
import org.onetwo.ext.security.utils.SecurityUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.Expression;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.method.AbstractFallbackMethodSecurityMetadataSource;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/****
 * 读取方法所需权限
 * @author way
 *
 */
public class JFishMethodSecurityMetadataSource extends AbstractFallbackMethodSecurityMetadataSource {
	@Resource
	private MenuInfoParser<? extends DefaultIPermission<?>> menuInfoParser;
	
	private DefaultWebSecurityExpressionHandler securityExpressionHandler = new DefaultWebSecurityExpressionHandler();
	

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	protected Collection<ConfigAttribute> findAttributes(Method method, Class<?> targetClass) {
		List<ConfigAttribute> permlist = Lists.newArrayList();
		ByPermissionClass byMenu = AnnotationUtils.findAnnotation(method, ByPermissionClass.class);
		if(byMenu!=null){
			permlist.addAll(extractAttributes(byMenu.value()));
		}
		ByPermissionClass byFunc = AnnotationUtils.findAnnotation(method, ByPermissionClass.class);
		if(byFunc!=null){
			permlist.addAll(extractAttributes(byFunc.value()));
		}
		return permlist;
	}
	
	private List<ConfigAttribute> extractAttributes(Class<?>...codeClasses){
		if(codeClasses!=null){
			List<ConfigAttribute> perms = Stream.of(codeClasses)
					.map(cls->{
						String code = SecurityUtils.createSecurityExpression(menuInfoParser.getCode(cls));
						Expression exp = securityExpressionHandler.getExpressionParser().parseExpression(code);
						WebExpressionConfigAttribute config = new WebExpressionConfigAttribute(exp);
						return config;
					})
					.collect(Collectors.toList());
			return perms;
		}
		return ImmutableList.of();
	}

	@Override
	protected Collection<ConfigAttribute> findAttributes(Class<?> clazz) {
		return null;
	}
	
}

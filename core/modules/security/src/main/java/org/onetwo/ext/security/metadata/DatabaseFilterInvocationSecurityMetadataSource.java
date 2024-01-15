package org.onetwo.ext.security.metadata;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.ext.security.metadata.DatabaseSecurityMetadataSource.AuthorityResource;
import org.onetwo.ext.security.metadata.DatabaseSecurityMetadataSource.SortableAntPathRequestMatcher;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.onetwo.ext.security.utils.SecurityUtils;
import org.slf4j.Logger;
import org.springframework.expression.Expression;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 默认的DefaultFilterInvocationSecurityMetadataSource匹配到了立刻返回，
 * 这里提供一个匹配所有到实现
 * @author weishao zeng
 * <br/>
 */
public class DatabaseFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	protected final Logger logger = JFishLoggerFactory.getLogger(getClass());

	private DefaultWebSecurityExpressionHandler securityExpressionHandler = new DefaultWebSecurityExpressionHandler();
	private final Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;
	private boolean ignoreAnyRequestMatcher = false;
	private boolean allowManyPermissionMapToOneUrl = true;
	private SecurityConfig securityConfig;

	public DatabaseFilterInvocationSecurityMetadataSource() {
		this.requestMap = Maps.newLinkedHashMap();
	}
	
	public DatabaseFilterInvocationSecurityMetadataSource(LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap) {
		this.requestMap = requestMap;
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();

		for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap
				.entrySet()) {
			allAttributes.addAll(entry.getValue());
		}

		return allAttributes;
	}

	public Collection<ConfigAttribute> getAttributes(Object object) {
		final HttpServletRequest request = ((RequestAuthorizationContext) object).getRequest();
		Collection<ConfigAttribute> auths = Sets.newHashSet();
		for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
			if (ignoreAnyRequestMatcher && entry.getKey().equals(AnyRequestMatcher.INSTANCE)) {
				if (logger.isInfoEnabled()) {
					logger.info("ignore any matcher for: {}", entry.getValue());
				}
				continue;
			}
			
//			if (entry.getKey().toString().contains("/productCategory/treeList*")) {
//				System.out.println("test");
//			}
			if (entry.getKey().matches(request)) {
				auths.addAll(entry.getValue());
			}
		}
		
		if (auths.isEmpty()) {
			ConfigAttribute anyRequest = createAnyRequestConfigAttribute();
			return Arrays.asList(anyRequest);
		}
		
		return auths;
	}

	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	public void setSecurityConfig(SecurityConfig securityConfig) {
		this.securityConfig = securityConfig;
	}
	
	private ConfigAttribute createAnyRequestConfigAttribute() {
		String anyRequest = securityConfig.getAnyRequest();
		//其它未标记管理的功能的默认权限
		if(StringUtils.isBlank(anyRequest)){
			return null;
		}
//		else if(SexcurityConfig.ANY_REQUEST_NONE.equals(anyRequest)){
//			return null;
//		}
		else{
			ConfigAttribute attr = createSecurityConfig("anyRequest", anyRequest);
			return attr;
		}
	}

	/***
	 * 表达式：hasAuthority(auth.getAuthority())
	 * @author weishao zeng
	 * @param auth
	 * @return
	 */
	protected ConfigAttribute createSecurityConfig(AuthorityResource auth){
		return createSecurityConfig(auth.getAuthorityName(), auth.getAuthority());
	}
	
	protected ConfigAttribute createSecurityConfig(String authName, String authority){
		if (StringUtils.isBlank(authority)) {
			throw new BaseException("authority can not be blank: " + authName);
		}
		// auth.getAuthority()=perm.code
		String exp = SecurityUtils.createSecurityExpression(authority);
		Expression authorizeExpression = this.securityExpressionHandler.getExpressionParser().parseExpression(exp);
		return new CodeSecurityConfig(authName, authority, authorizeExpression);
	}
	

	public void buildRequestMap(List<AuthorityResource> authorities){
//		List<AuthorityResource> authorities = fetchAuthorityResources();
		final Map<SortableAntPathRequestMatcher, Collection<ConfigAttribute>> resouceMap = new HashMap<>(authorities.size());
		authorities.forEach(auth->{
//			if (auth.getAuthority().equals("test")) {
//				System.out.println("test");
//			}
			auth.getUrlResourceInfo().forEach(r->{
				//根据httpmethod和url映射权限标识，url拦截时也是根据这个matcher找到对应的权限SecurityConfig
//				AntPathRequestMatcher matcher = new AntPathRequestMatcher(r.getUrl(), r.getMethod());
				SortableAntPathRequestMatcher matcher = new SortableAntPathRequestMatcher(new AntPathRequestMatcher(r.getUrl(), r.getMethod()), auth.getSort());
//				if (r.getUrl().contains("/mallCanteenMgr/productCategory/treeList*")) {
//					System.out.println("test");
//				}
				if(resouceMap.containsKey(matcher)){
//					resouceMap.get(matcher).add(new SecurityConfig(auth.getAuthority()));
					Collection<ConfigAttribute> attrs = resouceMap.get(matcher);
//					throw new RuntimeException("Expected a single expression attribute for " + matcher);
					if(allowManyPermissionMapToOneUrl){
						attrs.add(createSecurityConfig(auth));
					}else{
						throw new RuntimeException("permission conflict, don't allow many permission map to one url. exist: " + attrs + ", new: "+auth.getAuthority());
					}
				}else{
					resouceMap.put(matcher, Lists.newArrayList(createSecurityConfig(auth)));
				}
			});
		});
		
		//first sorted by sort field, then sorted by pattern
		//数字越小，越靠前
		List<SortableAntPathRequestMatcher> keys = resouceMap.keySet()
													.stream()
													.sorted((o1, o2)->{
														//这里先使用了sort字段决定优先是不对的，sort字段只作为界面的菜单显示顺序用
														//如果遇到菜单A（/prefix*）显示在菜单B（/prefixBMenuPath*）上面，
														//但访问/prefixBMenuPath时根据url模式应该是先匹配B的，结果却匹配到了A，导致denied access
														//如果需要手动指定匹配优先模式，应该新增另外的字段match_sort
														/*int rs = o1.getSort().compareTo(o2.getSort());
														if(rs!=0)
															return rs;*/
														//长的优先
														int rs = o2.getPathMatcher().getPattern().length() - o1.getPathMatcher().getPattern().length();
														if(rs!=0)
															return rs;
														return -o1.getPathMatcher().getPattern().compareTo(o2.getPathMatcher().getPattern());
													})
													.collect(Collectors.toList());
//		Collections.reverse(keys);
//		this.requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
		requestMap.clear();
		keys.forEach(k->{
//			System.out.println("url:"+k.getPathMatcher()+", value:"+resouceMap.get(k));
			this.requestMap.put(k.getPathMatcher(), resouceMap.get(k));
		});
	}
}

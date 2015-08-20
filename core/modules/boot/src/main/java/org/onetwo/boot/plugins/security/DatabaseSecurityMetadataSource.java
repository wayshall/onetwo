package org.onetwo.boot.plugins.security;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

import org.onetwo.boot.plugins.permission.utils.UrlResourceInfo;
import org.onetwo.boot.plugins.permission.utils.UrlResourceInfoParser;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.file.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;


public class DatabaseSecurityMetadataSource extends JdbcDaoSupport /*implements FactoryBean<FilterInvocationSecurityMetadataSource>*/ {
	private static final String AUTHORITY_RESOURCE_SQL_FILE = "/plugins/security/authority_resource.sql";

	private String resourceQuery;
	
	private LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap;
	
	private DefaultWebSecurityExpressionHandler securityExpressionHandler = new DefaultWebSecurityExpressionHandler();
	
	// from WebSecurityExpressionRoot
	private Set<String> keywords = ImmutableSet.of("permitAll", "denyAll", "is", "has");
	
	@PostConstruct
	public void buildRequestMap(){
		if(StringUtils.isBlank(resourceQuery)){
			Resource res = SpringUtils.classpath(AUTHORITY_RESOURCE_SQL_FILE);
			if(res.exists()){
				try {
					List<String> strs = FileUtils.readAsList(res.getInputStream());
					this.resourceQuery = StringUtils.join(strs, " ");
				} catch (IOException e) {
					throw new BaseException("read sql file error: "+ AUTHORITY_RESOURCE_SQL_FILE, e);
				}
			}
		}
		
		Assert.hasText(resourceQuery);
		ResourceMapping mapping = new ResourceMapping(getDataSource(), resourceQuery);
		
		List<AuthorityResource> authorities = mapping.execute();
		final LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> resouceMap = new LinkedHashMap<>(authorities.size());
		authorities.forEach(auth->{
			auth.getUrlResourceInfo().forEach(r->{
				AntPathRequestMatcher matcher = new AntPathRequestMatcher(r.getUrl(), r.getMethod());
				if(resouceMap.containsKey(matcher)){
//					resouceMap.get(matcher).add(new SecurityConfig(auth.getAuthority()));
					throw new BaseException("Expected a single expression attribute for " + matcher);
				}else{
					resouceMap.put(matcher, Lists.newArrayList(createSecurityConfig(auth)));
				}
			});
		});
		this.requestMap = resouceMap;
		/*mapping.execute().stream()
			.collect(Collectors.toMap(r->{
				AntPathRequestMatcher matcher = new AntPathRequestMatcher(r.getUrl(), r.getMethod());
				return matcher;
			}, r->{
				ConfigAttribute attr = new SecurityConfig(r.getAuthority());
				return attr;
			}));*/
	}
	
	protected SecurityConfig createSecurityConfig(AuthorityResource authority){
		String authString = authority.getAuthority();
		if(isKeyword(authString)){
			return new SecurityConfig(authString);
		}
		StringBuilder str = new StringBuilder("hasAuthority('");
		str.append(authString).append("')");
		return new SecurityConfig(str.toString());
	}
	
	protected boolean isKeyword(String authority){
		return keywords.stream().filter(key->authority.startsWith(key))
						.findAny().isPresent();
	}
	
	public ExpressionBasedFilterInvocationSecurityMetadataSource convertTo(FilterInvocationSecurityMetadataSource source){
		/*if(DefaultFilterInvocationSecurityMetadataSource.class.isInstance(source)){
			LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> existRequestMap = (LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>)ReflectUtils.getFieldValue(source, "requestMap");
			requestMap.putAll(existRequestMap);
			existRequestMap.clear();
			existRequestMap.putAll(requestMap);
			for(Entry<RequestMatcher, Collection<ConfigAttribute>> entry : existRequestMap.entrySet()){
				Collection<ConfigAttribute> configs = Collections2.transform(entry.getValue(), new Function<ConfigAttribute, ConfigAttribute>() {

					@Override
					public ConfigAttribute apply(ConfigAttribute input) {
						return new SecurityConfig(input.toString());
					}
					
				});
				this.requestMap.put(entry.getKey(), configs);
			}
			return new ExpressionBasedFilterInvocationSecurityMetadataSource(requestMap, securityExpressionHandler);
			
		}else{
			return new ExpressionBasedFilterInvocationSecurityMetadataSource(requestMap, securityExpressionHandler);
		}*/
		return new ExpressionBasedFilterInvocationSecurityMetadataSource(requestMap, securityExpressionHandler);
	}
	
	/*@Override
    public FilterInvocationSecurityMetadataSource getObject() throws Exception {
//	    return new DefaultFilterInvocationSecurityMetadataSource(requestMap);
		return new ExpressionBasedFilterInvocationSecurityMetadataSource(requestMap, securityExpressionHandler);
    }

	@Override
    public Class<?> getObjectType() {
	    return DefaultFilterInvocationSecurityMetadataSource.class;
    }

	@Override
    public boolean isSingleton() {
	    return true;
    }*/
	
	public static class ResourceMapping extends MappingSqlQuery<AuthorityResource> {
		private UrlResourceInfoParser urlResourceInfoParser = new UrlResourceInfoParser();

		public ResourceMapping(DataSource ds, String sql) {
	        super(ds, sql);
        }

		@Override
        protected AuthorityResource mapRow(ResultSet rs, int rowNum) throws SQLException {
			AuthorityResource authoricty = new AuthorityResource();
			String rp = rs.getString("resources_pattern");
			List<UrlResourceInfo> urlResourceInfo = urlResourceInfoParser.parseToUrlResourceInfos(rp);
			authoricty.setUrlResourceInfo(urlResourceInfo);
			authoricty.setAuthority(rs.getString("authority"));
	        return authoricty;
        }
		
	}
	
	public static class AuthorityResource {
		@Getter @Setter
		private String authority;
		@Getter @Setter
		private List<UrlResourceInfo> urlResourceInfo;
	}
	

}

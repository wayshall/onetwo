package org.onetwo.boot.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.google.common.collect.Lists;




public class DatabaseSecurityMetadataSource extends JdbcDaoSupport implements FactoryBean<FilterInvocationSecurityMetadataSource> {
	
	private String resourceQuery;
	
	private LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap;
	
	@PostConstruct
	public void buildRequestMap(){
		ResourceMapping mapping = new ResourceMapping(getDataSource(), resourceQuery);
		
		List<AuthorityResource> authorities = mapping.execute();
		final LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> resouceMap = new LinkedHashMap<>(authorities.size());
		authorities.forEach(r->{
			AntPathRequestMatcher matcher = new AntPathRequestMatcher(r.getUrl(), r.getMethod());
			if(resouceMap.containsKey(matcher)){
				resouceMap.get(matcher).add(new SecurityConfig(r.getAuthority()));
			}else{
				resouceMap.put(matcher, Lists.newArrayList(new SecurityConfig(r.getAuthority())));
			}
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

	@Override
    public FilterInvocationSecurityMetadataSource getObject() throws Exception {
	    return new DefaultFilterInvocationSecurityMetadataSource(requestMap);
    }

	@Override
    public Class<?> getObjectType() {
	    return DefaultFilterInvocationSecurityMetadataSource.class;
    }

	@Override
    public boolean isSingleton() {
	    return true;
    }
	
	public static class ResourceMapping extends MappingSqlQuery<AuthorityResource> {

		public ResourceMapping(DataSource ds, String sql) {
	        super(ds, sql);
        }

		@Override
        protected AuthorityResource mapRow(ResultSet rs, int rowNum) throws SQLException {
	        // TODO Auto-generated method stub
	        return null;
        }
		
	}
	
	public static class AuthorityResource {
		private String url;
		private String method;
		private String authority;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getAuthority() {
			return authority;
		}
		public void setAuthority(String authority) {
			this.authority = authority;
		}
		public String getMethod() {
			return method;
		}
		public void setMethod(String method) {
			this.method = method;
		}
	}
	

}

package org.onetwo.ext.security.metadata;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.ext.permission.MenuInfoParserFactory;
import org.onetwo.ext.permission.api.IPermission;
import org.onetwo.ext.permission.api.PermissionConfig;
import org.onetwo.ext.permission.utils.UrlResourceInfo;
import org.onetwo.ext.permission.utils.UrlResourceInfoParser;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


public class DatabaseSecurityMetadataSource extends JdbcDaoSupport implements JdbcSecurityMetadataSourceBuilder {
	/*****
	 * 获取权限标识和url的sql语句，返回下列字段：
	 * authority：权限标识代码
	 * resources_pattern：拦截的资源模式，格式：http_method|request_path,
	 * format: http_method|request_path,http_method|request_path,http_method|request_path...
	 */
	private static final String AUTHORITY_RESOURCE_SQL_FILE = "/plugins/security/authority_resource.sql";

	private String resourceQuery;
	private List<String> appCodes;
	
//	private LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = Maps.newLinkedHashMap();


//	private FilterSecurityInterceptor filterSecurityInterceptor;
	@Autowired(required = false)
	private MenuInfoParserFactory<? extends IPermission> menuInfoParserFactory;
	// from WebSecurityExpressionRoot
//	private Set<String> keywords = ImmutableSet.of("permitAll", "denyAll", "is", "has");
	private SecurityMetadataSource securityMetadataSource;

	private SecurityConfig securityConfig;
	

	@Override
	protected void checkDaoConfig() {
		super.checkDaoConfig();
		
		if (menuInfoParserFactory!=null) {
			List<? extends PermissionConfig<?>> configs = menuInfoParserFactory.getPermissionConfigList();
			if(configs!=null){
				List<String> appCodes = configs.stream().map(c->c.getAppCode()).collect(Collectors.toList());
				setAppCodes(appCodes);
			}
		}
	}
	

	@Override
	protected void initDao() throws Exception {
		this.buildSecurityMetadataSource();
	}
	
	protected List<AuthorityResource> fetchAuthorityResources(){
		if(StringUtils.isBlank(resourceQuery)){
			Resource res = SpringUtils.classpath(AUTHORITY_RESOURCE_SQL_FILE);
			if(res.exists()){
				try {
					List<String> strs = FileUtils.readAsList(res.getInputStream());
					this.resourceQuery = StringUtils.join(strs, " ");
				} catch (IOException e) {
					throw new BaseException("read sql file error: "+ AUTHORITY_RESOURCE_SQL_FILE, e);
				}
			}else{
				this.resourceQuery = "SELECT "
										+ "perm.name as authority_name, "
										+ "perm.code as authority, "
										+ "perm.resources_pattern as resources_pattern, "
										+ "perm.sort "
										+ "FROM admin_permission perm "
										+ "WHERE perm.resources_pattern is not null "
										+ (LangUtils.isEmpty(appCodes)?"":"and perm.app_code in ( :appCode ) ")
//										+ "and perm.resources_pattern!='' " //oracle里是个坑
										+ "order by perm.sort";
			}
		}
		
		Assert.hasText(resourceQuery, "resourceQuery must has text!");
//		ResourceMapping mapping = new ResourceMapping(getDataSource(), resourceQuery);
//		List<AuthorityResource> authorities = mapping.execute();
		ResourceMapping mapping = new ResourceMapping();
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(getDataSource());
		Map<String, Object> params = new HashMap<String, Object>();
		if(!LangUtils.isEmpty(appCodes)){
			params.put("appCode", appCodes);
		}
		List<AuthorityResource> authorities = jdbcTemplate.query(resourceQuery, params, mapping);
		
		if(authorities.isEmpty()){
			logger.warn("no authorities fetch, check your application!");
		}
		return authorities;
	}
	
	public void setAppCodes(List<String> appCodes) {
		this.appCodes = appCodes;
	}

	public void setResourceQuery(String resourceQuery) {
		this.resourceQuery = resourceQuery;
	}
	/****
	 * fetch all permissions from database
	 */
//	@PostConstruct
	
	
	private Map<RequestMatcher, Collection<ConfigAttribute>> defaultRequestMap;
	/****
	 * 基于url匹配拦截时，转换为ExpressionBasedFilterInvocationSecurityMetadataSource
	 * @param source
	 * @return
	 */
	@Override
	public void buildSecurityMetadataSource(){
//		Assert.notNull(filterSecurityInterceptor, "filterSecurityInterceptor can not be null");
		List<AuthorityResource> authorities = fetchAuthorityResources();
//		this.buildRequestMap();
		
//		Map<RequestMatcher, Collection<ConfigAttribute>> originRequestMap = getDefaultRequestMap();
//		if(originRequestMap!=null && !originRequestMap.isEmpty()){
//			this.requestMap.putAll(originRequestMap);
//		}
//		DefaultFilterInvocationSecurityMetadataSource fism = new DefaultFilterInvocationSecurityMetadataSource(requestMap);
		DatabaseFilterInvocationSecurityMetadataSource dfism = new DatabaseFilterInvocationSecurityMetadataSource();
		dfism.setSecurityConfig(securityConfig);
		dfism.buildRequestMap(authorities);
//		this.filterSecurityInterceptor.setSecurityMetadataSource(fism);
		this.securityMetadataSource = dfism;
	}
	
	protected final Map<RequestMatcher, Collection<ConfigAttribute>> getDefaultRequestMap() {
		Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = this.defaultRequestMap;
		if (requestMap==null) {
//			DefaultFilterInvocationSecurityMetadataSource originMetadata = (DefaultFilterInvocationSecurityMetadataSource)filterSecurityInterceptor.getSecurityMetadataSource();
//			//这个内置实现不支持一个url映射到多个表达式
////			ExpressionBasedFilterInvocationSecurityMetadataSource fism = new ExpressionBasedFilterInvocationSecurityMetadataSource(requestMap, securityExpressionHandler);
//			requestMap = (Map<RequestMatcher, Collection<ConfigAttribute>>)ReflectUtils.getFieldValue(originMetadata, "requestMap", false);
//			this.defaultRequestMap = requestMap;
		}
		return requestMap;
	}

//	public void setFilterSecurityInterceptor(FilterSecurityInterceptor filterSecurityInterceptor) {
//		this.filterSecurityInterceptor = filterSecurityInterceptor;
//	}
	
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
	
	public static class ResourceMapping extends MappingSqlQuery<AuthorityResource> implements RowMapper<AuthorityResource> {
		private UrlResourceInfoParser urlResourceInfoParser = new UrlResourceInfoParser();
		private boolean hasAuthorityName = true;

		public ResourceMapping(){
		}
		public ResourceMapping(DataSource ds, String sql) {
	        super(ds, sql);
	        hasAuthorityName = sql.contains("authority_name");
        }

		@Override
		public AuthorityResource mapRow(ResultSet rs, int rowNum) throws SQLException {
			AuthorityResource authoricty = new AuthorityResource();
			String rp = rs.getString("resources_pattern");
			List<UrlResourceInfo> urlResourceInfo = urlResourceInfoParser.parseToUrlResourceInfos(rp);
			authoricty.setUrlResourceInfo(urlResourceInfo);
			authoricty.setAuthority(rs.getString("authority"));
			authoricty.setSort(rs.getInt("sort"));
			if(hasAuthorityName){
				authoricty.setAuthorityName(rs.getString("authority_name"));
			}
	        return authoricty;
        }
		
	}
	
	@Data
	@ToString
	public static class AuthorityResource {
		//权限标识, permision.code
		private String authority;
		private List<UrlResourceInfo> urlResourceInfo;
		private Integer sort;
		private String authorityName;
		
		public List<UrlResourceInfo> getUrlResourceInfo() {
			return this.urlResourceInfo;
		}
	}

	@Data
	@AllArgsConstructor
	static class SortableAntPathRequestMatcher {
		private AntPathRequestMatcher pathMatcher;
		private Integer sort;
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SortableAntPathRequestMatcher other = (SortableAntPathRequestMatcher) obj;
			if (pathMatcher == null) {
				if (other.pathMatcher != null)
					return false;
			} else if (!pathMatcher.equals(other.pathMatcher))
				return false;
			return true;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((pathMatcher == null) ? 0 : pathMatcher.hashCode());
			return result;
		}
		@Override
		public String toString() {
			return pathMatcher.toString();
		}
		
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(RequestAuthorizationContext context) {
		Collection<ConfigAttribute> attrs = securityMetadataSource.getAttributes(context);
		return attrs;
	}

	public void setSecurityConfig(SecurityConfig securityConfig) {
		this.securityConfig = securityConfig;
	}

}

package org.onetwo.ext.security.metadata;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.ext.permission.utils.UrlResourceInfo;
import org.onetwo.ext.permission.utils.UrlResourceInfoParser;
import org.onetwo.ext.security.utils.SecurityUtils;
import org.springframework.core.io.Resource;
import org.springframework.expression.Expression;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
	
	private LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = Maps.newLinkedHashMap();
	
	private DefaultWebSecurityExpressionHandler securityExpressionHandler = new DefaultWebSecurityExpressionHandler();

	private boolean allowManyPermissionMapToOneUrl = true;

	private FilterSecurityInterceptor filterSecurityInterceptor;
	// from WebSecurityExpressionRoot
//	private Set<String> keywords = ImmutableSet.of("permitAll", "denyAll", "is", "has");
	
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
	public void buildRequestMap(){
		List<AuthorityResource> authorities = fetchAuthorityResources();
		final Map<SortableAntPathRequestMatcher, Collection<ConfigAttribute>> resouceMap = new HashMap<>(authorities.size());
		authorities.forEach(auth->{
			auth.getUrlResourceInfo().forEach(r->{
				//根据httpmethod和url映射权限标识，url拦截时也是根据这个matcher找到对应的权限SecurityConfig
//				AntPathRequestMatcher matcher = new AntPathRequestMatcher(r.getUrl(), r.getMethod());
				SortableAntPathRequestMatcher matcher = new SortableAntPathRequestMatcher(new AntPathRequestMatcher(r.getUrl(), r.getMethod()), auth.getSort());
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
	
	protected SecurityConfig createSecurityConfig(AuthorityResource auth){
		String exp = SecurityUtils.createSecurityExpression(auth.getAuthority());
		Expression authorizeExpression = this.securityExpressionHandler.getExpressionParser().parseExpression(exp);
		return new CodeSecurityConfig(auth, authorizeExpression);
	}
	
	private Map<RequestMatcher, Collection<ConfigAttribute>> defaultRequestMap;
	/****
	 * 基于url匹配拦截时，转换为ExpressionBasedFilterInvocationSecurityMetadataSource
	 * @param source
	 * @return
	 */
	@Override
	public void buildSecurityMetadataSource(){
		Assert.notNull(filterSecurityInterceptor, "filterSecurityInterceptor can not be null");
		this.buildRequestMap();
		
		Map<RequestMatcher, Collection<ConfigAttribute>> originRequestMap = getDefaultRequestMap();
		if(originRequestMap!=null && !originRequestMap.isEmpty()){
			this.requestMap.putAll(originRequestMap);
		}
		DefaultFilterInvocationSecurityMetadataSource fism = new DefaultFilterInvocationSecurityMetadataSource(requestMap);
		this.filterSecurityInterceptor.setSecurityMetadataSource(fism);
	}
	

	@SuppressWarnings("unchecked")
	protected final Map<RequestMatcher, Collection<ConfigAttribute>> getDefaultRequestMap() {
		Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = this.defaultRequestMap;
		if (requestMap==null) {
			DefaultFilterInvocationSecurityMetadataSource originMetadata = (DefaultFilterInvocationSecurityMetadataSource)filterSecurityInterceptor.getSecurityMetadataSource();
			//这个内置实现不支持一个url映射到多个表达式
//			ExpressionBasedFilterInvocationSecurityMetadataSource fism = new ExpressionBasedFilterInvocationSecurityMetadataSource(requestMap, securityExpressionHandler);
			requestMap = (Map<RequestMatcher, Collection<ConfigAttribute>>)ReflectUtils.getFieldValue(originMetadata, "requestMap", false);
			this.defaultRequestMap = requestMap;
		}
		return requestMap;
	}

	public void setFilterSecurityInterceptor(FilterSecurityInterceptor filterSecurityInterceptor) {
		this.filterSecurityInterceptor = filterSecurityInterceptor;
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
	
	public static class ResourceMapping extends MappingSqlQuery<AuthorityResource> implements RowMapper<AuthorityResource> {
		private UrlResourceInfoParser urlResourceInfoParser = new UrlResourceInfoParser();
		private boolean hasAuthorityName;

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
		//权限标识
		private String authority;
		private List<UrlResourceInfo> urlResourceInfo;
		private Integer sort;
		private String authorityName;
	}

	@Data
	@EqualsAndHashCode
	@AllArgsConstructor
	@ToString
	static class SortableAntPathRequestMatcher {
		private AntPathRequestMatcher pathMatcher;
		private Integer sort;
	}

	@SuppressWarnings("serial")
	public static class CodeSecurityConfig extends SecurityConfig {
		private final AuthorityResource auth;
		private final Expression authorizeExpression;
		// for sprig security 4.1, 可支持变量url 
//		private final EvaluationContextPostProcessor<FilterInvocation> postProcessor;
		
		public CodeSecurityConfig(AuthorityResource auth, Expression authorizeExpression) {
			super(SecurityUtils.createSecurityExpression(auth.getAuthority()));
			this.auth = auth;
			this.authorizeExpression = authorizeExpression;
		}
		public String getAuthorityName() {
			return auth.getAuthorityName();
		}
		
		public String getCode(){
			return auth.getAuthority();
		}

		public Expression getAuthorizeExpression() {
			return authorizeExpression;
		}
	}
}

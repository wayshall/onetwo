package org.onetwo.ext.permission;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.ext.permission.api.IPermission;
import org.onetwo.ext.permission.api.annotation.FullyAuthenticated;
import org.onetwo.ext.permission.parser.MenuInfoParser;
import org.onetwo.ext.security.metadata.SecurityMetadataSourceBuilder;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

abstract public class AbstractPermissionManager<P extends IPermission> implements PermissionManager<P> {

	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	/*@Resource
	protected MenuInfoParser<P> menuInfoParser;*/
	private List<MenuInfoParser<P>> parsers;
	private SecurityMetadataSourceBuilder securityMetadataSourceBuilder;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private Multimap<Method, IPermission> methodPermissionMapping;
	@Autowired
	private SecurityConfig securityConfig;
	
	@PostConstruct
	public void initParsers(){
		if(parsers==null || parsers.isEmpty()){
			this.parsers = SpringUtils.getBeans(applicationContext, MenuInfoParser.class, new ParameterizedTypeReference<MenuInfoParser<P>>(){});
		}
		checkParsers();
	}

	protected MenuInfoParser<P> getTopMenuInfoParser(){
		return parsers.get(0);
	}

	public void setParsers(List<MenuInfoParser<P>> parsers) {
		this.parsers = parsers;
	}
	
	private void checkParsers(){
		if (securityConfig.getPermission().isSync2db()) {
			Assert.notEmpty(parsers, "parsers can not be empty");
		}
	}


	public List<P> getMemoryRootMenu() {
		checkParsers();
//	    return this.menuInfoParser.getRootMenu();
	    return parsers.stream()
	    				.filter(p->p.getRootMenu().isPresent())
	    				.map(parser->parser.getRootMenu().get())
	    				.collect(Collectors.toList());
    }
	
	protected Set<String> getDatabaseRootCodes() {
		return Collections.emptySet();
	}

	/****
	 * 根据menuClass构建菜单
	 */
	@Override
	public void build(){
		checkParsers();
//		PermissionUtils.setMenuInfoParser(menuInfoParser);
		parsers.stream().forEach(parser->{
			Optional<P> rootMenu = parser.parseTree();
			afterParseTree(rootMenu.orElse(null));
		});
	}
	
	protected void afterParseTree(P rootMenu){
	}
	

	@Override
	public P getPermission(Class<?> permClass){
		checkParsers();
//		return menuInfoParser.getPermission(permClass);
		for(MenuInfoParser<P> parser : parsers){
			P perm = parser.getPermission(permClass);
			if(perm!=null)
				return perm;
		}
		return null;
	}
	
	/****
	 * 根据权限代码删除权限和相关已分配给角色的权限关联数据
	 * @author weishao zeng
	 * @param permissionCode
	 * @param usePostLike
	 */
	abstract protected void removePermission(String permissionCode, boolean usePostLike);
	
	/***
	 * syncMenuToDatabase菜单同步方法调用时，需要查找已存在的需要同步到菜单数据
	 * @param rootCode
	 * @return
	 */
	abstract protected Map<String, P> findExistsPermission(String rootCode);
	
	/***
	 * syncMenuToDatabase菜单同步方法调用时，通过对比内存和数据库检测需要变更的数据
	 * @param adds
	 * @param deletes
	 * @param updates
	 */
	abstract protected void updatePermissions(P rootPermission, Map<String, P> dbPermissionMap, Set<P> adds, Set<P> deletes, Set<P> updates);
	

	/****
	 * 当模块菜单的rootmenu被标记为过时时，对应的rootmenu为null
	 * @author wayshall
	 * @param menuInfoParser
	 */
	protected void removeRootMenu(MenuInfoParser<P> menuInfoParser){
		
	}
	
	/****
	 * 同步菜单
	 */
	@Override
	@Transactional
	public void syncMenuToDatabase(){
		parsers.stream().forEach(parser->syncMenuToDatabase(parser));

		Set<String> memoryRootCodes = parsers.stream()
											.filter(p -> p.getRootMenu().isPresent())
											.map(p -> p.getRootMenuCode())
											.collect(Collectors.toSet());
		Set<String> deleteRootCodes = Sets.difference(getDatabaseRootCodes(), memoryRootCodes);
		deleteRootCodes.stream().forEach(rootCode -> {
			if (isReversePermissions(rootCode)) {
				return;
			}
			this.removeUnusedRootMenu(rootCode);
		});
		
		if(securityMetadataSourceBuilder!=null) {
			securityMetadataSourceBuilder.buildSecurityMetadataSource();
		}
	}
	
	protected boolean isReversePermissions(String code) {
		return code.equalsIgnoreCase(FullyAuthenticated.AUTH_CODE);
	}
	
	protected void removeUnusedRootMenu(String rootCode) {
		this.removePermission(rootCode, true);
	}
	
	/***
	 * 刷新security权限数据
	 * @author weishao zeng
	 */
	public void refreshSecurityMetadataSource() {
		this.getSecurityMetadataSourceBuilder().buildSecurityMetadataSource();
	}
	
	public void syncMenuToDatabase(MenuInfoParser<P> menuInfoParser){
//		Class<?> rootMenuClass = this.menuInfoParser.getMenuInfoable().getRootMenuClass();
//		Class<?> permClass = this.menuInfoParser.getMenuInfoable().getIPermissionClass();
		if (logger.isInfoEnabled()) {
			logger.info("synchronizing menu class: {} ...", menuInfoParser.getRootMenuCode());
		}
		Optional<P> rootPermissionOpt = menuInfoParser.getRootMenu();
		if(!rootPermissionOpt.isPresent()){
			this.removeRootMenu(menuInfoParser);
			return ;
		}
		P rootPermission = rootPermissionOpt.get();
//		List<? extends IPermission> permList = (List<? extends IPermission>)this.baseEntityManager.findByProperties(permClass, "code:like", rootCode+"%");
//		Set<P> dbPermissions = findExistsPermission(rootPermission.getCode());
		Map<String, P> dbPermissionMap = findExistsPermission(rootPermission.getCode());
		Map<String, P> menuNodeMap = menuInfoParser.getPermissionMap();
		Set<P> memoryPermissions = new HashSet<>(menuNodeMap.values());

		Set<P> dbPermissions = new HashSet<P>(dbPermissionMap.values());
		Set<P> adds = Sets.difference(memoryPermissions, dbPermissions);
		Set<P> deletes = Sets.difference(dbPermissions, memoryPermissions);
		Set<P> intersections = Sets.intersection(memoryPermissions, dbPermissions);
		
//		filterReversePermissions(deletes);
		
		this.updatePermissions(rootPermission, dbPermissionMap, adds, deletes, intersections);

		logger.info("menu data has synchronized to database...");
	}

	public SecurityMetadataSourceBuilder getSecurityMetadataSourceBuilder() {
		SecurityMetadataSourceBuilder securityMetadataSourceBuilder = this.securityMetadataSourceBuilder;
		if (securityMetadataSourceBuilder==null) {
			securityMetadataSourceBuilder = SpringUtils.getBean(applicationContext, SecurityMetadataSourceBuilder.class);
		}
		return securityMetadataSourceBuilder;
	}

	public void setSecurityMetadataSourceBuilder(SecurityMetadataSourceBuilder securityMetadataSourceBuilder) {
		this.securityMetadataSourceBuilder = securityMetadataSourceBuilder;
	}

	public Multimap<Method, IPermission> getMethodPermissionMapping() {
		return methodPermissionMapping;
	}

	public void setMethodPermissionMapping(Multimap<Method, IPermission> methodPermissionMapping) {
		this.methodPermissionMapping = methodPermissionMapping;
	}

	/*@Override
	public String parseCode(Class<?> permClass) {
		return menuInfoParser.getCode(permClass);
	}*/
	
}

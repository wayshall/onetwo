package org.onetwo.ext.permission;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.ext.permission.entity.DefaultIPermission;
import org.onetwo.ext.permission.parser.MenuInfoParser;
import org.onetwo.ext.security.metadata.JdbcSecurityMetadataSourceBuilder;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Sets;

abstract public class AbstractPermissionManager<P extends DefaultIPermission<P>> implements PermissionManager<P> {

	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	/*@Resource
	protected MenuInfoParser<P> menuInfoParser;*/
	private List<MenuInfoParser<P>> parsers;
	private JdbcSecurityMetadataSourceBuilder databaseSecurityMetadataSource;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@PostConstruct
	public void initParsers(){
		if(parsers==null || parsers.isEmpty()){
			this.parsers = SpringUtils.getBeans(applicationContext, MenuInfoParser.class, new ParameterizedTypeReference<MenuInfoParser<P>>(){});
		}
		Assert.notEmpty(parsers);
	}

	protected MenuInfoParser<P> getTopMenuInfoParser(){
		return parsers.get(0);
	}

	public void setParsers(List<MenuInfoParser<P>> parsers) {
		this.parsers = parsers;
	}


	public List<P> getMemoryRootMenu() {
		Assert.notNull(parsers);
//	    return this.menuInfoParser.getRootMenu();
	    return parsers.stream().map(parser->parser.getRootMenu()).collect(Collectors.toList());
    }

	/****
	 * 根据menuClass构建菜单
	 */
	@Override
	public void build(){
		Assert.notNull(parsers);
//		PermissionUtils.setMenuInfoParser(menuInfoParser);
		parsers.stream().forEach(parser->{
			P rootMenu = parser.parseTree();
			afterParseTree(rootMenu);
		});
	}
	
	protected void afterParseTree(P rootMenu){
	}
	

	@Override
	public P getPermission(Class<?> permClass){
		Assert.notNull(parsers);
//		return menuInfoParser.getPermission(permClass);
		for(MenuInfoParser<P> parser : parsers){
			P perm = parser.getPermission(permClass);
			if(perm!=null)
				return perm;
		}
		return null;
	}
	
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
	 * 同步菜单
	 */
	@Override
	@Transactional
	public void syncMenuToDatabase(){
		parsers.stream().forEach(parser->syncMenuToDatabase(parser));
		if(databaseSecurityMetadataSource!=null)
			databaseSecurityMetadataSource.buildSecurityMetadataSource();
	}
	public void syncMenuToDatabase(MenuInfoParser<P> menuInfoParser){
//		Class<?> rootMenuClass = this.menuInfoParser.getMenuInfoable().getRootMenuClass();
//		Class<?> permClass = this.menuInfoParser.getMenuInfoable().getIPermissionClass();
		P rootPermission = menuInfoParser.getRootMenu();
//		List<? extends IPermission> permList = (List<? extends IPermission>)this.baseEntityManager.findByProperties(permClass, "code:like", rootCode+"%");
//		Set<P> dbPermissions = findExistsPermission(rootPermission.getCode());
		Map<String, P> dbPermissionMap = findExistsPermission(rootPermission.getCode());
		Map<String, P> menuNodeMap = menuInfoParser.getPermissionMap();
		Set<P> memoryPermissions = new HashSet<>(menuNodeMap.values());

		Set<P> dbPermissions = new HashSet<P>(dbPermissionMap.values());
		Set<P> adds = Sets.difference(memoryPermissions, dbPermissions);
		Set<P> deletes = Sets.difference(dbPermissions, memoryPermissions);
		Set<P> intersections = Sets.intersection(memoryPermissions, dbPermissions);
		
		this.updatePermissions(rootPermission, dbPermissionMap, adds, deletes, intersections);

		logger.info("menu data has synchronized to database...");
	}
	

	/*@Override
	public String parseCode(Class<?> permClass) {
		return menuInfoParser.getCode(permClass);
	}*/
}

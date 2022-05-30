package org.onetwo.ext.permission;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.permission.api.IPermission;
import org.onetwo.ext.permission.api.annotation.FullyAuthenticated;
import org.onetwo.ext.permission.parser.DefaultMenuInfoParser;
import org.onetwo.ext.permission.parser.MenuInfoParser;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author weishao zeng
 * <br/>
 */
public class MenuInfoParserFactory<P extends IPermission> implements InitializingBean, ApplicationContextAware {
	
	@Autowired(required = false)
	private Map<String, RootMenuClassProvider> rootMenuClassProviders;
	private Class<P> permissionClass;
	private List<SimplePermissionConfig<P>> permissionConfigList;
	private List<MenuInfoParser<P>> permissionParsers;
	private ApplicationContext applicationContext;
	

	public MenuInfoParserFactory(Class<P> permissionClass) {
		this(null, permissionClass);
	}
	
	public MenuInfoParserFactory(Map<String, RootMenuClassProvider> rootMenuClassProviders, Class<P> permissionClass) {
		this.rootMenuClassProviders = rootMenuClassProviders;
		this.permissionClass = permissionClass;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Collection<String> profiles = SpringUtils.getProfiles(applicationContext);
		List<SimplePermissionConfig<P>> list = Lists.newArrayList();
		
		if (LangUtils.isNotEmpty(rootMenuClassProviders)) {
			Logger logger = JFishLoggerFactory.getCommonLogger();
			if(logger.isInfoEnabled()){
				rootMenuClassProviders.forEach((k, v)->{
					List<Class<?>> rootMenuClass = Lists.newArrayList();//v.rootMenuClassList();
					rootMenuClass.addAll(v.rootMenuClassList());
					rootMenuClass.addAll(v.rootMenuClassListByProfiles(profiles));
//					if(v instanceof RootMenuClassListProvider){
//						rootMenuClass = ((RootMenuClassListProvider)v).rootMenuClassList();
//					}else{
//						rootMenuClass = v.rootMenuClass();
//					}
					logger.info("loading RootMenuClassProvider: {} -> {}", k, rootMenuClass);
				});
			}
			
			Collection<RootMenuClassProvider> providers = rootMenuClassProviders.values();
			providers.forEach(provider->{
				Collection<Class<?>> rooMenuClassList = new HashSet<>();
//				if(provider instanceof RootMenuClassListProvider){
//					rooMenuClassList.addAll(((RootMenuClassListProvider)provider).rootMenuClassList());
//				}else{
//					rooMenuClassList.add(provider.rootMenuClass());
//				}
				rooMenuClassList.addAll(provider.rootMenuClassList());
				rooMenuClassList.addAll(provider.rootMenuClassListByProfiles(profiles));
				
				rooMenuClassList.forEach(rootMenuClass->{
					SimplePermissionConfig<P> config = new SimplePermissionConfig<>(rootMenuClass, permissionClass);
//					config.setRootMenuClassProvider(provider);
//					config.setRootMenuClass(rootMenuClass);
					list.add(config);
				});
			});
		}
		

		SimplePermissionConfig<P> config = new SimplePermissionConfig<>(FullyAuthenticated.class, permissionClass);
		list.add(config);
		
		this.permissionConfigList = list;
		

		Set<Class<?>> menuClasses = Sets.newHashSetWithExpectedSize(list.size());
		List<MenuInfoParser<P>> parsers = list.stream().map(cfg->{
			if(menuClasses.contains(cfg.getRootMenuClass())){
				throw new BaseException("duplicate config menu class : " + cfg.getRootMenuClass());
			}
			menuClasses.add(cfg.getRootMenuClass());
			return new DefaultMenuInfoParser<P>(cfg);
		})
		.collect(Collectors.toList());
		
		this.permissionParsers = parsers;
	}
	
	public Optional<String> getPermissionCode(Class<?> permClass){
		for(MenuInfoParser<P> parser : permissionParsers){
			String code = parser.getCode(permClass);
			if(code!=null) {
				return Optional.of(code);
			}
		}
		return Optional.empty();
	}
	
	public List<MenuInfoParser<P>> getMnuInfoPrarseList() {
		return permissionParsers;
	}

	public List<SimplePermissionConfig<P>> getPermissionConfigList() {
		return permissionConfigList;
	}

	public void setRootMenuClassProviders(Map<String, RootMenuClassProvider> rootMenuClassProviders) {
		this.rootMenuClassProviders = rootMenuClassProviders;
	}

	public void setPermissionClass(Class<P> permissionClass) {
		this.permissionClass = permissionClass;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}

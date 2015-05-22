package org.onetwo.common.spring.plugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.JFishProperties;

import com.google.common.collect.ImmutableSet;

public class PluginInfo {
	
	public static class PKeys {
		public static final String NAME = "name";
		public static final String VERSION = "version";
		public static final String PLUGIN_CLASS = "pluginClass";
		public static final String DESC = "desc";
		public static final String DEPENDENCY = "dependency";
		public static final String SCOPE = "scope";
		public static final String WEBAPP_PLUGIN = "webapp.plugin";
		public static final String WEBAPP_PLUGIN_SERVER_LISTENER = "webapp.plugin.server.listener";
	}
	
	public void init(JFishProperties prop){
		PluginInfo info = this;
		info.name = prop.getAndThrowIfEmpty(PKeys.NAME);
		info.version = prop.getProperty(PKeys.VERSION, "1.0");
		info.pluginClass = prop.getProperty(PKeys.PLUGIN_CLASS);
//		info.pluginInstance = ReflectUtils.newInstance(cls);
//		info.contextPath = prop.getAndThrowIfEmpty("contextPath");
		info.contextPath = info.name;
		if(!info.contextPath.startsWith("/"))
			info.contextPath = "/" + info.contextPath;
		info.contextPath = StringUtils.trimEndWith(info.contextPath, "/");
		info.desc = prop.getProperty(PKeys.DESC);
		
		Set<DependencyPluginInfo> tempDependencies = new HashSet<>();
		List<String> dependencyList = prop.getPropertyWithSplit(PKeys.DEPENDENCY, ",");
		for(String dep : dependencyList){
			tempDependencies.add(DependencyPluginInfo.create(dep));
		}
		info.dependencies = ImmutableSet.copyOf(tempDependencies);
		
		info.scopes = prop.getPropertyWithSplit(PKeys.SCOPE, ",");
		info.webappPlugin = prop.getBoolean(PKeys.WEBAPP_PLUGIN, false);
		info.webappPluginServerListener = prop.getProperty(PKeys.WEBAPP_PLUGIN_SERVER_LISTENER, "");
		
		prop.remove(PKeys.NAME);
		prop.remove(PKeys.VERSION);
		prop.remove(PKeys.PLUGIN_CLASS);
		prop.remove(PKeys.DESC);
		prop.remove(PKeys.SCOPE);
		info.properties = prop;
	}

	private String version;
	private String name;
	private String pluginClass;
	private String contextPath;
	private String desc;
	private List<String> scopes;
	private JFishProperties properties;
	
	private Set<DependencyPluginInfo> dependencies;
	
	private boolean initialized;
	
	private boolean webappPlugin;
	private String webappPluginServerListener;

	public String getVersion() {
		return version;
	}

	public String getName() {
		return name;
	}


	public String getContextPath() {
		return contextPath;
	}
	
	public String getPluginClass() {
		return pluginClass;
	}

	public String toString(){
		return LangUtils.append("plugin{name:", name, ", version:", version, "}");
	}

	public JFishProperties getProperties() {
		return properties;
	}

	public String getDesc() {
		return desc;
	}

	/****
	 * 依赖的插件
	 * 默认非强制依赖，如果找到则首先加载依赖，否则会忽略
	 * '!'号前缀表示强制依赖，如果找不到依赖会抛异常
	 * @return
	 */
	public Collection<DependencyPluginInfo> getDependency() {
		return dependencies;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized() {
		this.initialized = true;
	}

	public List<String> getScopes() {
		return scopes;
	}

	public boolean applyOnScope(String scope){
		if(LangUtils.isEmpty(scopes)){
			return true;
		}
		return scopes.contains(scope);
	}
	
	public String wrapAsContextPath(String pluginContextUrl){
		String path = StringUtils.appendStartWith(pluginContextUrl, "/");
		return contextPath + path;
	}

	public boolean isWebappPlugin() {
		return webappPlugin;
	}

	public String getWebappPluginServerListener() {
		return webappPluginServerListener;
	}

	static class DependencyPluginInfo {
		private static final String FORCE_DEPENDENCY = "!";
		public static DependencyPluginInfo create(String dependencyString){
			DependencyPluginInfo d = null;
			if(dependencyString.startsWith(FORCE_DEPENDENCY)){
				d = new DependencyPluginInfo(dependencyString.substring(FORCE_DEPENDENCY.length()), true);
			}else{
				d = new DependencyPluginInfo(dependencyString, false);
			}
			return d;
		}
		final private String name;
		final private boolean force;
		
		
		public DependencyPluginInfo(String name, boolean force) {
			super();
			this.name = name;
			this.force = force;
		}
		public String getName() {
			return name;
		}
		public boolean isForce() {
			return force;
		}
		@Override
		public String toString() {
			return "DependencyPluginInfo [name=" + name + ", force=" + force
					+ "]";
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (force ? 1231 : 1237);
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DependencyPluginInfo other = (DependencyPluginInfo) obj;
			if (force != other.force)
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
		
		
	}
}

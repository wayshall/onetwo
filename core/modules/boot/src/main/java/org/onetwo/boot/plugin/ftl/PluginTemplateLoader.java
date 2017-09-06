package org.onetwo.boot.plugin.ftl;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.onetwo.boot.plugin.core.PluginManager;
import org.onetwo.boot.plugin.core.PluginMeta;
import org.onetwo.boot.plugin.core.WebPlugin;
import org.onetwo.boot.plugin.mvc.PluginContextHolder;
import org.onetwo.boot.plugin.mvc.PluginThreadContext;
import org.onetwo.boot.utils.PathMatchers;
import org.onetwo.boot.utils.PathMatchers.PathMatcher;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;

import freemarker.cache.StatefulTemplateLoader;
import freemarker.cache.TemplateLoader;

@SuppressWarnings({"unchecked", "rawtypes"})
public class PluginTemplateLoader implements StatefulTemplateLoader {
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private static final String ROOT_DIR_PREFIX = "~";
	private static final String CHINA_POSTFIX = "*"+Locale.CHINA.toString()+".*";
	private static final String CHINESE_POSTFIX = "*"+Locale.CHINESE.toString()+".*";
	private PathMatcher ignorePostfixMatcher = PathMatchers.anyPaths(CHINA_POSTFIX, CHINESE_POSTFIX);
	
//	public static final String ROOT_ACCESS = ":";

	private final PluginNameParser pluginNameParser;
	private final TemplateLoader[] loaders;
	private final Map lastLoaderForName = Collections.synchronizedMap(new HashMap());

	private Map<String, TemplateLoader> pluginLoaders = new HashMap<String, TemplateLoader>();
	final private PluginManager pluginManager;

	public PluginTemplateLoader(TemplateLoader[] loaders, PluginManager pluginManager) {
		this.loaders = (TemplateLoader[]) loaders.clone();
		this.pluginManager = pluginManager;
		this.pluginNameParser = pluginManager.getPluginNameParser();
	}

	public void putPluginTemplateLoader(WebPlugin plugin, TemplateLoader loader){
		this.pluginLoaders.put(plugin.getPluginMeta().getName(), loader);
	}
	/*public void addTemplateLoader(TemplateLoader loader) {
		loaders = (TemplateLoader[])ArrayUtils.add(loaders, loader);
	}*/

	public boolean containsPluginTemplateLoader(String name){
		return this.pluginLoaders.containsKey(name);
	}

	public Object findPluginTemplateSource(PluginMeta meta, String name) throws IOException {
		if(meta==null)
			return null;
		TemplateLoader loader = pluginLoaders.get(meta.getName());
		Object source = loader.findTemplateSource(name);
		if (source != null) {
			return new MultiSource(source, loader);
		}
		
		return null;
	}

	private PluginMeta getWebPluginMeta(String name){
		WebPlugin webplugin = pluginManager.getPlugin(name);
		if(webplugin==null)
			return null;
		return webplugin.getPluginMeta();
	}
	public Object findTemplateSource(String name) throws IOException {
		String fileName = FileUtils.getFileName(name);
		if(ignorePostfixMatcher.match(fileName)){
			return null;
		}
		
		String actualName = name;
		//for ftl view
		Object source = null;
		if(pluginNameParser.isPluginAccess(actualName)){
			String pname = pluginNameParser.getPluginName(actualName);
			if(pname!=null){
				PluginMeta meta = getWebPluginMeta(pname);
				actualName = actualName.substring(pname.length()+pluginNameParser.getLength());
				//必须要去掉开始的斜杠，否则找不到
				actualName = StringUtils.trimStartWith(actualName, "/");
				source = findPluginTemplateSource(meta, actualName);
			}
		}else{
			if(logger.isDebugEnabled()){
				logger.debug("actual view name: {}", actualName);
			}
			Optional<PluginThreadContext> context = PluginContextHolder.get();
			if (context.isPresent()) {//for controller
				if(!actualName.startsWith(ROOT_DIR_PREFIX)){
					source = findPluginTemplateSource(context.get().getPlugin().getPluginMeta(), actualName);
					if(source!=null){
						return source;
					}
				}else{
					//如果以 ~ 开头的模板，则从正常的根目录查找，如: ~/login
					actualName = actualName.substring(ROOT_DIR_PREFIX.length());
					//必须要去掉开始的斜杠，否则找不到
					actualName = StringUtils.trimStartWith(actualName, "/");
					if(logger.isDebugEnabled()){
						logger.debug("root view name: {}", actualName);
					}
				}
			}
		}
		
		if(source!=null)
			return source;
		
		
		// Use soft affinity - give the loader that last found this
		// resource a chance to find it again first.
		TemplateLoader lastLoader = (TemplateLoader) lastLoaderForName.get(name);
		if (lastLoader != null) {
			source = lastLoader.findTemplateSource(name);
			if (source != null) {
				return new MultiSource(source, lastLoader);
			}
		}
		
		/*Object source = findPluginTemplateSource(name);
		if(source!=null)
			return source;*/
		
		

		// If there is no affine loader, or it could not find the resource
		// again, try all loaders in order of appearance. If any manages
		// to find the resource, then associate it as the new affine loader
		// for this resource.
		for (int i = 0; i < loaders.length; ++i) {
			TemplateLoader loader = loaders[i];
			source = loader.findTemplateSource(actualName);
			if (source != null) {
				logger.info("lastLoaderForName: {}", source);
				lastLoaderForName.put(name, loader);
				return new MultiSource(source, loader);
			}
		}

		lastLoaderForName.remove(name);
		if(logger.isDebugEnabled()){
			logger.debug("return null");
		}
		// Resource not found
		return null;
	}

	public long getLastModified(Object templateSource) {
		return ((MultiSource) templateSource).getLastModified();
	}

	public Reader getReader(Object templateSource, String encoding) throws IOException {
		return ((MultiSource) templateSource).getReader(encoding);
	}

	public void closeTemplateSource(Object templateSource) throws IOException {
		((MultiSource) templateSource).close();
	}

	public void resetState() {
		lastLoaderForName.clear();
		for (int i = 0; i < loaders.length; i++) {
			TemplateLoader loader = loaders[i];
			if (loader instanceof StatefulTemplateLoader) {
				((StatefulTemplateLoader) loader).resetState();
			}
		}
	}

	private static final class MultiSource {
		private final Object source;
		private final TemplateLoader loader;

		MultiSource(Object source, TemplateLoader loader) {
			this.source = source;
			this.loader = loader;
		}

		long getLastModified() {
			return loader.getLastModified(source);
		}

		Reader getReader(String encoding) throws IOException {
			return loader.getReader(source, encoding);
		}

		void close() throws IOException {
			loader.closeTemplateSource(source);
		}

		public boolean equals(Object o) {
			if (o instanceof MultiSource) {
				MultiSource m = (MultiSource) o;
				return m.loader.equals(loader) && m.source.equals(source);
			}
			return false;
		}

		public int hashCode() {
			return loader.hashCode() + 31 * source.hashCode();
		}

		public String toString() {
			return source.toString();
		}
	}

	public static void main(String[] args){
		PluginTemplateLoader j = new PluginTemplateLoader(new TemplateLoader[]{}, null);
		System.out.println(j.pluginNameParser.getPluginName("lib/test/test.ftl"));
		String path = "[codegen]/lib/test/test.ftl";
		String pname = j.pluginNameParser.getPluginName(path);
		System.out.println(pname);
		System.out.println(path.substring(pname.length()+j.pluginNameParser.getLength()));
		
		path = "[codegen]/test.ftl";
		pname = j.pluginNameParser.getPluginName(path);
		System.out.println(pname);
		System.out.println(path.substring(pname.length()+j.pluginNameParser.getLength()));
		
		path = "[[codegen-test";
		pname = j.pluginNameParser.getPluginName(path);
		System.out.println("[["+pname);
		System.out.println("[["+path.substring(pname.length()+j.pluginNameParser.getLength()));
		

	}
}

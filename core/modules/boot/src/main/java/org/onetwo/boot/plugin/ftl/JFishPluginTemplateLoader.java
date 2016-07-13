package org.onetwo.common.fish.plugin;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.onetwo.common.utils.StringUtils;

import freemarker.cache.StatefulTemplateLoader;
import freemarker.cache.TemplateLoader;

@SuppressWarnings({"unchecked", "rawtypes"})
public class JFishPluginTemplateLoader implements StatefulTemplateLoader {
	
	private static final String CHINA_POSTFIX = Locale.CHINA.toString()+".ftl";
	private static final String CHINESE_POSTFIX = Locale.CHINESE.toString()+".ftl";
	
//	public static final String ROOT_ACCESS = ":";

	private final PluginNameParser newPnameParser = PluginNameParser.INSTANCE;
	private final TemplateLoader[] loaders;
	private final Map lastLoaderForName = Collections.synchronizedMap(new HashMap());

	private Map<String, TemplateLoader> pluginLoaders = new HashMap<String, TemplateLoader>();

	public JFishPluginTemplateLoader(TemplateLoader[] loaders, PluginNameParser pluginNameParser) {
		this.loaders = (TemplateLoader[]) loaders.clone();
		this.newPnameParser = pluginNameParser;
	}

	/*public void addTemplateLoader(TemplateLoader loader) {
		loaders = (TemplateLoader[])ArrayUtils.add(loaders, loader);
	}*/

	public boolean containsPluginTemplateLoader(String name){
		return this.pluginLoaders.containsKey(name);
	}
	public void addPluginTemplateLoader(String name, TemplateLoader loader) {
		this.pluginLoaders.put(name, loader);
	}
	

	/*public Object findPluginTemplateSource(String name) throws IOException {
		JFishPluginMeta meta = null;
		if(pnameParser.isPluginAccess(name)){
			String pname = pnameParser.getPluginName(name);
			if(StringUtils.isBlank(pname)){
				return null;
			}else{
				meta = pluginManager.getJFishPluginMeta(pname);
				name = name.substring(pname.length()+pnameParser.getLength());
				return findPluginTemplateSource(meta, name);
			}
		}
		Object controller = JFishWebUtils.currentController();
		if (controller == null) {
			return null;
		}
		meta = pluginManager.getJFishPluginMetaOf(controller.getClass());
		return findPluginTemplateSource(meta, name);
	}*/
	
	/*private String getPluginCacheKey(JFishPluginMeta meta, String name){
		String key = "plugin["+meta.getPluginInfo().getName()+"]"+name;
		return key;
	}*/

	public Object findPluginTemplateSource(JFishWebMvcPluginMeta meta, String name) throws IOException {
		if(meta==null)
			return null;
		TemplateLoader loader = pluginLoaders.get(meta.getPluginInfo().getName());
		Object source = loader.findTemplateSource(name);
		if (source != null) {
			return new MultiSource(source, loader);
		}
		
		return null;
	}

	private JFishWebMvcPluginMeta getJFishPluginMeta(String name){
		try {
			JFishWebMvcPluginMeta meta = pluginManager.getJFishPluginMeta(name);
			return meta;
		} catch (Exception e) {
			return null;
		}
	}
	public Object findTemplateSource(String name) throws IOException {
		if(name.endsWith(CHINA_POSTFIX) || name.endsWith(CHINESE_POSTFIX)){
			return null;
		}
		
		String actualName = name;
		//for ftl view
		Object source = null;
		if(newPnameParser.isPluginAccess(actualName)){
			String pname = newPnameParser.getPluginName(actualName);
			if(pname!=null){
				JFishWebMvcPluginMeta meta = getJFishPluginMeta(pname);
				actualName = actualName.substring(pname.length()+newPnameParser.getLength());
				actualName = StringUtils.trimStartWith(actualName, "/");
				source = findPluginTemplateSource(meta, actualName);
			}
		}else if(pnameParser.isPluginAccess(actualName)){
			String pname = pnameParser.getPluginName(actualName);
			if(pname!=null){
				JFishWebMvcPluginMeta meta = getJFishPluginMeta(pname);
				actualName = actualName.substring(pname.length()+pnameParser.getLength());
				actualName = StringUtils.trimStartWith(actualName, "/");
				source = findPluginTemplateSource(meta, actualName);
			}
		}/*else{
			Object controller = JFishWebUtils.currentController();
			if (controller != null) {//for controller
				JFishPluginMeta meta = pluginManager.getJFishPluginMetaOf(controller.getClass());
				if(meta!=null){
					source = findPluginTemplateSource(meta, actualName);
		
					if(source!=null)
						return source;
				}
			}
		}*/
		
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
				lastLoaderForName.put(name, loader);
				return new MultiSource(source, loader);
			}
		}

		lastLoaderForName.remove(name);
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
		JFishPluginTemplateLoader j = new JFishPluginTemplateLoader(new TemplateLoader[]{}, new PluginNameParser("[", "]"));
		System.out.println(j.newPnameParser.getPluginName("lib/test/test.ftl"));
		String path = "[codegen]/lib/test/test.ftl";
		String pname = j.newPnameParser.getPluginName(path);
		System.out.println(pname);
		System.out.println(path.substring(pname.length()+j.newPnameParser.getLength()));
		
		path = "[codegen]/test.ftl";
		pname = j.newPnameParser.getPluginName(path);
		System.out.println(pname);
		System.out.println(path.substring(pname.length()+j.newPnameParser.getLength()));
		
		path = "[[codegen-test";
		pname = j.newPnameParser.getPluginName(path);
		System.out.println("[["+pname);
		System.out.println("[["+path.substring(pname.length()+j.newPnameParser.getLength()));
		

	}
}

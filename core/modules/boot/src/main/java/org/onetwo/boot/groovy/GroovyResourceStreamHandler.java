package org.onetwo.boot.groovy;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.exception.ServiceException;

/**
 * @author weishao zeng
 * <br/>
 */
public class GroovyResourceStreamHandler extends URLStreamHandler {
	private GroovyScriptFinder groovyScriptFinder;

	public GroovyResourceStreamHandler(GroovyScriptFinder groovyScriptFinder) {
		super();
		this.groovyScriptFinder = groovyScriptFinder;
	}

	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		String path = u.getPath();
//		if (!path.startsWith("groovy://mysql/")) {
//			return u.openConnection();
//		}
//		if (!path.startsWith("/groovytest")) {
//			return new URL(path).openConnection();
//		}
		String[] paths = StringUtils.split(path, "/");
		String name = paths[0];
		GroovyScriptData scriptData = groovyScriptFinder.findScriptByCode(name);
		if (scriptData == null) {
			// 这里不能直接抛错，因为加载一些系统类时，也会经过此接口，转为普通url加载即可
//			throw new ServiceException("找不到查询脚本: " + path);
			return new URL(path).openConnection();
		}
		GroovyResourceConnection grc = new GroovyResourceConnection(u, groovyScriptFinder, scriptData);
		return grc;
	}

}
